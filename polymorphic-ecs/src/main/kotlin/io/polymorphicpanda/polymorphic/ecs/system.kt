package io.polymorphicpanda.polymorphic.ecs

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

abstract class System {
    abstract val aspect: Aspect

    open fun init() { }

    abstract suspend fun process(timeStep: Double, context: SystemContext)

    open fun dispose() { }
}


internal abstract class ExecutionLayer(val systems: List<System>) {
    abstract fun execute(timeStep: Double, systemContextProvider: (System) -> SystemContext)
}

internal class SerialExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(timeStep: Double, systemContextProvider: (System) -> SystemContext) = runBlocking {
        for (system in systems) {
            system.process(timeStep, systemContextProvider(system))
        }
    }
}

internal class ParallelExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(timeStep: Double, systemContextProvider: (System) -> SystemContext) = runBlocking {
        val jobs = mutableListOf<Job>()

        for (system in systems) {
            jobs += launch { system.process(timeStep, systemContextProvider(system)) }
        }

        jobs.forEach {
            it.join()
        }
    }
}
