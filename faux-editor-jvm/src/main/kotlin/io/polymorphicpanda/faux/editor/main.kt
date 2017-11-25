package io.polymorphicpanda.faux.editor

import io.polymorphicpanda.faux.core.EngineSettings
import io.polymorphicpanda.faux.core.Launcher

fun main(args: Array<String>) {
    val editor = object: Launcher() {
        override fun initLauncherSettings(settings: EngineSettings) {
            val applicationTitle = settings.windowConfig.title
            settings.setWindowTitle("Faux Engine Editor | $applicationTitle")
        }

    }

    editor.run(args)
}
