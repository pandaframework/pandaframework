package io.polymorphicpanda.faux

interface State {
    fun start(engine: Engine) { }
    fun stop(engine: Engine) { }
    fun pause(engine: Engine) { }
    fun update(elapsedTime: Double, engine: Engine) { }
}
