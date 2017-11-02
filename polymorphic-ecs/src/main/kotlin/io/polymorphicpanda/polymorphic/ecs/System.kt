package io.polymorphicpanda.polymorphic.ecs

abstract class System {
    abstract val aspect: Aspect

    open fun init() { }

    abstract suspend fun process(timeStep: Double, context: SystemContext)

    open fun dispose() { }
}
