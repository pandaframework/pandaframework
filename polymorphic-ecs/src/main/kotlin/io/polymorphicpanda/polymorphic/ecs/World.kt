package io.polymorphicpanda.polymorphic.ecs

class World internal constructor(
    private val executionLayers: List<ExecutionLayer>,
    private val componentIdMappings: Map<ComponentType, ComponentId>
) {
    val worldContext: WorldContext get() = TODO()

    fun step(timeStep: Double) {}
}
