package io.polymorphicpanda.polymorphic.ecs

import org.roaringbitmap.buffer.ImmutableRoaringBitmap
import org.roaringbitmap.buffer.MutableRoaringBitmap
import kotlin.reflect.KClass

typealias Entity = Int

class EntityEditor internal constructor(internal val entityReference: EntityReference,
                                        internal val componentTypeMap: Map<ComponentType, ComponentId>) {
    val entity: Entity = entityReference.entity
    fun <T: Component> get(component: KClass<T>): T =
        entityReference.get(componentTypeMap.getValue(component))
    fun contains(component: KClass<out Component>) =
        entityReference.contains(componentTypeMap.getValue(component))

    inline fun <reified T: Component> get() = get(T::class)
    inline fun <reified T: Component> contains() = contains(T::class)
}


internal interface EntityReference {
    val entity: Entity
    val bitSet: ImmutableRoaringBitmap
    fun add(componentId: ComponentId, component: Component)
    fun <T: Component> get(componentId: ComponentId): T
    fun contains(componentId: ComponentId) = bitSet.contains(componentId)
    fun remove(componentId: ComponentId)
    fun release()
    fun isValid(): Boolean
}

internal class DirtyEntityTracker {
    private val dirty = mutableSetOf<EntityReference>()

    fun track(entityReference: EntityReference) {
        dirty.add(entityReference)
    }

    fun dirtySet() = dirty.toMutableSet()

    fun clear() {
        dirty.clear()
    }
}

internal class EntityStorage(val dirtyEntityTracker: DirtyEntityTracker) {
    private val storage = mutableMapOf<Entity, MutableMap<ComponentId, Component>>()
    private val references = mutableMapOf<Entity, EntityReference>()

    fun manage(entity: Entity): EntityReference {
        return references.computeIfAbsent(entity) {
            val components = mutableMapOf<ComponentId, Component>()
            storage.put(entity, components)
            val index = MutableRoaringBitmap()

            object: EntityReference {
                private var valid = true
                override val entity = entity
                override val bitSet: ImmutableRoaringBitmap = index

                override fun add(componentId: ComponentId, component: Component) {
                    markDirty()
                    components.put(componentId, component)
                    index.add(componentId)
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T: Component> get(componentId: ComponentId): T = components[componentId] as T

                override fun remove(componentId: ComponentId) {
                    components.remove(componentId)?.let {
                        markDirty()
                        it.detached()
                        index.remove(componentId)
                    }
                }

                override fun release() {
                    markDirty()
                    storage.remove(entity)
                    references.remove(entity)
                    index.clear()
                    valid = false
                }

                override fun isValid() = valid

                private fun markDirty() {
                    dirtyEntityTracker.track(this)
                }
            }
        }
    }
}

internal interface EntityProvider {
    fun acquire(): Entity
    fun release(entity: Entity)
}

internal class BasicEntityProvider: EntityProvider {
    private var nextEntity = 0
    private val limbo = mutableListOf<Entity>()

    override fun acquire(): Entity {
        return if (limbo.isEmpty()) {
            nextEntity++
        } else {
            limbo.removeAt(0)
        }
    }

    override fun release(entity: Entity) {
        limbo.add(entity)
    }

}
