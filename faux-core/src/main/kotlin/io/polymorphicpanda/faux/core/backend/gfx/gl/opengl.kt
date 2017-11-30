package io.polymorphicpanda.faux.core.backend.gfx.gl

import io.polymorphicpanda.faux.core.backend.gfx.GfxBackend
import io.polymorphicpanda.faux.core.backend.gfx.GfxRenderSystem
import io.polymorphicpanda.faux.core.backend.gfx.GfxRenderer
import io.polymorphicpanda.faux.system.SystemContext
import org.lwjgl.opengl.GL11

class OpenGlRenderSystem: GfxRenderSystem() {
    suspend override fun process(duration: Double, context: SystemContext) {
        println("rendering!!")
    }

    companion object Descriptor: GfxRenderer<OpenGlRenderSystem>() {
        override val id = OpenGlRenderSystem::class
        override fun create() = OpenGlRenderSystem()
    }
}


class OpenGlGfxBackend: GfxBackend {
    override fun handleWindowResize(width: Int, height: Int) { }

    override fun handleFrameBufferResize(width: Int, height: Int) {
        GL11.glViewport(0, 0, width, height)
    }

    override fun getRenderer() = OpenGlRenderSystem.Descriptor
}
