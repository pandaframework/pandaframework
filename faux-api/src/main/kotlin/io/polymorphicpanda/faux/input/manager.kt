package io.polymorphicpanda.faux.input

import io.polymorphicpanda.faux.service.Service

interface InputManager: Service {
    /**
     * Return the last saved mouse position.
     *
     * @note Should only be called in the main thread.
     */
    fun getMousePosition(): Pair<Double, Double>

    /**
     * Check whether the specified button is pressed or not.
     *
     * @note Should only be called in the main thread.
     */
    fun isMouseButtonPressed(button: MouseButton): Boolean

    /**
     * Check whether the specified key is pressed or not.
     *
     * @note Should only be called in the main thread.
     */
    fun isKeyPressed(key: Key): Boolean
}
