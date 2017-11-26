package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.core.backend.Backend
import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.system.SystemDescriptor


class Engine(private val backend: Backend,
             private val globalContext: GlobalContextImpl,
             private val systemGraph: DynamicGraph<SystemDescriptor<*>>) {
    fun update(duration: Double) {}
    fun handleEvent(event: Event) {}
    fun handleWindowResize(width: Int, height: Int) {
        backend.handleWindowResize(width, height)
    }

    fun handleFramebufferResize(width: Int, height: Int) {
        backend.handleFramebufferResize(width, height)
    }
}
