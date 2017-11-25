package io.polymorphicpanda.faux.core

abstract class Launcher {
    fun run(args: Array<String>) {
        val configurer = EngineConfigurer()
        val application = Bootstrapper().load()

        application.init(configurer)
    }
}
