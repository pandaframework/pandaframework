package io.polymorphicpanda.faux.runtime

import io.polymorphicpanda.faux.service.Service

expect object Faux {
    inline fun <reified T: Service> getService(): T
}
