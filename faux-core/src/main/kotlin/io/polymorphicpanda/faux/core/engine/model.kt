package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.system.SystemDescriptor

data class EngineModel(
    val components: Map<ComponentType, ComponentDescriptor<*>>,
    val systems: Map<SystemDescriptor<*>, List<SystemDescriptor<*>>>
) {

    val componentMappings: Map<ComponentType, ComponentId> = ComponentMapper().let { mapper ->
        components.keys.associate { it to mapper.map(it) }
    }

    val systemGraph = DynamicGraph<SystemDescriptor<*>>().apply {
        systems.forEach { system, dependencies ->
            if (dependencies.isEmpty()) {
                addNode(system)
            } else {
                dependencies.forEach {
                    addEdge(system, it)
                }
            }
        }
    }

    companion object {
        fun from(settings: EngineSettings) = EngineModel(
            settings.getComponents(),
            settings.getSystems()
        )
    }
}
