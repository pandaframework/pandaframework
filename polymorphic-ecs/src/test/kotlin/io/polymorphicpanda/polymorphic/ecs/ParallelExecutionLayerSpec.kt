package io.polymorphicpanda.polymorphic.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.lessThanOrEqualTo
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.system.measureTimeMillis

@Deprecated("not a correct way to test parallel execution")
object ParallelExecutionLayerSpec: Spek({
    class Sys1(val time: Long): System() {
        override val aspect = aspects()

        suspend override fun process(timeStep: Double, context: SystemContext) {
            delay(time)
        }
    }

    given("two systems with delay1 and delay2, respectively") {
        val delay1 = 1000L
        val delay2 = 1200L
        val executionLayer by memoized { ParallelExecutionLayer(listOf(Sys1(delay1), Sys1(delay2))) }

        describe("execution time") {

            it("should not exceed delay1 + delay2") {
                val time = measureTimeMillis {
                    executionLayer.execute(CommonPool, 1.0, { mock() })
                }

                assert.that(time, lessThanOrEqualTo(delay1 + delay2))
            }
        }
    }



})
