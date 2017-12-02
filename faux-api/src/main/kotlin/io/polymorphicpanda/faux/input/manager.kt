package io.polymorphicpanda.faux.input

import io.polymorphicpanda.faux.service.Service

interface InputManager: Service {
    /**
     * Return the last saved mouse position.
     *
     */
    suspend fun getMousePosition(): Pair<Double, Double>

    /**
     * Check whether the specified button is pressed or not.
     *
     * @note Should only be called in the main thread.
     */
    suspend fun isMouseButtonPressed(button: MouseButton): Boolean

    /**
     * Check whether the specified key is pressed or not.
     *
     * @note Should only be called in the main thread.
     */
    suspend fun isKeyPressed(key: Key): Boolean
}
