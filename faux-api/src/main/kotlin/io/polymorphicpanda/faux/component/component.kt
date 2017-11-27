package io.polymorphicpanda.faux.component

import io.polymorphicpanda.faux.runtime.Descriptor
import io.polymorphicpanda.faux.runtime.FCompoundType
import io.polymorphicpanda.faux.runtime.FCompoundTypePropertyBuilder
import io.polymorphicpanda.faux.runtime.fType
import kotlin.reflect.KClass

typealias ComponentType = KClass<out Component>
interface Component

abstract class ComponentDescriptor<T: Component>: Descriptor<T> {
    val FType: FCompoundType<T> by lazy { buildType(fType(id.qualifiedName!!, this::create)) }
    protected abstract fun buildType(builder: FCompoundTypePropertyBuilder<T>): FCompoundType<T>
}
