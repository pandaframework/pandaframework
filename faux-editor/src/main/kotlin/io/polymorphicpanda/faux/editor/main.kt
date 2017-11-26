package io.polymorphicpanda.faux.editor

import io.polymorphicpanda.faux.core.launcher.EngineConfigurer
import io.polymorphicpanda.faux.core.config.EngineSettings
import io.polymorphicpanda.faux.core.launcher.Launcher

class EditorConfigurer: EngineConfigurer() {
    override fun configureSpecific(settings: EngineSettings) {
        val applicationTitle = settings.windowConfig.title
        settings.setWindowTitle("Faux Engine Editor | $applicationTitle")
    }

}

fun main(args: Array<String>) {
    Launcher(EditorConfigurer()).launch(args)
}
