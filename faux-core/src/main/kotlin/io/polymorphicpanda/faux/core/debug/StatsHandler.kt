package io.polymorphicpanda.faux.core.debug

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.Timer
import io.polymorphicpanda.faux.system.System
import java.util.concurrent.TimeUnit

object StatsHandler: MetricRegistry() {
    private val statsReporter = JmxReporter.forRegistry(this)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()

    private var enabled = false
    private val systemTimers = mutableMapOf<System, Timer>()
    private val frameTimer = timer("frame-time")

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled

        if (enabled) {
            statsReporter.start()
        }
    }

    suspend fun frameTime(frame: suspend () -> Unit) {
        if (enabled) {
            val context = frameTimer.time()
            try {
                frame()
            } finally {
                context.stop()
            }
        } else {
            frame()
        }
    }

    suspend fun systemTime(system: System, exec: suspend () -> Unit) {
        val timer = systemTimers.computeIfAbsent(system) {
            timer("system-time@${name(system::class.java)}")
        }

        val context = timer.time()
        try {
            exec()
        } finally {
            context.stop()
        }
    }
}
