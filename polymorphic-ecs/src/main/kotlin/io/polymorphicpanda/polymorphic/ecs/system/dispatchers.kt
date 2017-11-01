package io.polymorphicpanda.polymorphic.ecs.system

abstract class Dispatcher(val systems: List<System>) {
    abstract fun dispatch(elapsedTime: Double)
}

class SerialDispatcher(systems: List<System>): Dispatcher(systems) {
    override fun dispatch(elapsedTime: Double) {
        TODO()
    }
}

class ParallelDispatcher(systems: List<System>): Dispatcher(systems) {
    override fun dispatch(elapsedTime: Double) {
        TODO()
    }
}
