package io.polymorphicpanda.faux.entity

import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.component.Component
import kotlin.reflect.KClass

typealias Entity = Int

abstract class EntityEditor {
    abstract val entity: Entity
    abstract fun <T: Component> get(component: KClass<T>): T
    abstract fun contains(component: KClass<out Component>): Boolean
    abstract fun <T: Component> add(component: T): EntityEditor
    abstract fun <T: Component> remove(componentType: KClass<T>): EntityEditor
    abstract fun destroy()

    inline fun <reified T: Component> remove(): EntityEditor = remove(T::class)
    inline fun <reified T: Component> get() = get(T::class)
    inline fun <reified T: Component> contains() = contains(T::class)
}

interface Context {
    fun create(blueprint: Blueprint? = null): EntityEditor
    fun manage(entity: Entity): EntityEditor
}
