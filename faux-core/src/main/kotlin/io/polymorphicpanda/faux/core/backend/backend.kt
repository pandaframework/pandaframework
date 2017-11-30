package io.polymorphicpanda.faux.core.backend

import org.lwjgl.opengl.GL11

interface Backend {
    fun handleWindowResize(width: Int, height: Int)
    fun handleFrameBufferResize(width: Int, height: Int)
}

class OpenGlBackend: Backend {
    override fun handleWindowResize(width: Int, height: Int) { }

    override fun handleFrameBufferResize(width: Int, height: Int) {
        GL11.glViewport(0, 0, width, height)
    }
}
