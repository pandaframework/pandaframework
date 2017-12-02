package io.polymorphicpanda.faux.core.util


open class PlatformTypeMapper<T, K>(private val mappings: Map<T, K>) {
    private val reversedMappings: Map<K, T> = mappings.entries
        .map { it.value to it.key }
        .toMap()

    fun fromPlatformType(type: T): K? {
        return mappings[type]
    }

    fun toPlatformType(type: K): T {
        return reversedMappings.getValue(type)
    }
}
