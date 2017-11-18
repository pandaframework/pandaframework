package io.polymorphicpanda.faux

typealias Entity = Int
interface EntityEditor
class Aspect

interface Descriptor<out T> {
    fun create(): T
}

interface ComponentDescriptor<out T: Component>: Descriptor<T>
interface Component {
    fun detached()
}

interface BlueprintDescriptor<out T: Blueprint>: Descriptor<T>
interface Blueprint

interface Context {
    fun create(blueprint: Blueprint? = null): Entity
    fun manage(entity: Entity): Try<EntityEditor>
}

interface WorldContext: Context

interface SystemContext: Context

abstract class System {
    abstract val aspect: Aspect
    abstract fun process(duration: Double, context: SystemContext)
}
