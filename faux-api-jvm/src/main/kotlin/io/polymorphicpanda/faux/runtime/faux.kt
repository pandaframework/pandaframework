package io.polymorphicpanda.faux.runtime

import io.polymorphicpanda.faux.bootstrap.EnginePeer
import io.polymorphicpanda.faux.service.Service

actual object Faux {
    lateinit var peer: EnginePeer

    actual inline fun <reified T: Service> getService(): T = peer.getService(T::class)
}
