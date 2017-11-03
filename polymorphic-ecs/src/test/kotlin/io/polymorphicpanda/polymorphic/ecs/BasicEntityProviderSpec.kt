package io.polymorphicpanda.polymorphic.ecs

import org.jetbrains.spek.api.Spek

object BasicEntityProviderSpec: Spek({
    val entityProvider by memoized { BasicEntityProvider() }
})
