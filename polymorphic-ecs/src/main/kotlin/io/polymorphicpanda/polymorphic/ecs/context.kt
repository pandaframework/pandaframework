package io.polymorphicpanda.polymorphic.ecs

import org.roaringbitmap.buffer.ImmutableRoaringBitmap
import org.roaringbitmap.buffer.MutableRoaringBitmap
import kotlin.reflect.KClass

class ChangeSet internal constructor(private val componentTypeMap: Map<ComponentType, ComponentId>,
                                              private val entityStorage: EntityStorage) {
    private val entityEditors = mutableMapOf<Entity, EntityEditor>()
    private val entityProvider = BasicEntityProvider()

    fun Context.create() = editorFor(entityProvider.acquire())

    fun <T: Component> EntityEditor.add(component: T): EntityEditor {
        entityReference.add(componentTypeMap.getValue(component::class), component)
        return this
    }

    fun <T: Component> EntityEditor.remove(componentType: KClass<T>): EntityEditor {
        entityReference.remove(componentTypeMap.getValue(componentType))
        return this
    }

    fun EntityEditor.destroy() {
        entityReference.release()
        entityProvider.release(entity)
        entityEditors.remove(entity)
    }

    inline fun <reified T: Component> EntityEditor.remove(): EntityEditor = remove(T::class)

    internal fun editorFor(entity: Entity): EntityEditor {
        return entityEditors.computeIfAbsent(entity) {
            EntityEditor(entityStorage.manage(entity), componentTypeMap)
        }
    }
}

interface Context {
    fun manage(entity: Entity): EntityEditor
    fun changeSet(cs: ChangeSet.() -> Unit)
}

class SystemContext internal constructor(
    private val worldContext: WorldContext,
    private val includedBitSet: ImmutableRoaringBitmap,
    private val excludedBitSet: ImmutableRoaringBitmap
): Context by worldContext {
    private val trackedEntities = mutableSetOf<Entity>()

    fun entities(): Set<Entity> = trackedEntities

    internal fun update(dirtyEntities: Set<EntityReference>) {
        dirtyEntities.forEach { entityRef ->
            if (entityRef.isValid()) {
                val bitSet = entityRef.bitSet
                val alreadyTracked = trackedEntities.contains(entityRef.entity)
                val matched = bitSet.contains(includedBitSet) &&
                    !bitSet.contains(excludedBitSet)

                if (alreadyTracked && !matched) {
                    trackedEntities.remove(entityRef.entity)
                } else if (!alreadyTracked && matched) {
                    trackedEntities.add(entityRef.entity)
                }

            } else {
                // entity has been destroyed
                trackedEntities.remove(entityRef.entity)
            }
        }
    }
}

class WorldContext internal constructor(componentTypes: List<ComponentType>): Context {
    private val dirtyEntityTracker = DirtyEntityTracker()
    private val entityStorage = EntityStorage(dirtyEntityTracker)
    private val contextMappings = mutableMapOf<System, SystemContext>()
    private val componentTypeMap: Map<ComponentType, ComponentId>
    private val changeSets = mutableListOf<ChangeSet.() -> Unit>()

    init {
        val mapper = ComponentMapper()
        componentTypeMap = componentTypes.associate { it to mapper.map(it) }
    }

    private val cs = ChangeSet(componentTypeMap, entityStorage)

    override fun manage(entity: Entity) = cs.editorFor(entity)

    override fun changeSet(cs: ChangeSet.() -> Unit) {
        synchronized(changeSets) {
            changeSets.add(cs)
        }
    }

    internal fun registerSystem(system: System) {
        contextMappings.computeIfAbsent(system) {
            val includedBitSet = MutableRoaringBitmap()
            val excludedBitSet = MutableRoaringBitmap()
            val aspect = system.aspect
            aspect.included.map { componentTypeMap.getValue(it) }
                .forEach { includedBitSet.add(it) }

            aspect.excluded.map { componentTypeMap.getValue(it) }
                .forEach { excludedBitSet.add(it) }

            SystemContext(this, includedBitSet, excludedBitSet)
        }
    }

    internal fun resolveChangeSets() {
        changeSets.forEach { it(cs) }
        val dirtyEntities = dirtyEntityTracker.dirtySet()
        if (dirtyEntities.isNotEmpty()) {
            contextMappings.values.forEach {
                it.update(dirtyEntities)
            }
            dirtyEntityTracker.clear()
        }
    }

    internal fun contextFor(system: System): SystemContext = contextMappings.getValue(system)
}
