package io.polymorphicpanda.faux.math

import io.polymorphicpanda.faux.runtime.FCompoundTypePropertyBuilder
import io.polymorphicpanda.faux.runtime.fType
import org.joml.Vector3f
import org.joml.Vector4f
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import kotlin.reflect.KMutableProperty1

class Vec3f private constructor(private val internal: Vector3f) {
    constructor(): this(Vector3f())

    var x: Float
        get() = internal.x
        set(value) {
            internal.x = value
        }

    var y: Float
        get() = internal.y
        set(value) {
            internal.y = value
        }

    var z: Float
        get() = internal.z
        set(value) {
            internal.z = value
        }

    operator fun plus(other: Vec3f): Vec3f {
        val result = Vector3f()
        internal.add(other.internal, result)
        return Vec3f(result)
    }

    operator fun minus(other: Vec3f): Vec3f {
        val result = Vector3f()
        internal.sub(other.internal, result)
        return Vec3f(result)
    }

    operator fun times(scalar: Float): Vec3f {
        val result = Vector3f()
        internal.mul(scalar, result)
        return Vec3f(result)
    }

    operator fun div(scalar: Float): Vec3f {
        val result = Vector3f()
        internal.div(scalar, result)
        return Vec3f(result)
    }

    operator fun unaryMinus(): Vec3f {
        val result = Vector3f()
        internal.negate(result)
        return Vec3f(result)
    }

    operator fun plusAssign(other: Vec3f) {
        internal.add(other.internal)
    }

    operator fun minusAssign(other: Vec3f) {
        internal.sub(other.internal)
    }

    operator fun timesAssign(other: Vec3f) {
        internal.mul(other.internal)
    }

    operator fun divAssign(other: Vec3f) {
        internal.div(other.internal)
    }

    fun length() = internal.length()
    fun lengthSquared() = internal.lengthSquared()

    /**
     * Normalize this vector.
     * @see [normalized]
     */
    fun normalize() {
        internal.normalize()
    }

    /**
     * Return a normalized version of this vector, without
     * mutating this.
     * @see [normalize]
     */
    fun normalized(): Vec3f {
        val result = Vector3f()
        internal.normalize(result)
        return Vec3f(result)
    }

    fun store(buffer: ByteBuffer, index: Int = 0) {
        internal.get(index, buffer)
    }

    fun store(buffer: FloatBuffer, index: Int = 0) {
        internal.get(index, buffer)
    }

    companion object {
        fun from(buffer: FloatBuffer, index: Int = 0): Vec3f {
            val dest = Vector3f()
            dest.set(index, buffer)
            return Vec3f(dest)
        }

        fun from(buffer: ByteBuffer, index: Int = 0): Vec3f {
            val dest = Vector3f()
            dest.set(index, buffer)
            return Vec3f(dest)
        }

        val FType = fType("Vec3f", ::Vec3f)
            .floatProperty(Vec3f::x)
            .floatProperty(Vec3f::y)
            .floatProperty(Vec3f::z)
            .build()
    }
}

class Vec4f private constructor(private val internal: Vector4f) {
    constructor(): this(Vector4f())

    var x: Float
        get() = internal.x
        set(value) {
            internal.x = value
        }

    var y: Float
        get() = internal.y
        set(value) {
            internal.y = value
        }

    var z: Float
        get() = internal.z
        set(value) {
            internal.z = value
        }

    var w: Float
        get() = internal.w
        set(value) {
            internal.w = value
        }

    operator fun plus(other: Vec4f): Vec4f {
        val result = Vector4f()
        internal.add(other.internal, result)
        return Vec4f(result)
    }

    operator fun minus(other: Vec4f): Vec4f {
        val result = Vector4f()
        internal.sub(other.internal, result)
        return Vec4f(result)
    }

    operator fun times(scalar: Float): Vec4f {
        val result = Vector4f()
        internal.mul(scalar, result)
        return Vec4f(result)
    }

    operator fun div(scalar: Float): Vec4f {
        val result = Vector4f()
        internal.div(scalar, result)
        return Vec4f(result)
    }

    operator fun unaryMinus(): Vec4f {
        val result = Vector4f()
        internal.negate(result)
        return Vec4f(result)
    }

    operator fun plusAssign(other: Vec4f) {
        internal.add(other.internal)
    }

    operator fun minusAssign(other: Vec4f) {
        internal.sub(other.internal)
    }

    operator fun timesAssign(other: Vec4f) {
        internal.mul(other.internal)
    }

    operator fun divAssign(other: Vec4f) {
        internal.div(other.internal)
    }

    fun length() = internal.length()
    fun lengthSquared() = internal.lengthSquared()

    /**
     * Normalize this vector.
     * @see [normalized]
     */
    fun normalize() {
        internal.normalize()
    }

    /**
     * Return a normalized version of this vector, without
     * mutating this.
     * @see [normalize]
     */
    fun normalized(): Vec4f {
        val result = Vector4f()
        internal.normalize(result)
        return Vec4f(result)
    }

    fun store(buffer: ByteBuffer, index: Int = 0) {
        internal.get(index, buffer)
    }

    fun store(buffer: FloatBuffer, index: Int = 0) {
        internal.get(index, buffer)
    }

    companion object {
        fun from(buffer: FloatBuffer, index: Int = 0): Vec4f {
            val dest = Vector4f()
            dest.set(index, buffer)
            return Vec4f(dest)
        }

        fun from(buffer: ByteBuffer, index: Int = 0): Vec4f {
            val dest = Vector4f()
            dest.set(index, buffer)
            return Vec4f(dest)
        }

        val FType = fType("Vec4f", ::Vec4f)
            .floatProperty(Vec4f::x)
            .floatProperty(Vec4f::y)
            .floatProperty(Vec4f::z)
            .floatProperty(Vec4f::w)
            .build()
    }
}

fun <T: Any> FCompoundTypePropertyBuilder<T>.vec3fProperty(handler: KMutableProperty1<T, Vec3f>)
    = property(Vec3f.FType, handler)

fun <T: Any> FCompoundTypePropertyBuilder<T>.vec4fProperty(handler: KMutableProperty1<T, Vec4f>)
    = property(Vec4f.FType, handler)
