package io.polymorphicpanda.faux.core

actual object WindowFactory {
    actual fun create(config: WindowConfig): Window {
        TODO()
    }
}

class GlfwWindow(private var width: Int,
                 private var height: Int,
                 private var title: String): Window {
    override fun init() {
        TODO()
    }
}
