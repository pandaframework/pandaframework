package io.polymorphicpanda.faux.core.window

import io.polymorphicpanda.faux.core.engine.Engine
import io.polymorphicpanda.faux.core.config.WindowConfig
import io.polymorphicpanda.faux.core.util.NULL
import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.runtime.FauxException
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWWindowRefreshCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL

interface Window {
    fun init()
    fun dispose()
    fun shouldClose(): Boolean
    fun flush()
    fun pollEvents()
}

interface WindowEventHandler {
    fun handleInput(event: Event)
    fun handleFrameBufferResize(width: Int, height: Int)
    fun handleWindowResize(width: Int, height: Int)
}

object WindowFactory {
    fun create(config: WindowConfig,
               engine: Engine): Window
        = GlfwWindow(config.width, config.height, config.title, engine)
}

class GlfwWindow(private var width: Int,
                 private var height: Int,
                 private var title: String,
                 private val engine: Engine): Window {
    private var window: Long = NULL
    private lateinit var refreshCallback: GLFWWindowRefreshCallback
    private lateinit var framebufferResizeCallback: GLFWFramebufferSizeCallback
    private lateinit var resizeCallback: GLFWWindowSizeCallback
    private val errorCallback = GLFWErrorCallback.createPrint(System.err)

    override fun init() {
        if (!GLFW.glfwInit()) {
            throw FauxException("Failed to initialize GLFW.")
        }

        GLFW.glfwSetErrorCallback(errorCallback)
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL)

        if (window == NULL) {
            throw FauxException("Failed to create GLFW window.")
        }

        refreshCallback = GLFWWindowRefreshCallback.create { _ ->
            flush()
        }

        framebufferResizeCallback = GLFWFramebufferSizeCallback.create { _, width, height ->
            engine.handleFrameBufferResize(width, height)
        }

        resizeCallback = GLFWWindowSizeCallback.create { _, width, height -> engine.handleWindowResize(width, height) }

        GLFW.glfwSetWindowRefreshCallback(window, refreshCallback)
        GLFW.glfwSetFramebufferSizeCallback(window, framebufferResizeCallback)
        GLFW.glfwMakeContextCurrent(window)

        GL.createCapabilities(true)
    }

    override fun dispose() {
        refreshCallback.free()
        framebufferResizeCallback.free()
        resizeCallback.free()
        GLFW.glfwTerminate()
    }

    override fun shouldClose() =  GLFW.glfwWindowShouldClose(window)

    override fun flush() {
        GLFW.glfwSwapBuffers(window)
    }

    override fun pollEvents() {
        GLFW.glfwPollEvents()
    }
}
