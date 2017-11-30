package io.polymorphicpanda.faux.core.debug

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer
import io.polymorphicpanda.faux.system.System

object StatsHandler: MetricRegistry() {
    private val systemTimers = mutableMapOf<System, Timer>()
    val frameTimer = timer("frame-time")

    fun systemTimer(system: System): Timer {
        return systemTimers.computeIfAbsent(system) {
            timer("system-time@${name(system::class.java)}")
        }
    }
}
