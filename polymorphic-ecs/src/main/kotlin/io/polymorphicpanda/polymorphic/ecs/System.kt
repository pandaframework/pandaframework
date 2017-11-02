package io.polymorphicpanda.polymorphic.ecs

abstract class System {
    abstract val aspect: Aspect

    open fun init() { }

    abstract suspend fun update(elapsedTime: Double, context: SystemContext)

    open fun dispose() { }
}
