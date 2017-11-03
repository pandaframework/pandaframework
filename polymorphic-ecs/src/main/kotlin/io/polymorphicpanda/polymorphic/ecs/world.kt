package io.polymorphicpanda.polymorphic.ecs

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.reflect.KClass

class World internal constructor(
    private val executionLayers: List<ExecutionLayer>,
    componentTypes: List<ComponentType>
) {
    val worldContext: WorldContext = WorldContextImpl(componentTypes)
    inline private val worldContextImpl get() = worldContext as WorldContextImpl

    private var initRequired = true

    fun init() {
        executionLayers.flatMap(ExecutionLayer::systems)
            .forEach(worldContextImpl::registerSystem)

        initRequired = false
    }

    fun step(timeStep: Double) {
        if (initRequired) {
            throw IllegalStateException("World#init not called!")
        }

        for (executionLayer in executionLayers) {
            runBlocking {
                launch(CommonPool) { executionLayer.execute(timeStep, worldContextImpl::contextFor) }
            }

            worldContextImpl.resolveChangeSets()
        }
    }

    fun dispose() {
        executionLayers.flatMap(ExecutionLayer::systems)
            .forEach { it.dispose() }
    }
}

class WorldBuilder {
    private val componentTypes = mutableListOf<KClass<out Component>>()
    private val executionLayers = mutableListOf<ExecutionLayer>()

    fun withExecutionLayer(systems: List<System>): WorldBuilder {
        addExecutionLayer(SerialExecutionLayer(systems))
        return this
    }

    fun withAsyncExecutionLayer(systems: List<System>): WorldBuilder {
        addExecutionLayer(ParallelExecutionLayer(systems))
        return this
    }

    fun withComponents(componentTypes: List<KClass<out Component>>): WorldBuilder {
        this.componentTypes.addAll(componentTypes)
        return this
    }

    fun build(): World = World(
        executionLayers,
        componentTypes
    )

    private fun addExecutionLayer(executionLayer: ExecutionLayer) {
        executionLayers.add(executionLayer)
    }
}
