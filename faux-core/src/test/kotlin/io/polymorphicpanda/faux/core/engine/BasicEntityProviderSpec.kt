package io.polymorphicpanda.faux.core.engine

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it

object BasicEntityProviderSpec: Spek({
    val entityProvider by memoized { BasicEntityProvider() }

    it("should return unique entities") {
        assert.that(entityProvider.acquire(), !equalTo(entityProvider.acquire()))
    }

    it("should re-use released entities") {
        val entity = entityProvider.acquire()

        entityProvider.release(entity)
        assert.that(entity, equalTo(entityProvider.acquire()))
    }
})
