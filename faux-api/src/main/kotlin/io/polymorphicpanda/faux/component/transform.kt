package io.polymorphicpanda.faux.component

import io.polymorphicpanda.faux.math.Vec3f
import io.polymorphicpanda.faux.math.vec3fProperty
import io.polymorphicpanda.faux.runtime.FCompoundType
import io.polymorphicpanda.faux.runtime.FCompoundTypePropertyBuilder

data class Transform(
    var position: Vec3f,
    var rotation: Vec3f,
    var scale: Vec3f
): Component {
    companion object Descriptor: ComponentDescriptor<Transform>() {
        override fun buildType(builder: FCompoundTypePropertyBuilder<Transform>): FCompoundType<Transform> {
            return builder.vec3fProperty(Transform::position)
                .vec3fProperty(Transform::rotation)
                .vec3fProperty(Transform::scale)
                .build()
        }

        override val id = Transform::class
        override fun create() = Transform(Vec3f(), Vec3f(), Vec3f())
    }
}
