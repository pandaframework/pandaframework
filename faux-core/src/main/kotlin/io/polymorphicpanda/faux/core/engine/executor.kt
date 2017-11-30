package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.system.System
import io.polymorphicpanda.faux.system.SystemContext
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext

class SystemExecutor {
    fun execute(coroutineContext: CoroutineContext,
                duration: Double,
                graph: DynamicGraph<Pair<System, SystemContext>>) {
        runBlocking(coroutineContext) {
            var freeNodes = graph.getFreeNodes()

            do {
                val jobs = mutableListOf<Deferred<Pair<System, SystemContext>>>()
                for (pair in freeNodes) {
                    val (system, context) = pair
                    jobs += async (coroutineContext) {
                        system.process(duration, context)
                        pair
                    }
                }

                jobs.forEach {
                    graph.removeNode(it.await())
                }

                freeNodes = graph.getFreeNodes()


            } while (!freeNodes.isEmpty())
        }
    }
}
