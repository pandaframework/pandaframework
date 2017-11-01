package io.polymorphicpanda.polymorphic

interface State {
    fun start(engine: Engine)
    fun update(engine: Engine, elapsedTime: Double)
    fun stop(engine: Engine)
}
