package io.polymorphicpanda.faux.fui

interface State

data class ToggleState(val toggled: Boolean): State
data class TextState(val text: String): State
