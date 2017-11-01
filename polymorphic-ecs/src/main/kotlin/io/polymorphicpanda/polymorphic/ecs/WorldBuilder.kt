package io.polymorphicpanda.polymorphic.ecs

import io.polymorphicpanda.polymorphic.ecs.system.Dispatcher

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
