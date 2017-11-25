package io.polymorphicpanda.faux.core

import org.lwjgl.glfw.GLFW

interface Clock {
    fun getTime(): Double
}

class GlfwClock: Clock {
    override fun getTime() = GLFW.glfwGetTime()
}
