package io.polymorphicpanda.faux.runtime

import io.polymorphicpanda.faux.service.Service

class FauxException(message: String? = null, throwable: Throwable? = null): Throwable(
    message, throwable
)

expect object Faux {
    inline fun <reified T: Service> getService(): T
}
