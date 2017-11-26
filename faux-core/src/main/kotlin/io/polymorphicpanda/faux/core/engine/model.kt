package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.system.System
import io.polymorphicpanda.faux.system.SystemDescriptor

data class EngineModel(
    val components: Map<ComponentType, ComponentDescriptor<*>>,
    val systems: Map<SystemDescriptor<*>, List<SystemDescriptor<*>>>
) {

    val componentMappings: Map<ComponentType, ComponentId> = ComponentMapper().let { mapper ->
        components.keys.associate { it to mapper.map(it) }
    }

    private val systemInstanceMap = mutableMapOf<SystemDescriptor<*>, System>()

    val systemGraph = DynamicGraph<System>().apply {
        systems.forEach { descriptor, dependencies ->
            if (dependencies.isEmpty()) {
                addNode(systemInstanceFor(descriptor))
            } else {
                dependencies.forEach {
                    addEdge(systemInstanceFor(descriptor), systemInstanceFor(it))
                }
            }
        }
    }

    val systemInstances: Map<SystemDescriptor<*>, System> = systemInstanceMap

    private fun systemInstanceFor(descriptor: SystemDescriptor<*>): System {
        return systemInstanceMap.computeIfAbsent(descriptor) {
            descriptor.create()
        }
    }

    companion object {
        fun from(settings: EngineSettings) = EngineModel(
            settings.getComponents(),
            settings.getSystems()
        )
    }
}
