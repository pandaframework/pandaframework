package io.polymorphicpanda.faux.core

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.bootstrap.Bootstrap
import io.polymorphicpanda.faux.util.Try
import io.polymorphicpanda.faux.util.failure
import io.polymorphicpanda.faux.util.success
import java.util.ServiceLoader

class FauxException(message: String? = null, throwable: Throwable? = null): RuntimeException(
    message, throwable
)

class Bootstrapper {
    private val serviceLoader = ServiceLoader.load(Bootstrap::class.java)

    fun load(): Try<out Application> {
        val result = serviceLoader.findFirst()

        return if (result.isPresent) {
            success(result.get().getApplication())
        } else {
            failure(FauxException("Bootstrapper not found!"))
        }
    }
}

class Launcher {
    fun run(args: Array<String>) {
        val application = Bootstrapper().load().get()
    }
}
