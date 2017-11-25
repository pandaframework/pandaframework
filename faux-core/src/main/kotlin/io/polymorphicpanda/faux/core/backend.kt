package io.polymorphicpanda.faux.core

interface Backend {
    fun handleWindowResize(width: Int, height: Int)
    fun handleFramebufferResize(width: Int, height: Int)
}

expect class OpenGlBackend(): Backend
