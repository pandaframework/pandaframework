package io.polymorphicpanda.polymorphic.ecs.system

import io.polymorphicpanda.polymorphic.ecs.entity.EntityManager

abstract class System {
    internal var _entityManager: EntityManager? = null

    val entityManager by lazy { _entityManager }

    open fun init() { }

    abstract fun update(elapsedTime: Double)

    open fun dispose() { }
}
