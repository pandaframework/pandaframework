package io.polymorphicpanda.faux.core

class FauxException(message: String? = null, throwable: Throwable? = null): Throwable(
    message, throwable
)
