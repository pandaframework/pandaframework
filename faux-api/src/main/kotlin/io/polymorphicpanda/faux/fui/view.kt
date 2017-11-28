package io.polymorphicpanda.faux.fui



abstract class FuiRoot: FuiLayouts()

abstract class FuiView {
    abstract fun FuiRoot.render(width: Double, height: Double)
}

object LoginView: FuiView() {
    override fun FuiRoot.render(width: Double, height: Double) {
        vbox {
            val remember by state<ToggleState>()
            val username by state<TextState>()
            val password by state<TextState>()

            row(50.0) {
                textInput(username, placeholder = "Username")
            }

            row(50.0) {
                passwordInput(password, placeholder = "Password")
            }

            row(50.0) {
                button(text = "Login") {
                    doLogin(username.text, password.text, remember.toggled)
                }
            }

            row(50.0) {
                hbox {
                    column(20.0) {
                        checkbox(remember)
                    }

                    column(50.0) {
                        text("Remember me")
                    }
                }
            }
        }
    }

    fun doLogin(username: String, password: String, remember: Boolean) {}
}
