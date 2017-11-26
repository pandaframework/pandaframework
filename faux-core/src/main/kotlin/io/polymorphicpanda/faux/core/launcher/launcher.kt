package io.polymorphicpanda.faux.core.launcher

import io.polymorphicpanda.faux.core.backend.OpenGlBackend
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.core.engine.BasicEntityProvider
import io.polymorphicpanda.faux.core.engine.ComponentPools
import io.polymorphicpanda.faux.core.engine.Engine
import io.polymorphicpanda.faux.core.engine.EngineModel
import io.polymorphicpanda.faux.core.engine.EntityStorage
import io.polymorphicpanda.faux.core.engine.GlobalContextImpl
import io.polymorphicpanda.faux.core.engine.SystemExecutor
import io.polymorphicpanda.faux.core.window.Window
import io.polymorphicpanda.faux.core.window.WindowFactory
import kotlinx.coroutines.experimental.CommonPool

abstract class EngineConfigurer {
    fun configure(settings: EngineSettings): Engine {
        configureStandard(settings)
        configureSpecific(settings)
        return createEngine(settings)
    }

    protected abstract fun configureSpecific(settings: EngineSettings)
    private fun configureStandard(settings: EngineSettings) {}
    private fun createEngine(settings: EngineSettings): Engine {
        val entityStorage = EntityStorage()
        val engineModel = EngineModel.from(settings)

        val globalContext = GlobalContextImpl(
            entityStorage,
            BasicEntityProvider(),
            ComponentPools(engineModel.components),
            engineModel
        )

        engineModel.systemInstances.forEach {
            globalContext.registerSystem(it.key, it.value)
        }

        return Engine(
            OpenGlBackend(),
            globalContext,
            engineModel.systemGraph,
            CommonPool,
            SystemExecutor()
        )
    }

}

class Launcher(val configurer: EngineConfigurer) {
    private lateinit var window: Window
    private lateinit var engine: Engine
    private val clock: Clock = GlfwClock()

    fun launch(args: Array<String>) {
        val settings = EngineSettings()
        val application = ApplicationLoader().load()
        application.init(settings)
        engine = configurer.configure(settings)
        window = WindowFactory.create(settings.windowConfig, engine)

        window.init()
        loop()
        window.dispose()
    }

    private fun loop() {
        var lastUpdate = clock.getTime()
        while (!window.shouldClose()) {
            window.pollEvents()

            engine.update(clock.getTime() - lastUpdate)

            window.flush()

            lastUpdate = clock.getTime()
        }
    }
}
