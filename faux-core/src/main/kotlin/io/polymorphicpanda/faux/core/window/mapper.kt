package io.polymorphicpanda.faux.core.window

import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.event.MouseButtonEvent
import io.polymorphicpanda.faux.event.MouseMoveEvent
import io.polymorphicpanda.faux.input.InputAction
import io.polymorphicpanda.faux.input.MouseButton
import mu.KotlinLogging
import org.lwjgl.glfw.GLFW


class GlfwEventMapper(private val processor: (Event) -> Unit) {
    private val logger = KotlinLogging.logger {}

    fun mouseMove(x: Double, y: Double) {
        processor(MouseMoveEvent(x, y))
    }

    fun mouseButton(x: Double, y: Double, button: Int, action: Int, mods: Int) {
        val platformButton = toMouseButton(button)
        val platformAction = toInputAction(action)
        if (platformButton != null && platformAction != null) {
            processor(MouseButtonEvent(x, y, platformButton, platformAction))
        } else {
            logger.warn {
                "Unsupported mouse action: (button=$button, action=$action, mods=$mods)"
            }
        }
    }
}

fun toMouseButton(button: Int): MouseButton? {
    return when (button) {
        GLFW.GLFW_MOUSE_BUTTON_LEFT -> MouseButton.LEFT
        GLFW.GLFW_MOUSE_BUTTON_RIGHT -> MouseButton.RIGHT
        GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> MouseButton.MIDDLE
        else -> null
    }
}

fun toInputAction(action: Int): InputAction? {
    return when (action) {
        GLFW.GLFW_PRESS -> InputAction.PRESS
        GLFW.GLFW_RELEASE -> InputAction.RELEASE
        else -> null
    }
}

fun fromMouseButton(button: MouseButton): Int {
    return when (button) {
        MouseButton.LEFT -> GLFW.GLFW_MOUSE_BUTTON_LEFT
        MouseButton.RIGHT -> GLFW.GLFW_MOUSE_BUTTON_RIGHT
        MouseButton.MIDDLE -> GLFW.GLFW_MOUSE_BUTTON_MIDDLE
    }
}
