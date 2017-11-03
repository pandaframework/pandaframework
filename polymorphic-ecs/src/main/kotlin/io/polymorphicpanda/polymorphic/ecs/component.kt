package io.polymorphicpanda.polymorphic.ecs

import kotlin.reflect.KClass

interface Component {
    fun detached() { }
}

typealias ComponentType = KClass<out Component>

internal typealias ComponentId = Int

internal class ComponentMapper {
    private var nextId = 0
    private val mappings = mutableMapOf<ComponentType, ComponentId>()
    fun map(componentType: ComponentType): ComponentId {
        return mappings.computeIfAbsent(componentType) {
            nextId++
        }
    }
}
