package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.entity.Context
import io.polymorphicpanda.faux.entity.Entity
import io.polymorphicpanda.faux.entity.EntityEditor
import io.polymorphicpanda.faux.state.GlobalContext
import io.polymorphicpanda.faux.system.SystemContext
import io.polymorphicpanda.faux.system.SystemDescriptor
import org.roaringbitmap.buffer.ImmutableRoaringBitmap
import java.util.concurrent.ConcurrentHashMap

class GlobalContextImpl(private val entityStorage: EntityStorage,
                        private val entityProvider: EntityProvider,
                        private val componentMappings: Map<ComponentType, ComponentId>): GlobalContext {
    private val entityEditors = ConcurrentHashMap<Entity, EntityEditor>()
    private val systemContextMappings = mutableMapOf<SystemDescriptor<*>, SystemContextImpl>()

    override fun create(blueprint: Blueprint?): EntityEditor {
        return manage(entityProvider.acquire())
    }

    override fun manage(entity: Entity): EntityEditor {
        return entityEditors.computeIfAbsent(entity) {
            EntityEditorImpl(entityStorage.manage(entity), componentMappings)
        }
    }

    fun resolve() {
        entityStorage.resolve { dirty ->
            systemContextMappings.forEach { _, systemContext -> systemContext.resolve(dirty) }
            entityProvider.release(dirty.entity)
        }
    }
}

class SystemContextImpl(globalContext: GlobalContext,
                        private val includedBitSet: ImmutableRoaringBitmap,
                        private val excludedBitSet: ImmutableRoaringBitmap)
    : SystemContext, Context by globalContext {
    private val trackedEntities = mutableSetOf<Entity>()

    override fun entities(): Set<Entity> = trackedEntities

    fun resolve(entityStorageRef: EntityStorageRef) {
        if (entityStorageRef.isValid()) {
            val bitSet = entityStorageRef.bitSet
            val alreadyTracked = trackedEntities.contains(entityStorageRef.entity)
            val matched = bitSet.contains(includedBitSet) &&
                !bitSet.contains(excludedBitSet)

            if (alreadyTracked && !matched) {
                trackedEntities.remove(entityStorageRef.entity)
            } else if (!alreadyTracked && matched) {
                trackedEntities.add(entityStorageRef.entity)
            }

        } else {
            // entity has been destroyed
            trackedEntities.remove(entityStorageRef.entity)
        }
    }
}
