package io.polymorphicpanda.faux.core.window

import io.polymorphicpanda.faux.core.config.WindowConfig
import io.polymorphicpanda.faux.core.engine.Engine
import io.polymorphicpanda.faux.core.util.NULL
import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.input.InputManager
import io.polymorphicpanda.faux.input.Key
import io.polymorphicpanda.faux.input.MouseButton
import io.polymorphicpanda.faux.runtime.FauxException
import kotlinx.coroutines.experimental.async
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWCharCallback
import org.lwjgl.glfw.GLFWCursorPosCallback
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWMouseButtonCallback
import org.lwjgl.glfw.GLFWWindowRefreshCallback
import org.lwjgl.glfw.GLFWWindowSizeCallback
import org.lwjgl.opengl.GL

interface Window: InputManager {
    fun handle(): Long
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
    private val eventMapper = GlfwEventMapper(engine::handleInput)
    private lateinit var refreshCallback: GLFWWindowRefreshCallback
    private lateinit var frameBufferResizeCallback: GLFWFramebufferSizeCallback
    private lateinit var resizeCallback: GLFWWindowSizeCallback
    private lateinit var cursorPosCallback: GLFWCursorPosCallback
    private lateinit var mouseButtonCallBack: GLFWMouseButtonCallback
    private lateinit var keyCallback: GLFWKeyCallback
    private lateinit var charCallback: GLFWCharCallback
    private val errorCallback = GLFWErrorCallback.createPrint(System.err)

    override fun handle(): Long {
        return window
    }

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

        frameBufferResizeCallback = GLFWFramebufferSizeCallback.create { _, width, height ->
            engine.handleFrameBufferResize(width, height)
        }

        resizeCallback = GLFWWindowSizeCallback.create { _, width, height -> engine.handleWindowResize(width, height) }

        cursorPosCallback = GLFWCursorPosCallback.create {_, x, y -> eventMapper.mouseMove(x, y) }
        mouseButtonCallBack = GLFWMouseButtonCallback.create {_, button, action, mods ->
            val x = DoubleArray(1)
            val y = DoubleArray(1)

            GLFW.glfwGetCursorPos(window, x, y)

            eventMapper.mouseButton(x[0], y[0], button, action, mods)
        }

        keyCallback = GLFWKeyCallback.create { _, key, _, action, mods ->
            eventMapper.keyPress(key, action, mods)
        }

        charCallback = GLFWCharCallback.create { _, unicode ->
            eventMapper.charTyped(unicode)
        }

        GLFW.glfwSetWindowRefreshCallback(window, refreshCallback)
        GLFW.glfwSetFramebufferSizeCallback(window, frameBufferResizeCallback)
        GLFW.glfwSetCursorPosCallback(window, cursorPosCallback)
        GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallBack)
        GLFW.glfwSetKeyCallback(window, keyCallback)
        GLFW.glfwSetCharCallback(window, charCallback)
        GLFW.glfwMakeContextCurrent(window)

        GL.createCapabilities(true)
    }

    override fun dispose() {
        errorCallback.free()
        refreshCallback.free()
        frameBufferResizeCallback.free()
        resizeCallback.free()
        cursorPosCallback.free()
        mouseButtonCallBack.free()
        keyCallback.free()
        charCallback.free()
        GLFW.glfwTerminate()
    }

    override fun shouldClose() =  GLFW.glfwWindowShouldClose(window)

    override fun flush() {
        GLFW.glfwSwapBuffers(window)
    }

    override fun pollEvents() {
        GLFW.glfwPollEvents()
    }

    override suspend fun getMousePosition(): Pair<Double, Double> {
        return async(engine.getMainThread()) {
            val x = DoubleArray(1)
            val y = DoubleArray(1)

            GLFW.glfwGetCursorPos(window, x, y)

            x[0] to y[0]
        }.await()
    }

    override suspend  fun isMouseButtonPressed(button: MouseButton): Boolean {
        return async(engine.getMainThread()) {
            GLFW.glfwGetMouseButton(window, GlfwMouseButtonMapper.toPlatformType(button)) == GLFW.GLFW_PRESS
        }.await()
    }

    override suspend fun isKeyPressed(key: Key): Boolean {
        return async(engine.getMainThread()) {
            GLFW.glfwGetKey(window, GlfwKeyMapper.toPlatformType(key)) == GLFW.GLFW_PRESS
        }.await()
    }
}
