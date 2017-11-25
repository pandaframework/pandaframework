package io.polymorphicpanda.faux.core

import io.polymorphicpanda.faux.application.Application

expect class Bootstrapper() {
    fun load(): Application
}
