package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.component.Component
import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.component.ComponentType

typealias ComponentId = Int

class ComponentMapper {
    private var nextId = 0
    private val mappings = mutableMapOf<ComponentType, ComponentId>()
    fun map(componentType: ComponentType): ComponentId {
        return mappings.computeIfAbsent(componentType) {
            nextId++
        }
    }
}

abstract class ComponentPool<T: Component>(private val descriptor: ComponentDescriptor<T>) {
    abstract fun acquire(): T
    abstract fun release(instance: T)

    protected fun createInstance(): T = descriptor.create()
}

class BasicComponentPool<T: Component>(descriptor: ComponentDescriptor<T>): ComponentPool<T>(descriptor) {
    override fun acquire(): T {
        return createInstance()
    }

    override fun release(instance: T) {
        // nada
    }
}

class ComponentPools(private val descriptors: Map<ComponentType, ComponentDescriptor<*>>) {
    private val pools = mutableMapOf<ComponentDescriptor<*>, ComponentPool<*>>()

    fun poolFor(componentType: ComponentType): ComponentPool<*> {
        val descriptor = descriptors.getValue(componentType)
        return pools.computeIfAbsent(descriptor) {
            BasicComponentPool(descriptor)
        }
    }
}
