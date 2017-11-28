package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.core.backend.Backend
import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.runtime.EnginePeer
import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.system.System
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.KClass


class Engine(private val backend: Backend,
             private val globalContext: GlobalContextImpl,
             private val systemGraph: DynamicGraph<System>,
             private val coroutineContext: CoroutineContext,
             private val systemExecutor: SystemExecutor): EnginePeer {
    fun update(duration: Double) {
        // TODO: execute state

        systemExecutor.execute(coroutineContext, duration, systemGraph.clone(), globalContext::contextFor)
    }

    fun handleEvent(event: Event) {}
    fun handleWindowResize(width: Int, height: Int) {
        backend.handleWindowResize(width, height)
    }

    fun handleFramebufferResize(width: Int, height: Int) {
        backend.handleFramebufferResize(width, height)
    }

    override fun <T: Service> getService(service: KClass<T>): T {
        TODO()
    }

    override fun getSharedPool() = coroutineContext
}
