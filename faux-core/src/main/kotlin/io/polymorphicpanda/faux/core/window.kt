package io.polymorphicpanda.faux.core

expect object WindowFactory {
    fun create(config: WindowConfig,
               engine: Engine): Window
}

interface Window {
    fun init()
    fun dispose()
    fun shouldClose(): Boolean

    fun flush()
    fun pollEvents()
}
