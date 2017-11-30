package io.polymorphicpanda.faux.core.launcher

import com.codahale.metrics.JmxReporter
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.core.debug.StatsHandler
import io.polymorphicpanda.faux.core.engine.BasicEntityProvider
import io.polymorphicpanda.faux.core.engine.ComponentPools
import io.polymorphicpanda.faux.core.engine.Engine
import io.polymorphicpanda.faux.core.engine.EngineExecutionModel
import io.polymorphicpanda.faux.core.engine.EntityStorage
import io.polymorphicpanda.faux.core.engine.GlobalContextImpl
import io.polymorphicpanda.faux.core.window.Window
import io.polymorphicpanda.faux.core.window.WindowFactory
import io.polymorphicpanda.faux.runtime.Faux
import java.util.concurrent.TimeUnit

open class Launcher {
    private lateinit var window: Window
    private lateinit var engine: Engine
    private val clock: Clock = GlfwClock()
    private val statsReporter = JmxReporter.forRegistry(StatsHandler)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build()

    fun launch(args: Array<String>) {
        val settings = EngineSettings()
        val application = ApplicationLoader().load()
        application.init(settings)
        val executionModel = getExecutionModel(settings)
        engine = createEngine(executionModel)
        window = WindowFactory.create(settings.windowConfig, engine)
        Faux.peer = engine
        window.init()
        statsReporter.start()
        loop()
        window.dispose()
    }

    protected open fun getExecutionModel(settings: EngineSettings): EngineExecutionModel {
        return EngineExecutionModel.from(settings)
    }

    private fun createEngine(executionModel: EngineExecutionModel): Engine {
        val entityStorage = EntityStorage()

        val globalContext = GlobalContextImpl(
            entityStorage,
            BasicEntityProvider(),
            ComponentPools(executionModel.components),
            executionModel
        )

        return Engine(
            globalContext,
            executionModel
        )
    }

    private fun loop() {
        var lastUpdate = clock.getTime()
        while (!window.shouldClose()) {
            StatsHandler.frameTimer.time {
                window.pollEvents()

                engine.update(clock.getTime() - lastUpdate)

                window.flush()

                lastUpdate = clock.getTime()
            }
        }
    }
}
