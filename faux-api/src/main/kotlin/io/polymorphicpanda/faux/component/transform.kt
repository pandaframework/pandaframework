package io.polymorphicpanda.faux.component

import com.sun.javafx.geom.Vec3f
import io.polymorphicpanda.faux.runtime.FCompoundType
import io.polymorphicpanda.faux.runtime.FCompoundTypePropertyBuilder
import io.polymorphicpanda.faux.runtime.fType

val FVec3 = fType("Vec3f", ::Vec3f)
    .floatProperty(Vec3f::x)
    .floatProperty(Vec3f::y)
    .floatProperty(Vec3f::z)
    .build()

data class Transform(
    var position: Vec3f,
    var rotation: Vec3f,
    var scale: Vec3f
): Component {
    companion object: ComponentDescriptor<Transform>() {
        override fun buildType(builder: FCompoundTypePropertyBuilder<Transform>): FCompoundType<Transform> {
            return builder.property(FVec3, Transform::position)
                .property(FVec3, Transform::rotation)
                .property(FVec3, Transform::scale)
                .build()
        }

        override val id = Transform::class
        override fun create() = Transform(Vec3f(), Vec3f(), Vec3f())

    }
}
