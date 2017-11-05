package io.polymorphicpanda.faux.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.sameInstance
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.reflect.KClass

object AspectSpec: Spek({
    class C1: Component

    describe("aspects()") {
        it("initial instance is a singleton") {
            assert.that(aspects(), sameInstance(aspects()))
        }

        describe("#with(...)") {
            val initial by memoized { aspects() }
            val next by memoized { initial.with(C1::class) }

            it("always return a new instance") {
                assert.that(next, !sameInstance(initial))
            }

            it("updates included list") {
                assert.that(next.included, hasElement(C1::class as KClass<*>))
            }
        }

        describe("#without(...)") {
            val initial by memoized { aspects() }
            val next by memoized { initial.without(C1::class) }

            it("always return a new instance") {
                assert.that(next, !sameInstance(initial))
            }

            it("updates excluded list") {
                assert.that(next.excluded, hasElement(C1::class as KClass<*>))
            }
        }
    }
})
