package io.polymorphicpanda.faux.core.service

import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.service.ServiceDescriptor
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class ServiceManager {
    private val services = ConcurrentHashMap<KClass<out Service>, Service>()

    fun <T: Service> registerService(descriptor: ServiceDescriptor<T>) {
        services.put(descriptor.id, descriptor.create())
    }

    fun <T: Service> getService(service: KClass<T>): T {
        return services.getValue(service) as T
    }
}
