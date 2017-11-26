package io.polymorphicpanda.faux.system

import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.entity.Context
import io.polymorphicpanda.faux.entity.Entity
import io.polymorphicpanda.faux.runtime.Descriptor
import io.polymorphicpanda.faux.runtime.Faux
import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.state.StateManager
import kotlinx.coroutines.experimental.CoroutineScope

data class Aspect internal constructor(val included: List<ComponentType>,
                                       val excluded: List<ComponentType>) {
    fun with(vararg componentTypes: ComponentType) = copy(included = included + componentTypes)
    fun without(vararg componentTypes: ComponentType) = copy(excluded = excluded + componentTypes)
}

private val initial = Aspect(emptyList(), emptyList())
fun aspects() = initial

interface SystemContext: Context {
    fun entities(): Set<Entity>
}

abstract class System {
    inline fun <reified T: Service> service() = lazy { Faux.getService<T>() }
    val stateManager by service<StateManager>()

    abstract suspend fun CoroutineScope.process(duration: Double, context: SystemContext)
}

interface SystemDescriptor<T: System>: Descriptor<T> {
    abstract val aspect: Aspect
}
