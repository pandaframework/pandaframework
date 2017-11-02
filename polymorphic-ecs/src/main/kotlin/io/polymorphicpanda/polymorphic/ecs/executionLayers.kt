package io.polymorphicpanda.polymorphic.ecs

abstract internal class ExecutionLayer(val systems: List<System>) {
    abstract fun execute(elapsedTime: Double)
}

internal class SerialExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(elapsedTime: Double) {
        TODO()
    }
}

internal class ParallelExecutionLayer(systems: List<System>): ExecutionLayer(systems) {
    override fun execute(elapsedTime: Double) {
        TODO()
    }
}
