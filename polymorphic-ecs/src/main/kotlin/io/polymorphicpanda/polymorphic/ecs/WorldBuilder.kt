package io.polymorphicpanda.polymorphic.ecs

import kotlin.reflect.KClass

class WorldBuilder {
    private val componentTypes = mutableListOf<KClass<out Component>>()
    private val executionLayers = mutableListOf<ExecutionLayer>()

    fun withExecutionLayer(systems: List<System>): WorldBuilder {
        addExecutionLayer(SerialExecutionLayer(systems))
        return this
    }

    fun withAsyncExectionLayer(systems: List<System>): WorldBuilder {
        addExecutionLayer(ParallelExecutionLayer(systems))
        return this
    }

    fun withComponents(componentTypes: List<KClass<out Component>>): WorldBuilder {
        this.componentTypes.addAll(componentTypes)
        return this
    }

    fun build(): World {
        val componentMapper = ComponentMapper()
        val componentIdMappings = componentTypes
            .associate { it to componentMapper.map(it) }

        return World(
            executionLayers,
            componentIdMappings
        )
    }

    private fun addExecutionLayer(executionLayer: ExecutionLayer) {
        executionLayers.add(executionLayer)
    }
}
