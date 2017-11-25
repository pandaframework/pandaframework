package io.polymorphicpanda.faux.core

abstract class Launcher {
    private lateinit var window: Window
    private lateinit var engine: Engine
    private val clock = Clock()

    fun run(args: Array<String>) {
        val settings = EngineSettings()
        val application = Bootstrapper().load()

        application.init(settings)
        initLauncherSettings(settings)
        initStandardSettings(settings)

        engine = createEngine(settings)
        window = WindowFactory.create(settings.windowConfig, engine)

        window.init()
        loop()
        window.dispose()
    }


    protected abstract fun initLauncherSettings(settings: EngineSettings)

    private fun loop() {
        var lastUpdate = clock.getTime()
        while (!window.shouldClose()) {
            window.pollEvents()

            engine.update(clock.getTime() - lastUpdate)

            window.flush()

            lastUpdate = clock.getTime()
        }
    }

    private fun initStandardSettings(settings: EngineSettings) {

    }

    private fun createEngine(settings: EngineSettings): Engine {
        return Engine(OpenGlBackend())
    }
}
