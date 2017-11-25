package io.polymorphicpanda.faux.bootstrap

import io.polymorphicpanda.faux.application.Application
import io.polymorphicpanda.faux.service.Service
import kotlin.reflect.KClass

interface EnginePeer {
    fun <T: Service> getService(service: KClass<T>): T
}

interface Bootstrap {
    fun getApplication(): Application
}
