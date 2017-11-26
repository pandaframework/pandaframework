package io.polymorphicpanda.faux.runtime

import kotlin.reflect.KMutableProperty1


abstract class FType<T: Any>{
    abstract fun serialize(context: SerializationContext, value: T)
    abstract fun deserialize(context: DeserializationContext): T
}

sealed class FPrimitiveType<T: Any>: FType<T>() {
    override fun serialize(context: SerializationContext, value: T) {
        context.write(toString(value))
    }

    override fun deserialize(context: DeserializationContext): T {
        return fromString(context.read())
    }

    protected abstract fun toString(value: T): String
    protected abstract fun fromString(value: String): T
}

object FInt: FPrimitiveType<Int>() {
    override fun toString(value: Int) = value.toString()
    override fun fromString(value: String) = value.toInt()
    override fun toString() = "Int"
}

object FFloat: FPrimitiveType<Float>() {
    override fun toString(value: Float) = value.toString()
    override fun fromString(value: String) = value.toFloat()
    override fun toString() = "Float"
}

object FDouble: FPrimitiveType<Double>() {
    override fun toString(value: Double) = value.toString()
    override fun fromString(value: String) = value.toDouble()
    override fun toString() = "Double"
}

object FBoolean: FPrimitiveType<Boolean>() {
    override fun toString(value: Boolean) = value.toString()
    override fun fromString(value: String) = value.toBoolean()
    override fun toString() = "Boolean"
}

object FString: FPrimitiveType<String>() {
    override fun toString(value: String) = value
    override fun fromString(value: String) = value
    override fun toString() = "String"
}

class FCompoundType<T: Any>(val name: String,
                            val properties: Map<Pair<String, FType<Any>>, KMutableProperty1<Any, Any>>,
                            private val factory: () -> T)
    : FType<T>() {
    override fun serialize(context: SerializationContext, value: T) {
        properties.forEach {
            val name = it.key.first
            val type = it.key.second
            type.serialize(context.childContext(name), it.value.get(value))
        }
    }

    override fun deserialize(context: DeserializationContext): T {
        val instance = factory()
        properties.forEach {
            val name = it.key.first
            val type = it.key.second
            val deserialized = type.deserialize(context.childContext(name))
            it.value.set(instance, deserialized)
        }
        return instance
    }

    override fun toString() = name
}


class FCompoundTypePropertyBuilder<T: Any>(private val name: String,
                                           private val factory: () -> T) {
    private val properties = mutableMapOf<Pair<String, FType<Any>>, KMutableProperty1<Any, Any>>()

    fun <K: Any> property(name: String,
                          type: FType<K>,
                          handler: KMutableProperty1<T, K>): FCompoundTypePropertyBuilder<T> {
        properties.put(name to type as FType<Any>, handler as KMutableProperty1<Any, Any>)
        return this
    }

    fun build(): FCompoundType<T> {
        return FCompoundType(name, properties, factory)
    }
}

fun <T: Any> fType(name: String, factory: () -> T) = FCompoundTypePropertyBuilder(name, factory)

class Vec3 {
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    companion object {
        val type = fType("Vec3", ::Vec3)
            .property("x", FDouble, Vec3::x)
            .property("y", FDouble, Vec3::y)
            .property("z", FDouble, Vec3::z)
            .build()
    }
}
