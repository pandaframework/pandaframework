package io.polymorphicpanda.faux.component

import io.polymorphicpanda.faux.runtime.Descriptor
import kotlin.reflect.KClass

typealias ComponentType = KClass<out Component>
interface Component {
    fun detached() { }
}

interface ComponentDescriptor<T: Component>: Descriptor<T>
