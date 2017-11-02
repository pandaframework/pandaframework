package io.polymorphicpanda.polymorphic.ecs

abstract class Dispatcher(val System: List<System>) {
    abstract fun dispatch(elapsedTime: Double)
}

class SerialDispatcher(System: List<System>): Dispatcher(System) {
    override fun dispatch(elapsedTime: Double) {
        TODO()
    }
}

class ParallelDispatcher(System: List<System>): Dispatcher(System) {
    override fun dispatch(elapsedTime: Double) {
        TODO()
    }
}
