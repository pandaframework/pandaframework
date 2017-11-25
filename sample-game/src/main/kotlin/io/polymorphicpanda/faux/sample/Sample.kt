package io.polymorphicpanda.faux.sample

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.application.EngineConfig

class Sample: Application {
    override fun init(config: EngineConfig) {
        super.init(config)
        config.setWindowTitle("Sample Application")
    }
}
