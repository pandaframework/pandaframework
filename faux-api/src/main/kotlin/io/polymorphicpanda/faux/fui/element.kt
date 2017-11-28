package io.polymorphicpanda.faux.fui

abstract class FuiElements: FuiLayouts() {
    abstract fun textInput(state: TextState,
                           x: Double = INHERIT_SIZE,
                           y: Double = INHERIT_SIZE,
                           width: Double = INHERIT_SIZE,
                           height: Double = INHERIT_SIZE,
                           placeholder: String = "")

    abstract fun passwordInput(state: TextState,
                               x: Double = INHERIT_SIZE,
                               y: Double = INHERIT_SIZE,
                               width: Double = INHERIT_SIZE,
                               height: Double = INHERIT_SIZE,
                               placeholder: String = "")

    abstract fun button(x: Double = INHERIT_SIZE,
                        y: Double = INHERIT_SIZE,
                        width: Double = INHERIT_SIZE,
                        height: Double = INHERIT_SIZE,
                        text: String,
                        action: () -> Unit)

    abstract fun checkbox(state: ToggleState,
                          x: Double = INHERIT_SIZE,
                          y: Double = INHERIT_SIZE,
                          width: Double = INHERIT_SIZE,
                          height: Double = INHERIT_SIZE)

    abstract fun text(text: String,
                      x: Double = INHERIT_SIZE,
                      y: Double = INHERIT_SIZE,
                      width: Double = INHERIT_SIZE,
                      height: Double = INHERIT_SIZE)
}

