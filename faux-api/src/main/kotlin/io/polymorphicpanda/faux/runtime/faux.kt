package io.polymorphicpanda.faux.runtime

import io.polymorphicpanda.faux.service.Service
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.KClass

class FauxException(message: String? = null, throwable: Throwable? = null): Throwable(
    message, throwable
)

interface EnginePeer {
    fun <T: Service> getService(service: KClass<T>): T
    fun getSharedPool(): CoroutineContext
    fun getMainThread(): CoroutineContext
}

object Faux {
    lateinit var peer: EnginePeer

    val sharedPool: CoroutineContext
        get() = peer.getSharedPool()

    val mainThread: CoroutineContext
        get() = peer.getMainThread()

    inline fun <reified T: Service> getService(): T = peer.getService(T::class)
}
