package io.polymorphicpanda.faux.core.engine

import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.core.util.DynamicGraph
import io.polymorphicpanda.faux.entity.Entity
import io.polymorphicpanda.faux.entity.EntityEditor
import io.polymorphicpanda.faux.system.Aspect
import io.polymorphicpanda.faux.system.System
import io.polymorphicpanda.faux.system.SystemContext
import io.polymorphicpanda.faux.system.SystemDescriptor
import io.polymorphicpanda.faux.system.aspects
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.reflect.KClass
import kotlin.system.measureTimeMillis

class SystemExecutor {
    fun execute(coroutineContext: CoroutineContext,
                duration: Double,
                graph: DynamicGraph<System>,
                contextProvider: (System) -> SystemContext) {
        runBlocking(coroutineContext) {
            var freeNodes = graph.getFreeNodes()

            do {
                val jobs = mutableListOf<Deferred<System>>()
                for (system in freeNodes) {
                    jobs += async (coroutineContext) {
                        system.apply {
                            process(duration, contextProvider(system))
                        }
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
private open class BaseSystem: System() {
    override suspend fun CoroutineScope.process(duration: Double, context: SystemContext) {
        delay(100)
        println("${Thread.currentThread().name}: Executing ${this@BaseSystem::class.simpleName} ")
    }
}

private class S1: BaseSystem() {
    companion object: SystemDescriptor<S4> {
        override fun create() = S4()

        override val aspect: Aspect = aspects()
        override val id: KClass<S4> = S4::class

    }
}

private class S2: BaseSystem() {
    companion object: SystemDescriptor<S2> {
        override fun create() = S2()

        override val aspect: Aspect = aspects()
        override val id: KClass<S2> = S2::class

    }
}

private class S3: BaseSystem() {
    companion object: SystemDescriptor<S3> {
        override fun create() = S3()

        override val aspect: Aspect = aspects()
        override val id: KClass<S3> = S3::class

    }
}


private class S4: BaseSystem() {
    companion object: SystemDescriptor<S4> {
        override fun create() = S4()

        override val aspect: Aspect = aspects()
        override val id: KClass<S4> = S4::class

    }
}

private class S5: BaseSystem() {
    companion object: SystemDescriptor<S5> {
        override fun create() = S5()

        override val aspect: Aspect = aspects()
        override val id: KClass<S5> = S5::class

    }
}

private class TestSystemContext: SystemContext {
    override fun entities(): Set<Entity> {
        TODO()
    }

    override fun create(blueprint: Blueprint?): EntityEditor {
        TODO()
    }

    override fun manage(entity: Entity): EntityEditor {
        TODO()
    }
}

fun main(args: Array<String>) {
    val graph = DynamicGraph<System>().apply {
        // s1 -> s2
        // s2 -> s3, s4
        // s5

        val s1 = S1()
        val s2 = S2()
        val s3 = S3()
        val s4 = S4()
        val s5 = S5()

        addEdge(s1, s2)
        addEdge(s2, s3)
        addEdge(s2, s4)
        addNode(s5)
    }

    val executor = SystemExecutor()

    while (true) {
        println("frame: start")
        val duration = measureTimeMillis { executor.execute(CommonPool, 1.0, graph.clone(), { TestSystemContext() }) }
        println("duration: $duration")
        println("frame: end")
    }
}
