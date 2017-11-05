package io.polymorphicpanda.faux.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.hasElement
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object DirtyEntityTrackerSpec: Spek({
    val dirtyEntityTracker by memoized { DirtyEntityTracker() }

    val entityRef by memoized { mock<EntityReference>() }

    on("tracking an entity") {
        dirtyEntityTracker.track(entityRef)

        it("should be contained in dirtySet") {
            assert.that(dirtyEntityTracker.dirtySet(), hasElement(entityRef))
        }
    }

    it("should not be contained in dirySet when tracker is cleared") {
        dirtyEntityTracker.track(entityRef)
        dirtyEntityTracker.clear()
        assert.that(dirtyEntityTracker.dirtySet(), !hasElement(entityRef))
    }
})
