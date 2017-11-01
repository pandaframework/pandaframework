package io.polymorphicpanda.polymorphic.ecs.entity

import io.polymorphicpanda.polymorphic.ecs.component.Component
import kotlin.reflect.KClass

typealias Entity = Int

interface EntitySubscription {
    fun entities(): List<Entity>
}

interface Mapper<out T: Component> {
    fun get(entity: Entity): T
    fun contains(entity: Entity): Boolean
}

interface ChangeSet {
    fun EntityManager.create(): Entity
    fun <T: Component> Mapper<T>.create(entity: Entity): T
    fun <T: Component> Mapper<T>.remove(entity: Entity)
}

interface EntityManager {
    fun subscribe(aspect: Aspect): EntitySubscription
    fun changeset(cs: ChangeSet.() -> Unit)
    fun <T: Component> mapperFor(component: KClass<T>): Mapper<T>
}
