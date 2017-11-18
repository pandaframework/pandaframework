package io.polymorphicpanda.faux

import kotlinx.coroutines.experimental.channels.ProducerScope
import kotlinx.coroutines.experimental.channels.ReceiveChannel

data class Progress(val progress: Double, val description: String)

interface StateManager {
    fun set(state: State): ReceiveChannel<Progress>
    fun push(state: State): ReceiveChannel<Progress>
    fun peek(): Some<State>
    fun pop(): Some<State>
}

interface State {
    suspend fun ProducerScope<Progress>.init() { }
    fun dispose() { }
    fun update(duration: Double)
}
