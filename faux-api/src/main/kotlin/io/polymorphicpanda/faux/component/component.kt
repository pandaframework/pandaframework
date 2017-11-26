package io.polymorphicpanda.faux.component

import io.polymorphicpanda.faux.runtime.Descriptor
import io.polymorphicpanda.faux.runtime.SerializationContext
import kotlin.reflect.KClass

typealias ComponentType = KClass<out Component>
interface Component

abstract class ComponentDescriptor<T: Component>: Descriptor<T> {
    abstract val type: FCompoundType<T>
    protected fun fType() = fType(id.qualifiedName!!, this::create)
}

data class Transform(var t: Vec3 = Vec3()): Component {
    companion object: ComponentDescriptor<Transform>() {
        override val id = Transform::class
        override fun create() = Transform()

        override val type = fType()
            .property("t", Vec3.type, Transform::t)
            .build()
    }
}

fun main(args: Array<String>) {
    val vector = Vec3()
    vector.x = 1.0
    vector.y = -1.0
    vector.z = 10.2
    Transform.type.serialize(object: SerializationContext {
        override fun write(value: String) {
            println("writing $value")
            // TODO()
        }

        override fun childContext(name: String): SerializationContext {
            println("childContext for $name")
            // TODO()
            return this
        }
    }, Transform(vector))

}
