package io.polymorphicpanda.faux.core.window

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.polymorphicpanda.faux.core.util.PlatformTypeMapper
import io.polymorphicpanda.faux.input.InputAction
import io.polymorphicpanda.faux.input.MouseButton
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.lwjgl.glfw.GLFW

abstract class BaseMapperSpec<T, K>(val mapper: PlatformTypeMapper<T, K>, val expectedMappings: Map<T, K>): Spek({
    describe("mappings") {
        expectedMappings.forEach { key, value ->
            it("$key should map to $value") {
                assertThat(mapper.fromPlatformType(key), equalTo(value))
            }

            it("$value should map to $key") {
                assertThat(mapper.toPlatformType(value), equalTo(key))
            }
        }
    }
})

object GlfwMouseButtonMapperSpec: BaseMapperSpec<Int, MouseButton>(
    GlfwMouseButtonMapper,
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

object GlfwInputActionMapperSpec: BaseMapperSpec<Int, InputAction>(
    GlfwInputActionMapper,
    mapOf(
        GLFW.GLFW_PRESS to InputAction.PRESS,
        GLFW.GLFW_RELEASE to InputAction.RELEASE
    )
)
