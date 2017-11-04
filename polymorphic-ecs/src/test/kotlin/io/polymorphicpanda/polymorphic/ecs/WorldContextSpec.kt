package io.polymorphicpanda.polymorphic.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.sameInstance
import com.natpryce.hamkrest.throws
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object WorldContextSpec: Spek({
    val editScope by memoized {
        mock<EditScope>().apply {
            val entityStorage = mock<EntityStorage>()
            val dirtyEntityTracker = mock<DirtyEntityTracker>()
            whenever(entityStorage.dirtyEntityTracker).thenReturn(dirtyEntityTracker)
            whenever(dirtyEntityTracker.dirtySet()).thenReturn(emptySet())
            whenever(this.entityStorage).thenReturn(entityStorage)
        }
    }
    val worldContext by memoized { WorldContext(editScope) }


    describe("system registration") {
        class Sys1: System() {
            override val aspect = aspects()

            suspend override fun process(timeStep: Double, context: SystemContext) {
                TODO()
            }
        }

        it("should only register once") {
            val system = Sys1()
            worldContext.registerSystem(system)
            val context = worldContext.contextFor(system)

            // this should do nothing
            worldContext.registerSystem(system)
            assert.that(context, sameInstance(worldContext.contextFor(system)))
        }

        it("should throw an exception when system is not registered") {
            val system = Sys1()

            assert.that({worldContext.contextFor(system)}, throws<NoSuchElementException>())
        }
    }

    on("manage") {
        val entity = 1
        worldContext.manage(entity)

        it("should delegate to editScope") {
            verify(editScope).editorFor(eq(entity))
        }
    }

    on("resolveChangeSets") {
        val edit1 = mock<EditScope.() -> Unit>()
        val edit2 = mock<EditScope.() -> Unit>()
        val order = inOrder(edit1, edit2)

        worldContext.changeSet(edit1)
        worldContext.changeSet(edit2)

        worldContext.resolveChangeSets()

        it("should resolve serially") {
            order.verify(edit1).invoke(eq(editScope))
            order.verify(edit2).invoke(eq(editScope))
        }
    }
})
