package io.polymorphicpanda.faux.runtime


interface SerializationContext {
    fun write(value: String)
    fun childContext(name: String): SerializationContext
}

interface DeserializationContext {
    fun read(): String
    fun childContext(name: String): DeserializationContext
}
