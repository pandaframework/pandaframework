package io.polymorphicpanda.faux.sample

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.application.EngineConfig
import io.polymorphicpanda.faux.system.System
import io.polymorphicpanda.faux.system.SystemContext
import io.polymorphicpanda.faux.system.SystemDescriptor
import io.polymorphicpanda.faux.system.aspects


class SampleSystem: System() {
    suspend override fun process(duration: Double, context: SystemContext) {
    }

    companion object Descriptor: SystemDescriptor<SampleSystem> {
        override val id = SampleSystem::class
        override fun create() = SampleSystem()
        override val aspect = aspects()

    }
}

class Sample: Application {
    override fun init(config: EngineConfig) {
        config.setDevelopmentMode(true)
        config.setWindowTitle("Sample Application")
        config.registerSystem(SampleSystem.Descriptor)
    }
}
