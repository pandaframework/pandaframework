package io.polymorphicpanda.polymorphic.ecs

class WorldBuilder {
    private val dispatchers = mutableListOf<Dispatcher>()

    fun addDispatcher(dispatcher: Dispatcher): WorldBuilder {
        dispatchers.add(dispatcher)
        return this
    }

    fun build(): World {
        TODO()
    }
}
