package io.polymorphicpanda.polymorphic.ecs

import kotlin.reflect.KClass

typealias Entity = Int

abstract class EntityEditor(val entity: Entity) {

    abstract fun <T: Component> get(component: KClass<T>): T
    abstract fun contains(component: KClass<out Component>): Boolean
    inline fun <reified T: Component> get() = get(T::class)
    inline fun <reified T: Component> contains() = contains(T::class)
}
