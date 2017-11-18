package io.polymorphicpanda.faux

expect object Faux {
    val stateManager: StateManager
    inline fun <reified T> getService(): T
}

inline fun <reified T> System.service() = lazy { Faux.getService<T>() }
inline fun <reified T> State.service() = lazy { Faux.getService<T>() }

inline val System.stateManager
    get() = Faux.stateManager

inline val State.stateManager
    get() = Faux.stateManager



interface EngineConfig {
    fun <T: Component> registerComponent(descriptor: ComponentDescriptor<T>)
    fun <T: Blueprint> registerBlueprint(descriptor: BlueprintDescriptor<T>)
    fun <T: Any> registerService(service: T)
    fun registerSystem(system: System, dependencies: List<System>)
}

interface Application {
    fun init(config: EngineConfig) { }
    fun start() {}
    fun dispose() { }
}
