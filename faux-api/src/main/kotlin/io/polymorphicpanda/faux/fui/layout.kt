package io.polymorphicpanda.faux.fui

abstract class FuiVbox {
    abstract fun row(height: Double,
                     body: FuiElements.() -> Unit)
}

abstract class FuiHbox {
    abstract fun column(width: Double,
                        body: FuiElements.() -> Unit)
}

abstract class FuiLayouts: FuiContext() {
    abstract fun freeStyle(x: Double = INHERIT_SIZE,
                           y: Double = INHERIT_SIZE,
                           width: Double = INHERIT_SIZE,
                           height: Double = INHERIT_SIZE,
                           body: FuiElements.() -> Unit)

    abstract fun vbox(x: Double = INHERIT_SIZE,
                      y: Double = INHERIT_SIZE,
                      width: Double = INHERIT_SIZE,
                      height: Double = INHERIT_SIZE,
                      spacing: Double = 0.0,
                      body: FuiVbox.() -> Unit)

    abstract fun hbox(x: Double = INHERIT_SIZE,
                      y: Double = INHERIT_SIZE,
                      width: Double = INHERIT_SIZE,
                      height: Double = INHERIT_SIZE,
                      spacing: Double = 0.0,
                      body: FuiHbox.() -> Unit)
}
