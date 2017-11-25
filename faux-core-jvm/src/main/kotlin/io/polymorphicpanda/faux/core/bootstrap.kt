package io.polymorphicpanda.faux.core

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.bootstrap.Bootstrap
import io.polymorphicpanda.faux.runtime.FauxException
import java.util.ServiceLoader

actual class Bootstrapper {
    private val serviceLoader = ServiceLoader.load(Bootstrap::class.java)

    actual fun load(): Application {
        val result = serviceLoader.findFirst()

        return result.orElseThrow { FauxException("Bootstrapper not found!") }
            .getApplication()
    }
}
