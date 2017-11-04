package io.polymorphicpanda.polymorphic.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.sameInstance
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object EditScopeSpec: Spek({
    class C1: Component
    val componentId = 1
    val componentMapping by memoized {
        mutableMapOf<ComponentType, ComponentId>(
        C1::class to componentId
        )
    }
    val entityProvider by memoized { mock<EntityProvider>() }
    val entityStorage by memoized { mock<EntityStorage>() }
    val editScope by memoized { EditScope(componentMapping, entityStorage, entityProvider) }

    describe("editorFor") {
        it("should return the same instance per entity") {
            val entity = 1
            whenever(entityStorage.manage(entity)).thenReturn(mock())
            assert.that(editScope.editorFor(entity), sameInstance(editScope.editorFor(entity)))
        }
    }

    on("Context#create") {
        val entity = 1
        whenever(entityStorage.manage(entity)).thenReturn(mock())
        whenever(entityProvider.acquire()).thenReturn(entity)
        val context = mock<Context>()

        with(editScope) {
            context.create()
        }

        it("should acquire an entity from provider") {
            verify(entityProvider).acquire()
        }

        it("should invoke EntityStorage#manage") {
            verify(entityStorage).manage(eq(entity))
        }
    }

    describe("EntityEditor#add") {
        it("should delegate to EntityReference") {
            val entityEditor = mock<EntityEditor>()
            val entityRef = mock<EntityReference>()
            whenever(entityEditor.entityReference).thenReturn(entityRef)

            val component = C1()
            with(editScope) {
                entityEditor.add(component)
            }

            verify(entityRef).add(eq(componentId), eq(component))
        }
    }

    on("EntityEditor#destory") {
        val entity = 1
        val entityEditor = mock<EntityEditor>()
        val entityRef = mock<EntityReference>()
        whenever(entityEditor.entityReference).thenReturn(entityRef)
        whenever(entityEditor.entity).thenReturn(entity)

        with(editScope) {
            entityEditor.destroy()
        }

        it("should release entity id") {
            verify(entityProvider).release(eq(entity))
        }

        it("should release reference") {
            verify(entityRef).release()
        }
    }
})
