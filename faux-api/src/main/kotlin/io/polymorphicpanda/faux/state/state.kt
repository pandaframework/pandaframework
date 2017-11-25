package io.polymorphicpanda.faux.state

import io.polymorphicpanda.faux.entity.Context
import io.polymorphicpanda.faux.runtime.Descriptor
import io.polymorphicpanda.faux.runtime.Faux
import io.polymorphicpanda.faux.service.Service
import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel

data class Progress(val progress: Double, val description: String)
interface WorldContext: Context

abstract class State {
    inline fun <reified T: Service> service() = lazy { Faux.getService<T>() }
    val stateManager by service<StateManager>()

    suspend open fun ProducerScope<Progress>.init() { }
    open fun dispose() { }
    open fun update(duration: Double, context: WorldContext) { }
}

interface StateDescriptor<T: State>: Descriptor<T>
interface StateManager: Service {
    fun set(descriptor: StateDescriptor<*>): ReceiveChannel<Progress>
    fun push(descriptor: StateDescriptor<*>): ReceiveChannel<Progress>
    fun peek(): StateDescriptor<*>
    fun pop(): StateDescriptor<*>
}

abstract class TransitionState<T: State>: State() {
    abstract val nextState: StateDescriptor<T>
    var progressChannel: ReceiveChannel<Progress>? = null

    suspend override fun ProducerScope<Progress>.init() {
        progressChannel = stateManager.set(nextState)
    }

    override final fun update(duration: Double, context: WorldContext) {
        try {
            if (progressChannel != null) {
                progressChannel!!.poll()?.let { progress ->
                    onUpdate(progress)
                }
            } else {
                onTransitionError()
            }
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    protected abstract fun onUpdate(progress: Progress)
    protected abstract fun onError(throwable: Throwable)
    protected abstract fun onTransitionError()
}
