package io.polymorphicpanda.faux.core

import org.lwjgl.opengl.GL11

interface Backend {
    fun handleWindowResize(width: Int, height: Int)
    fun handleFramebufferResize(width: Int, height: Int)
}

class OpenGlBackend: Backend {
    override fun handleWindowResize(width: Int, height: Int) { }

    override fun handleFramebufferResize(width: Int, height: Int) {
        GL11.glViewport(0, 0, width, height)
    }
}
