package io.polymorphicpanda.faux.core.config

import io.polymorphicpanda.faux.application.EngineConfig
import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.blueprint.BlueprintDescriptor
import io.polymorphicpanda.faux.component.Component
import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.service.ServiceDescriptor
import io.polymorphicpanda.faux.state.StateDescriptor
import io.polymorphicpanda.faux.system.SystemDescriptor

private const val DEFAULT_WIDTH = 800
private const val DEFAULT_HEIGHT = 600
private const val DEFAULT_TITLE = "Faux Engine"

data class WindowConfig(
    var width: Int,
    var height: Int,
    var title: String
)

class EngineSettings: EngineConfig {
    val windowConfig = WindowConfig(
        DEFAULT_WIDTH,
        DEFAULT_HEIGHT,
        DEFAULT_TITLE
    )

    private var developmentMode: Boolean = false

    private val systems = mutableMapOf<SystemDescriptor<*>, List<SystemDescriptor<*>>>()
    private val components = mutableMapOf<ComponentType, ComponentDescriptor<*>>()
    private val services = mutableListOf<ServiceDescriptor<out Service>>()

    override fun setDevelopmentMode(active: Boolean) {
        developmentMode = active
    }

    override fun setWindowSize(width: Int, height: Int) {
        require(width > 0 && height > 0)
        windowConfig.width = width
        windowConfig.height = height
    }

    override fun setWindowTitle(title: String) {
        windowConfig.title = title
    }

    override fun <T: Component> registerComponent(descriptor: ComponentDescriptor<T>) {
        components.put(descriptor.id, descriptor)
    }

    override fun <T: Blueprint> registerBlueprint(descriptor: BlueprintDescriptor<T>) {
        TODO()
    }

    override fun <T: Service> registerService(descriptor: ServiceDescriptor<T>) {
        services.add(descriptor)
    }

    override fun registerSystem(descriptor: SystemDescriptor<*>, dependencies: List<SystemDescriptor<*>>) {
        systems.put(descriptor, dependencies)
    }

    override fun setInitialState(state: StateDescriptor<*>) {
        TODO()
    }

    fun getServices(): List<ServiceDescriptor<out Service>> = services
    fun getSystems(): Map<SystemDescriptor<*>, List<SystemDescriptor<*>>> = systems
    fun getComponents(): Map<ComponentType, ComponentDescriptor<*>> = components
    fun isDevelopmentMode() = developmentMode
}
