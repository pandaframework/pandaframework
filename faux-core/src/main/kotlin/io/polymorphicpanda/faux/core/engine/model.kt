package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.component.ComponentDescriptor
import io.polymorphicpanda.faux.component.ComponentType
import io.polymorphicpanda.faux.core.backend.gfx.GfxBackend
import io.polymorphicpanda.faux.core.backend.gfx.gl.OpenGlGfxBackend
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.system.SystemDescriptor
import kotlinx.coroutines.experimental.CommonPool
import kotlin.coroutines.experimental.CoroutineContext

data class EngineExecutionModel(
    val components: Map<ComponentType, ComponentDescriptor<*>>,
    val systems: Map<SystemDescriptor<*>, List<SystemDescriptor<*>>>,
    val mainThread: CoroutineContext
) {

    val componentMappings: Map<ComponentType, ComponentId> = ComponentMapper().let { mapper ->
        components.keys.associate { it to mapper.map(it) }
    }

    val graphics: GfxBackend = OpenGlGfxBackend()

    val systemExecutor = SystemExecutor()

    val sharedPool = CommonPool

    companion object {
        fun from(settings: EngineSettings, mainThread: CoroutineContext) = EngineExecutionModel(
            settings.getComponents(),
            settings.getSystems(),
            mainThread
        )
    }
}
