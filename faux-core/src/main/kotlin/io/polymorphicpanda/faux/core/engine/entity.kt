package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.component.Component
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.entity.Entity
import io.polymorphicpanda.faux.entity.EntityEditor
import org.roaringbitmap.buffer.ImmutableRoaringBitmap
import org.roaringbitmap.buffer.MutableRoaringBitmap
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

interface EntityProvider {
    fun acquire(): Entity
    fun release(entity: Entity)
}

class BasicEntityProvider: EntityProvider {
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

class EntityEditorImpl(private val entityStorageRef: EntityStorageRef,
                       private val componentMappings: Map<ComponentType, ComponentId>): EntityEditor() {
    override val entity = entityStorageRef.entity

    override fun <T: Component> get(componentType: KClass<T>): T {
        return entityStorageRef.get(componentMappings.getValue(componentType))
    }

    override fun contains(componentType: KClass<out Component>): Boolean {
        return entityStorageRef.contains(componentMappings.getValue(componentType))
    }

    override fun <T: Component> add(component: T): EntityEditor {
        entityStorageRef.add(componentMappings.getValue(component::class), component)
        return this
    }

    override fun <T: Component> remove(componentType: KClass<T>): EntityEditor {
        entityStorageRef.remove(componentMappings.getValue(componentType))
        return this
    }

    override fun destroy() {
        entityStorageRef.release()
    }

}

interface EntityStorageRef {
    val entity: Entity
    val bitSet: ImmutableRoaringBitmap
    fun add(componentId: ComponentId, component: Component)
    fun <T: Component> get(componentId: ComponentId): T
    fun contains(componentId: ComponentId) = bitSet.contains(componentId)
    fun remove(componentId: ComponentId)
    fun release()
    fun isValid(): Boolean
}

class EntityStorage {
    private val storage = mutableMapOf<Entity, MutableMap<ComponentId, Component>>()
    private val references = mutableMapOf<Entity, EntityStorageRef>()
    private val dirtySet = ConcurrentHashMap<EntityStorageRef, MutableList<() -> Unit>>()

    /**
     * any changes to the component composition will not be visible until this method
     * is invoked.
     */
    fun resolve(dirty: (EntityStorageRef) -> Unit) {
         dirtySet.forEach { (ref, actions) ->
             actions.forEach { it() }
             dirty(ref)
         }
        dirtySet.clear()
    }

    fun manage(entity: Entity): EntityStorageRef {
        return references.computeIfAbsent(entity) {
            val components = mutableMapOf<ComponentId, Component>()
            storage.put(entity, components)
            val index = MutableRoaringBitmap()

            object: EntityStorageRef {
                private var valid = true
                override val entity = entity
                override val bitSet: ImmutableRoaringBitmap = index

                override fun add(componentId: ComponentId, component: Component) {
                    markDirty {
                        components.put(componentId, component)
                        index.add(componentId)
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T: Component> get(componentId: ComponentId): T = components[componentId] as T

                override fun remove(componentId: ComponentId) {
                    components.remove(componentId)?.let {
                        markDirty {
                            it.detached()
                            index.remove(componentId)
                        }
                    }
                }

                override fun release() {
                    markDirty {
                        storage.remove(entity)
                        references.remove(entity)
                        index.clear()
                        valid = false
                    }
                }

                override fun isValid() = valid

                private fun markDirty(action: () -> Unit) {
                    dirtySet.computeIfAbsent(this, { mutableListOf() })
                        .add(action)
                }
            }
        }
    }
}
