package io.polymorphicpanda.faux.core.engine

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
