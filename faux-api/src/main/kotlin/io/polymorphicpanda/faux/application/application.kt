package io.polymorphicpanda.faux.application

import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.blueprint.BlueprintDescriptor
import io.polymorphicpanda.faux.component.Component
import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.service.ServiceDescriptor
import io.polymorphicpanda.faux.state.StateDescriptor
import io.polymorphicpanda.faux.system.SystemDescriptor
import io.polymorphicpanda.faux.util.Some

interface EngineConfig {
    fun <T: Component> registerComponent(descriptor: ComponentDescriptor<T>)
    fun <T: Blueprint> registerBlueprint(descriptor: BlueprintDescriptor<T>)
    fun <T: Service> registerService(descriptor: ServiceDescriptor<T>)
    fun registerSystem(descriptor: SystemDescriptor<*>, dependencies: List<SystemDescriptor<*>>)
}

interface Application {
    val initialState: Some<StateDescriptor<*>>
    fun init(config: EngineConfig) { }
    fun dispose() { }
}
