package io.polymorphicpanda.polymorphic.ecs

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext

abstract class System {
    abstract val aspect: Aspect

    open fun init() { }

    abstract suspend fun process(timeStep: Double, context: SystemContext)

    open fun dispose() { }
}

internal typealias SystemContextProvider = (System) -> SystemContext

internal abstract class ExecutionLayer(val systems: List<System>) {
    abstract fun execute(coroutineContext: CoroutineContext, timeStep: Double, systemContextProvider: SystemContextProvider)
}

internal class SerialExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(coroutineContext: CoroutineContext,
                         timeStep: Double, systemContextProvider: (System) -> SystemContext) = runBlocking(coroutineContext) {
        for (system in systems) {
            system.process(timeStep, systemContextProvider(system))
        }
    }
}

internal class ParallelExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(coroutineContext: CoroutineContext,
                         timeStep: Double, systemContextProvider: (System) -> SystemContext) = runBlocking(coroutineContext) {
        val jobs = mutableListOf<Job>()

        for (system in systems) {
            jobs += launch(this.coroutineContext) { system.process(timeStep, systemContextProvider(system)) }
        }

        jobs.forEach {
            it.join()
        }
    }
}
