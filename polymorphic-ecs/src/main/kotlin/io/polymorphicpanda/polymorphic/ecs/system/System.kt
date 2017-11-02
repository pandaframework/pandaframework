package io.polymorphicpanda.polymorphic.ecs.system

import io.polymorphicpanda.polymorphic.ecs.entity.Aspect
import io.polymorphicpanda.polymorphic.ecs.entity.WorldContext

abstract class System {
    abstract val aspect: Aspect

    open fun init() { }

    abstract suspend fun update(elapsedTime: Double, context: WorldContext)

    open fun dispose() { }
}
