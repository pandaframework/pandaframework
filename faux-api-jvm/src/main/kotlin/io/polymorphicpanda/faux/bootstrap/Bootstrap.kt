package io.polymorphicpanda.faux.bootstrap

import io.polymorphicpanda.faux.application.Application

interface Bootstrap {
    fun getApplication(): Application
}
