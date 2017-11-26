package io.polymorphicpanda.faux.core.engine

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import io.polymorphicpanda.faux.component.Component
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object ComponentMapperSpec: Spek({
    class C1: Component
    class C2: Component

    val mapper by memoized { ComponentMapper() }

    describe("ComponentMapper#map(...)") {
        it("produces unique ids") {
            assert.that(mapper.map(C1::class), !equalTo(mapper.map(C2::class)))
        }

        it("return the same id for a component") {
            assert.that(mapper.map(C1::class), equalTo(mapper.map(C1::class)))
        }
    }
})
