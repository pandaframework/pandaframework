package io.polymorphicpanda.faux.core.util

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

object DynamicGraphSpec: Spek({
    given("this graph: a -> b | b -> c, d | e") {
        val graph by memoized {
            DynamicGraph<String>().apply {
                // a -> b
                // b -> c, d
                // e
                addEdge("a", "b")
                addEdge("b", "c")
                addEdge("b", "d")
                addNode("e")
            }
        }

        it("should initially contain the free nodes: c, d and e") {
            assertThat(graph.getFreeNodes(), equalTo(setOf("c", "d", "e")))
        }

        context("when e is removed") {
            beforeEachTest { graph.removeNode("e") }
            it("should contain the free nodes: c and d") {
                assertThat(graph.getFreeNodes(), equalTo(setOf("c", "d")))
            }

            context("when d is removed") {
                beforeEachTest { graph.removeNode("d") }
                it("should only contain the free node c") {
                    assertThat(graph.getFreeNodes(), equalTo(setOf("c")))
                }

                context("when c is removed") {
                    beforeEachTest { graph.removeNode("c") }
                    it("should only contain the free node b") {
                        assertThat(graph.getFreeNodes(), equalTo(setOf("b")))
                    }

                    context("when b is removed") {
                        beforeEachTest { graph.removeNode("b") }
                        it("should only contain the free node a") {
                            assertThat(graph.getFreeNodes(), equalTo(setOf("a")))
                        }

                        context("when a is removed") {
                            beforeEachTest { graph.removeNode("a") }
                            it("should be empty") {
                                assertThat(graph.getFreeNodes(), equalTo(emptySet()))
                            }
                        }
                    }
                }
            }
        }
    }
})
