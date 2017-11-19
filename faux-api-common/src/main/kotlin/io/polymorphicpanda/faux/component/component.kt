package io.polymorphicpanda.faux.component

import io.polymorphicpanda.faux.runtime.Descriptor

interface Component {
    fun detached()
}

interface ComponentDescriptor<T: Component>: Descriptor<T>
