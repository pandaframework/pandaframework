package io.polymorphicpanda.faux.event

import io.polymorphicpanda.faux.input.InputAction
import io.polymorphicpanda.faux.input.MouseButton

interface Event

sealed class MouseEvent(val x: Double, val y: Double): Event
class MouseMoveEvent(x: Double, y: Double): MouseEvent(x, y)
class MouseButtonEvent(x: Double, y: Double, val button: MouseButton, val action: InputAction): MouseEvent(x, y)
