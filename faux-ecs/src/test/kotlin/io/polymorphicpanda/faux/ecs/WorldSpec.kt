package io.polymorphicpanda.faux.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.throws
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.CommonPool
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object WorldSpec: Spek({
    val worldContext by memoized { mock<WorldContext>() }
    val system1 = mock<System>()
    val system2 = mock<System>()
    val executionLayer1 by memoized {
        mock<ExecutionLayer>().apply {
            whenever(systems).thenReturn(listOf(system1))
        }
    }

    val executionLayer2 by memoized {
        mock<ExecutionLayer>().apply {
            whenever(systems).thenReturn(listOf(system2))
        }
    }

    val executionLayers by memoized { listOf(executionLayer1, executionLayer2) }
    val dispatcher by memoized { CommonPool }
    val world by memoized { World(worldContext, executionLayers, dispatcher) }

    on("init") {
        world.init()

        it("should register each system") {
            verify(worldContext, times(2)).registerSystem(any())
        }

        it("should init each system") {
            verify(system1).init()
            verify(system2).init()
        }
    }

    on("dispose") {
        world.dispose()

        it("should dispose each system") {
            verify(system1).dispose()
            verify(system2).dispose()
        }
    }

    describe("step") {
        it("should throw exception when init is not invoked") {
            assert.that({world.step(1.0)}, throws<IllegalStateException>())
        }

        it("should resolve changesets per execution layer") {
            val order = inOrder(worldContext, executionLayer1, executionLayer2)
            val timeStep = 1.0
            world.init()
            world.step(timeStep)

            order.verify(executionLayer1).execute(eq(dispatcher), eq(timeStep), any())
            order.verify(worldContext).resolveChangeSets()
            order.verify(executionLayer2).execute(eq(dispatcher), eq(timeStep), any())
            order.verify(worldContext).resolveChangeSets()
        }
    }
})
