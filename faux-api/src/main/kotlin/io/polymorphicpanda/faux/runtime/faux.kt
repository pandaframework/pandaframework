package io.polymorphicpanda.faux.runtime

import io.polymorphicpanda.faux.service.Service
import kotlin.reflect.KClass

class FauxException(message: String? = null, throwable: Throwable? = null): Throwable(
    message, throwable
)

interface EnginePeer {
    fun <T: Service> getService(service: KClass<T>): T
}

object Faux {
    lateinit var peer: EnginePeer

    inline fun <reified T: Service> getService(): T = peer.getService(T::class)
}
