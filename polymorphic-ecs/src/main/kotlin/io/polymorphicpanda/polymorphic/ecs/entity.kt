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
    val characteristics: ImmutableRoaringBitmap
    fun add(componentId: ComponentId, component: Component)
    fun <T: Component> get(componentId: ComponentId): T
    fun contains(componentId: ComponentId) = characteristics.contains(componentId)
    fun remove(componentId: ComponentId)
    fun release()
}

internal class EntityStorage {
    private val storage = mutableMapOf<Entity, MutableMap<ComponentId, Component>>()
    private val references = mutableMapOf<Entity, EntityReference>()

    fun manage(entity: Entity): EntityReference {
        return references.computeIfAbsent(entity) {
            val components = mutableMapOf<ComponentId, Component>()
            storage.put(entity, components)
            val index = MutableRoaringBitmap()

            object: EntityReference {
                override val entity = entity
                override val characteristics: ImmutableRoaringBitmap = index

                override fun add(componentId: ComponentId, component: Component) {
                    components.put(componentId, component)
                    index.add(componentId)
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T: Component> get(componentId: ComponentId): T = components[componentId] as T

                override fun remove(componentId: ComponentId) {
                    components.remove(componentId)?.let {
                        it.detached()
                        index.remove(componentId)
                    }
                }

                override fun release() {
                    storage.remove(entity)
                    references.remove(entity)
                    index.clear()
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
