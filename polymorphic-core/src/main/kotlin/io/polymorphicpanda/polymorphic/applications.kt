package io.polymorphicpanda.polymorphic

import io.polymorphicpanda.polymorphic.ecs.ExecutionLayer

class Application {
    fun run() { TODO() }
}

class ApplicationBuilder {
    fun registerDispatcher(executionLayer: ExecutionLayer): ApplicationBuilder {
        return this
    }

    fun build(): Application { TODO() }
}
