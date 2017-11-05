package io.polymorphicpanda.faux.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isA
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Unconfined
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it

object WorldBuildSpec: Spek({
    it("should create a world with correct configs") {
        class C1: Component

        val coroutineDispatcher: CoroutineDispatcher = Unconfined
        val world = WorldBuilder()
            .withComponents(listOf(C1::class))
            .withAsyncExecutionLayer(emptyList())
            .withExecutionLayer(emptyList())
            .withCoroutineDispatcher(coroutineDispatcher)
            .build()

        assert.that(world.executionLayers[0], isA<ParallelExecutionLayer>())
        assert.that(world.executionLayers[1], isA<SerialExecutionLayer>())

        assert.that(world.coroutineDispatcher, equalTo(coroutineDispatcher))
        val editScope = world.worldContext.editScope
        assert.that(editScope.componentTypeMap.containsKey(C1::class), equalTo(true))
    }
})
