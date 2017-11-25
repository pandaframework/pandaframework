package io.polymorphicpanda.faux.core

import org.lwjgl.glfw.GLFW

actual class Clock {
    actual fun getTime(): Double {
        return GLFW.glfwGetTime()
    }
}
