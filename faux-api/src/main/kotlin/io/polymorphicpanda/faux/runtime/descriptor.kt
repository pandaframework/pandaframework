package io.polymorphicpanda.faux.runtime

import kotlin.reflect.KClass

interface Descriptor<T: Any> {
    val id: KClass<T>
    fun create(): T
}
