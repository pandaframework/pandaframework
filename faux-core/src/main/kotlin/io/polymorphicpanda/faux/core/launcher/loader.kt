package io.polymorphicpanda.faux.core.launcher

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.runtime.FauxException
import mu.KotlinLogging
import java.util.ServiceLoader

class ApplicationLoader {
    private val logger = KotlinLogging.logger {}
    private val serviceLoader = ServiceLoader.load(Application::class.java)

    fun load(): Application {
        return serviceLoader.findFirst()
            .orElseThrow { FauxException("Application not found!") }
    }
}
