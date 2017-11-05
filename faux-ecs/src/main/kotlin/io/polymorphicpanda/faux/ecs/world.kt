package io.polymorphicpanda.faux.ecs

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlin.reflect.KClass

class World internal constructor(
    val worldContext: WorldContext,
    internal val executionLayers: List<ExecutionLayer>,
    val coroutineDispatcher: CoroutineDispatcher
) {
    private var initRequired = true

    fun init() {
        executionLayers.flatMap(ExecutionLayer::systems)
            .forEach { system ->
                worldContext.registerSystem(system)
                system.init()
            }

        initRequired = false
    }

    fun step(timeStep: Double) {
        if (initRequired) {
            throw IllegalStateException("World#init not called!")
        }

        for (executionLayer in executionLayers) {
            executionLayer.execute(coroutineDispatcher, timeStep, worldContext::contextFor)

            worldContext.resolveChangeSets()
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
    private var coroutineDispatcher: CoroutineDispatcher = CommonPool

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

    fun withCoroutineDispatcher(coroutineDispatcher: CoroutineDispatcher): WorldBuilder {
        this.coroutineDispatcher = coroutineDispatcher
        return this
    }

    fun build(): World {
        val mapper = ComponentMapper()

        val editScope = EditScope(
            componentTypes.associate { it to mapper.map(it) },
            EntityStorage(DirtyEntityTracker()),
            BasicEntityProvider()
        )

        return World(
            WorldContext(editScope),
            executionLayers,
            coroutineDispatcher
        )
    }

    private fun addExecutionLayer(executionLayer: ExecutionLayer) {
        executionLayers.add(executionLayer)
    }
}
