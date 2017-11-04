package io.polymorphicpanda.polymorphic.ecs

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object SerialExecutionLayerSpec: Spek({
    val system1 by memoized { mock<System>() }
    val system2 by memoized { mock<System>() }
    val systemContext1 by memoized { mock<SystemContext>() }
    val systemContext2 by memoized { mock<SystemContext>() }
    val contextMap by memoized { mutableMapOf(
        system1 to systemContext1,
        system2 to systemContext2
    ) }

    val executionLayer by memoized { SerialExecutionLayer(listOf(system1, system2)) }

    on("execute") {
        val timeStep = 1.0
        val inOrder = inOrder(system1, system2)

        runBlocking { executionLayer.execute(CommonPool, timeStep, contextMap::getValue) }

        it("should invoke System#process") {
            runBlocking {
                verify(system1).process(eq(timeStep), eq(systemContext1))
                verify(system2).process(eq(timeStep), eq(systemContext2))
            }
        }

        it("should execute serially") {
            runBlocking {
                inOrder.verify(system1).process(eq(timeStep), eq(systemContext1))
                inOrder.verify(system2).process(eq(timeStep), eq(systemContext2))
            }
        }
    }
})
