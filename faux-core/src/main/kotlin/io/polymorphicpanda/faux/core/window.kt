package io.polymorphicpanda.faux.core

expect object WindowFactory {
    fun create(config: WindowConfig): Window
}

interface Window {
    fun init()
}
