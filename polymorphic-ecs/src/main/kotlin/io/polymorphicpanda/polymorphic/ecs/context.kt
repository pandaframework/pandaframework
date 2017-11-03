package io.polymorphicpanda.polymorphic.ecs

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

interface SystemContext: Context {
    fun entities(): List<Entity>
}

interface WorldContext: Context

internal class SystemContextImpl(
    private val worldContextImpl: WorldContextImpl,
    private val entityStorage: EntityStorage
): SystemContext, Context by worldContextImpl {
    override fun entities(): List<Entity> {
        TODO()
    }
}

internal class WorldContextImpl(componentTypes: List<ComponentType>): WorldContext {
    private val entityStorage = EntityStorage()
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

    fun registerSystem(system: System) {
        contextMappings.computeIfAbsent(system) {
            SystemContextImpl(this, entityStorage)
        }
    }

    fun resolveChangeSets() {
        changeSets.forEach { it(cs) }
    }

    fun contextFor(system: System): SystemContext = contextMappings.getValue(system)
}
