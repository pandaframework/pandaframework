package io.polymorphicpanda.faux.core.text

import io.polymorphicpanda.faux.event.Event

class CharEvent(val char: Char): Event {
    override fun toString(): String {
        return "CharEvent(char=$char)"
    }
}
