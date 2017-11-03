package org.polymorphicpanda.polymorphic.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import io.polymorphicpanda.polymorphic.ecs.Component
import io.polymorphicpanda.polymorphic.ecs.ComponentMapper
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

internal object ComponentMapperSpec: SubjectSpek<ComponentMapper>({
    val mapper by subject { ComponentMapper() }

    class C1: Component
    class C2: Component

    describe("ComponentMapper#map") {
        it("produces unique ids") {
            assert.that(mapper.map(C1::class), !equalTo(mapper.map(C2::class)))
        }

        it("return the same id for a component") {
            assert.that(mapper.map(C1::class), equalTo(mapper.map(C1::class)))
        }
    }
})
