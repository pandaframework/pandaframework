package io.polymorphicpanda.polymorphic

import io.polymorphicpanda.polymorphic.ecs.Dispatcher

class Application {
    fun run() { TODO() }
}

class ApplicationBuilder {
    fun registerDispatcher(dispatcher: Dispatcher): ApplicationBuilder {
        return this
    }

    fun build(): Application { TODO() }
}
