package io.polymorphicpanda.polymorphic.ecs.entity

import io.polymorphicpanda.polymorphic.ecs.component.Component
import kotlin.reflect.KClass

typealias Entity = Int

abstract class WorldContext {
    abstract fun entities(): List<Entity>
    abstract fun changeset(action: ChangeSet.() -> Unit)
    inline fun <reified T: Component> get(entity: Entity) = get(entity, T::class)

    @PublishedApi
    abstract internal fun <T: Component> get(entity: Entity, component: KClass<T>): T
    @PublishedApi
    abstract internal fun contains(entity: Entity, component: KClass<out Component>): Boolean
}

abstract class ChangeSet {
    abstract fun EntityManager.create(): Entity
    abstract fun EntityManager.destroy(entity: Entity)
    abstract fun <T: Component> add(entity: Entity, component: T)
    inline fun <reified T: Component> ChangeSet.remove(entity: Entity) { remove(entity, T::class) }

    @PublishedApi
    abstract internal fun <T: Component> remove(entity: Entity, component: KClass<T>)
}
