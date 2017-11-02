package io.polymorphicpanda.polymorphic.ecs

import kotlin.reflect.KClass

abstract class ChangeSet {
    abstract fun Context.create(): EntityEditor

    abstract fun <T: Component> EntityEditor.add(component: T): EntityEditor
    abstract fun <T: Component> EntityEditor.remove(componentType: KClass<T>): EntityEditor
    abstract fun EntityEditor.destroy()
    inline fun <reified T: Component> EntityEditor.remove(): EntityEditor = remove(T::class)
}
