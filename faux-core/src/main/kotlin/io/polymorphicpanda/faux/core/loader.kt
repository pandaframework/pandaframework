package io.polymorphicpanda.faux.core

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.runtime.FauxException
import java.util.ServiceLoader

class ApplicationLoader {
    private val serviceLoader = ServiceLoader.load(Application::class.java)

    fun load(): Application {
        return serviceLoader.findFirst()
            .orElseThrow { FauxException("Application not found!") }
    }
}
