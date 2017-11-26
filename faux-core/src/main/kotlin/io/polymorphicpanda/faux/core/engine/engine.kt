package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.core.backend.Backend
import io.polymorphicpanda.faux.event.Event


class Engine(private val backend: Backend) {
    fun update(duration: Double) {}
    fun handleEvent(event: Event) {}
    fun handleWindowResize(width: Int, height: Int) {
        backend.handleWindowResize(width, height)
    }

    fun handleFramebufferResize(width: Int, height: Int) {
        backend.handleFramebufferResize(width, height)
    }
}
