package io.polymorphicpanda.faux.system

import io.polymorphicpanda.faux.entity.Context
import io.polymorphicpanda.faux.runtime.Descriptor
import io.polymorphicpanda.faux.runtime.Faux
import io.polymorphicpanda.faux.service.Service
import io.polymorphicpanda.faux.state.StateManager

class Aspect
interface SystemContext: Context

abstract class System {
    inline fun <reified T: Service> service() = lazy { Faux.getService<T>() }
    val stateManager by service<StateManager>()

    abstract val aspect: Aspect
    abstract fun process(duration: Double, context: SystemContext)
}

interface SystemDescriptor<T: System>: Descriptor<T>
