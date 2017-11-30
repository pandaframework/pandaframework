package io.polymorphicpanda.faux.core.backend.gfx

import io.polymorphicpanda.faux.system.System
import io.polymorphicpanda.faux.system.SystemDescriptor
import io.polymorphicpanda.faux.system.aspects

interface GfxBackend {
    fun handleWindowResize(width: Int, height: Int)
    fun handleFrameBufferResize(width: Int, height: Int)

    fun getRenderer(): GfxRenderer<out GfxRenderSystem>
}

abstract class GfxRenderSystem: System()
abstract class GfxRenderer<T: GfxRenderSystem>: SystemDescriptor<T> {
    override val aspect = aspects()
}
