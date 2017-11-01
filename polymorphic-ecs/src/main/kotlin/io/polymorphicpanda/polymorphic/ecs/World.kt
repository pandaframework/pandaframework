package io.polymorphicpanda.polymorphic.ecs

import io.polymorphicpanda.polymorphic.ecs.entity.EntityManager

class World internal constructor(
    val entityManager: EntityManager
) {
    fun step(elapsedTime: Double) {}
}
