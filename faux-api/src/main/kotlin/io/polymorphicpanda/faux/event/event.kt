package io.polymorphicpanda.faux.event

import io.polymorphicpanda.faux.input.InputAction
import io.polymorphicpanda.faux.input.Key
import io.polymorphicpanda.faux.input.MouseButton

interface Event

sealed class MouseEvent(val x: Double, val y: Double): Event
class MouseMoveEvent(x: Double, y: Double): MouseEvent(x, y) {
    override fun toString(): String {
        return "MouseMoveEvent(x=$x, y=$y)"
    }
}
class MouseButtonEvent(x: Double, y: Double, val button: MouseButton, val action: InputAction, val modifiers: Int): MouseEvent(x, y) {
    override fun toString(): String {
        return "MouseButtonEvent(x=$x, y=$y, button=$button, action=$action, modifiers=$modifiers)"
    }
}

class KeyEvent(val key: Key, val action: InputAction, val modifiers: Int): Event {
    override fun toString(): String {
        return "KeyEvent(key=$key, action=$action, modifiers=$modifiers)"
    }
}
