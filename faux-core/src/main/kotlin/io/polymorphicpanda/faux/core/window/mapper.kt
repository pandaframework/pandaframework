package io.polymorphicpanda.faux.core.window

import io.polymorphicpanda.faux.core.text.CharEvent
import io.polymorphicpanda.faux.core.util.PlatformTypeMapper
import io.polymorphicpanda.faux.event.Event
import io.polymorphicpanda.faux.event.KeyEvent
import io.polymorphicpanda.faux.event.MouseButtonEvent
import io.polymorphicpanda.faux.event.MouseMoveEvent
import io.polymorphicpanda.faux.input.InputAction
import io.polymorphicpanda.faux.input.Key
import io.polymorphicpanda.faux.input.Modifier
import io.polymorphicpanda.faux.input.MouseButton
import mu.KotlinLogging
import org.lwjgl.glfw.GLFW


class GlfwEventMapper(private val processor: (Event) -> Unit) {
    private val logger = KotlinLogging.logger {}

    fun mouseMove(x: Double, y: Double) {
        processor(MouseMoveEvent(x, y))
    }

    fun mouseButton(x: Double, y: Double, button: Int, action: Int, modifiers: Int) {
        val mappedButton = GlfwMouseButtonMapper.fromPlatformType(button)
        val mappedAction = GlfwInputActionMapper.fromPlatformType(action)
        val mappedModifiers = GlfwModifierMapper.convertFromPlatformType(modifiers)
        if (mappedButton != null && mappedAction != null) {
            processor(MouseButtonEvent(x, y, mappedButton, mappedAction, mappedModifiers))
        } else {
            logger.warn {
                "Unsupported mouse action: (button=$button, action=$action, mods=$modifiers)"
            }
        }
    }

    fun keyPress(key: Int, action: Int, modifiers: Int) {
        val mappedKey = GlfwKeyMapper.fromPlatformType(key)
        val mappedAction = GlfwInputActionMapper.fromPlatformType(action)
        val mappedModifiers = GlfwModifierMapper.convertFromPlatformType(modifiers)

        if (mappedKey != null && mappedAction != null) {
            processor(KeyEvent(mappedKey, mappedAction, mappedModifiers))
        } else {
            logger.warn {
                "Unsupported key event: key=$key, action=$action, mods=$modifiers)"
            }
        }
    }

    fun charTyped(unicode: Int) {
        processor(CharEvent(unicode.toChar()))
    }
}

object GlfwMouseButtonMapper: PlatformTypeMapper<Int, MouseButton>(
    mapOf(
        GLFW.GLFW_MOUSE_BUTTON_LEFT to MouseButton.LEFT,
        GLFW.GLFW_MOUSE_BUTTON_RIGHT to MouseButton.RIGHT,
        GLFW.GLFW_MOUSE_BUTTON_MIDDLE to MouseButton.MIDDLE,
        GLFW.GLFW_MOUSE_BUTTON_1 to MouseButton.BUTTON_1,
        GLFW.GLFW_MOUSE_BUTTON_2 to MouseButton.BUTTON_2,
        GLFW.GLFW_MOUSE_BUTTON_3 to MouseButton.BUTTON_3,
        GLFW.GLFW_MOUSE_BUTTON_4 to MouseButton.BUTTON_4,
        GLFW.GLFW_MOUSE_BUTTON_5 to MouseButton.BUTTON_5,
        GLFW.GLFW_MOUSE_BUTTON_6 to MouseButton.BUTTON_6,
        GLFW.GLFW_MOUSE_BUTTON_7 to MouseButton.BUTTON_7,
        GLFW.GLFW_MOUSE_BUTTON_8 to MouseButton.BUTTON_8
    )
)

object GlfwInputActionMapper: PlatformTypeMapper<Int, InputAction>(
    mapOf(
        GLFW.GLFW_PRESS to InputAction.PRESS,
        GLFW.GLFW_RELEASE to InputAction.RELEASE
    )
)

object GlfwKeyMapper: PlatformTypeMapper<Int, Key>(
    mapOf(
        /** Printable keys */
        GLFW.GLFW_KEY_SPACE to Key.SPACE,
        GLFW.GLFW_KEY_APOSTROPHE to Key.APOSTROPHE,
        GLFW.GLFW_KEY_COMMA to Key.COMMA,
        GLFW.GLFW_KEY_MINUS to Key.MINUS,
        GLFW.GLFW_KEY_PERIOD to Key.PERIOD,
        GLFW.GLFW_KEY_SLASH to Key.SLASH,
        GLFW.GLFW_KEY_0 to Key.NUM_0,
        GLFW.GLFW_KEY_1 to Key.NUM_1,
        GLFW.GLFW_KEY_2 to Key.NUM_2,
        GLFW.GLFW_KEY_3 to Key.NUM_3,
        GLFW.GLFW_KEY_4 to Key.NUM_4,
        GLFW.GLFW_KEY_5 to Key.NUM_5,
        GLFW.GLFW_KEY_6 to Key.NUM_6,
        GLFW.GLFW_KEY_7 to Key.NUM_7,
        GLFW.GLFW_KEY_8 to Key.NUM_8,
        GLFW.GLFW_KEY_9 to Key.NUM_9,
        GLFW.GLFW_KEY_SEMICOLON to Key.SEMICOLON,
        GLFW.GLFW_KEY_EQUAL to Key.EQUAL,
        GLFW.GLFW_KEY_A to Key.A,
        GLFW.GLFW_KEY_B to Key.B,
        GLFW.GLFW_KEY_C to Key.C,
        GLFW.GLFW_KEY_D to Key.D,
        GLFW.GLFW_KEY_E to Key.E,
        GLFW.GLFW_KEY_F to Key.F,
        GLFW.GLFW_KEY_G to Key.G,
        GLFW.GLFW_KEY_H to Key.H,
        GLFW.GLFW_KEY_I to Key.I,
        GLFW.GLFW_KEY_J to Key.J,
        GLFW.GLFW_KEY_K to Key.K,
        GLFW.GLFW_KEY_L to Key.L,
        GLFW.GLFW_KEY_M to Key.M,
        GLFW.GLFW_KEY_N to Key.N,
        GLFW.GLFW_KEY_O to Key.O,
        GLFW.GLFW_KEY_P to Key.P,
        GLFW.GLFW_KEY_Q to Key.Q,
        GLFW.GLFW_KEY_R to Key.R,
        GLFW.GLFW_KEY_S to Key.S,
        GLFW.GLFW_KEY_T to Key.T,
        GLFW.GLFW_KEY_U to Key.U,
        GLFW.GLFW_KEY_V to Key.V,
        GLFW.GLFW_KEY_W to Key.W,
        GLFW.GLFW_KEY_X to Key.X,
        GLFW.GLFW_KEY_Y to Key.Y,
        GLFW.GLFW_KEY_Z to Key.Z,
        GLFW.GLFW_KEY_LEFT_BRACKET to Key.LEFT_BRACKET,
        GLFW.GLFW_KEY_BACKSLASH to Key.BACKSLASH,
        GLFW.GLFW_KEY_RIGHT_BRACKET to Key.RIGHT_BRACKET,
        GLFW.GLFW_KEY_GRAVE_ACCENT to Key.GRAVE_ACCENT,

        /** Function keys */
        GLFW.GLFW_KEY_ESCAPE to Key.ESCAPE,
        GLFW.GLFW_KEY_ENTER to Key.ENTER,
        GLFW.GLFW_KEY_TAB to Key.TAB,
        GLFW.GLFW_KEY_BACKSPACE to Key.BACKSPACE,
        GLFW.GLFW_KEY_INSERT to Key.INSERT,
        GLFW.GLFW_KEY_DELETE to Key.DELETE,
        GLFW.GLFW_KEY_RIGHT to Key.RIGHT,
        GLFW.GLFW_KEY_LEFT to Key.LEFT,
        GLFW.GLFW_KEY_DOWN to Key.DOWN,
        GLFW.GLFW_KEY_UP to Key.UP,
        GLFW.GLFW_KEY_PAGE_UP to Key.PAGE_UP,
        GLFW.GLFW_KEY_PAGE_DOWN to Key.PAGE_DOWN,
        GLFW.GLFW_KEY_HOME to Key.HOME,
        GLFW.GLFW_KEY_END to Key.END,
        GLFW.GLFW_KEY_CAPS_LOCK to Key.CAPS_LOCK,
        GLFW.GLFW_KEY_SCROLL_LOCK to Key.SCROLL_LOCK,
        GLFW.GLFW_KEY_NUM_LOCK to Key.NUM_LOCK,
        GLFW.GLFW_KEY_PRINT_SCREEN to Key.PRINT_SCREEN,
        GLFW.GLFW_KEY_PAUSE to Key.PAUSE,
        GLFW.GLFW_KEY_F1 to Key.F1,
        GLFW.GLFW_KEY_F2 to Key.F2,
        GLFW.GLFW_KEY_F3 to Key.F3,
        GLFW.GLFW_KEY_F4 to Key.F4,
        GLFW.GLFW_KEY_F5 to Key.F5,
        GLFW.GLFW_KEY_F6 to Key.F6,
        GLFW.GLFW_KEY_F7 to Key.F7,
        GLFW.GLFW_KEY_F8 to Key.F8,
        GLFW.GLFW_KEY_F9 to Key.F9,
        GLFW.GLFW_KEY_F10 to Key.F10,
        GLFW.GLFW_KEY_F11 to Key.F11,
        GLFW.GLFW_KEY_F12 to Key.F12,
        GLFW.GLFW_KEY_KP_0 to Key.NUMPAD_0,
        GLFW.GLFW_KEY_KP_1 to Key.NUMPAD_1,
        GLFW.GLFW_KEY_KP_2 to Key.NUMPAD_2,
        GLFW.GLFW_KEY_KP_3 to Key.NUMPAD_3,
        GLFW.GLFW_KEY_KP_4 to Key.NUMPAD_4,
        GLFW.GLFW_KEY_KP_5 to Key.NUMPAD_5,
        GLFW.GLFW_KEY_KP_6 to Key.NUMPAD_6,
        GLFW.GLFW_KEY_KP_7 to Key.NUMPAD_7,
        GLFW.GLFW_KEY_KP_8 to Key.NUMPAD_8,
        GLFW.GLFW_KEY_KP_9 to Key.NUMPAD_9,
        GLFW.GLFW_KEY_KP_DECIMAL to Key.NUMPAD_DECIMAL,
        GLFW.GLFW_KEY_KP_DIVIDE to Key.NUMPAD_DIVIDE,
        GLFW.GLFW_KEY_KP_MULTIPLY to Key.NUMPAD_MULTIPLY,
        GLFW.GLFW_KEY_KP_SUBTRACT to Key.NUMPAD_SUBTRACT,
        GLFW.GLFW_KEY_KP_ADD to Key.DUMPAD_ADD,
        GLFW.GLFW_KEY_KP_ENTER to Key.NUMPAD_ENTER,
        GLFW.GLFW_KEY_KP_EQUAL to Key.NUMPAD_EQUAL,
        GLFW.GLFW_KEY_LEFT_SHIFT to Key.LEFT_SHIFT,
        GLFW.GLFW_KEY_LEFT_CONTROL to Key.LEFT_CONTROL,
        GLFW.GLFW_KEY_LEFT_ALT to Key.LEFT_ALT,
        GLFW.GLFW_KEY_LEFT_SUPER to Key.LEFT_SUPER,
        GLFW.GLFW_KEY_RIGHT_SHIFT to Key.RIGHT_SHIFT,
        GLFW.GLFW_KEY_RIGHT_CONTROL to Key.RIGHT_CONTROL,
        GLFW.GLFW_KEY_RIGHT_ALT to Key.RIGHT_ALT,
        GLFW.GLFW_KEY_RIGHT_SUPER to Key.RIGHT_SUPER,
        GLFW.GLFW_KEY_MENU to Key.MENU
    )
)

object GlfwModifierMapper: PlatformTypeMapper<Int, Modifier>(
    mapOf(
        GLFW.GLFW_MOD_CONTROL to Modifier.CONTROL,
        GLFW.GLFW_MOD_SHIFT to Modifier.SHIFT,
        GLFW.GLFW_MOD_ALT to Modifier.ALT,
        GLFW.GLFW_MOD_SUPER to Modifier.SUPER
    )
) {
    @Suppress("NOTHING_TO_INLINE")
    inline fun convertFromPlatformType(modifiers: Int): Int {
        // since we use the same bit positions just return it
        return modifiers
    }
}
