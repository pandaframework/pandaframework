package io.polymorphicpanda.faux.core.launcher

import io.polymorphicpanda.faux.component.Transform
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
import mu.KotlinLogging

open class Launcher {
    private val logger = KotlinLogging.logger {}

    private lateinit var window: Window
    private lateinit var engine: Engine
    private val clock: Clock = GlfwClock()


    fun launch(args: Array<String>) {
        val settings = EngineSettings()
        try {
            val application = ApplicationLoader().load()
            logger.info { "Bootstrapping engine." }
            registerStandardSettings(settings)
            application.init(settings)
            val executionModel = getExecutionModel(settings)

            if (settings.isDevelopmentMode()) {
                StatsHandler.setEnabled(true)
                logger.info { "Development mode is true." }
                logger.info { "Stats tracking enabled." }
                logger.info {
                    "Registered components: ${executionModel.componentMappings.keys.map { it.qualifiedName }}"
                }

                logger.info {
                    "Registered systems: ${executionModel.systems.keys.map { it.id.qualifiedName }}"
                }
            }

            engine = createEngine(executionModel)
            logger.info { "Setting up window." }
            window = WindowFactory.create(settings.windowConfig, engine)
            Faux.peer = engine
            logger.info { "Initializing peer." }
            window.init()
            logger.info { "Done!" }
            loop()
            logger.info { "Cleaning up." }
            window.dispose()
        } catch (e: Throwable) {
            logger.error(e) { "An error has occurred." }
        }
    }

    protected open fun getExecutionModel(settings: EngineSettings): EngineExecutionModel {
        return EngineExecutionModel.from(settings)
    }

    private fun registerStandardSettings(settings: EngineSettings) {
        settings.registerComponent(Transform.Descriptor)
    }

    private fun createEngine(executionModel: EngineExecutionModel): Engine {
        val entityStorage = EntityStorage()

        val globalContext = GlobalContextImpl(
            entityStorage,
            BasicEntityProvider(),
            ComponentPools(executionModel.components),
            executionModel
        )

        logger.info { "Using '${executionModel.graphics.name}' gfx backend." }
        return Engine(
            globalContext,
            executionModel
        )
    }

    private fun loop() {
        var lastUpdate = clock.getTime()
        while (!window.shouldClose()) {
            StatsHandler.frameTime {
                window.pollEvents()

                engine.update(clock.getTime() - lastUpdate)

                window.flush()

                lastUpdate = clock.getTime()
            }
        }
    }
}
