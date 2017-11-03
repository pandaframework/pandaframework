package io.polymorphicpanda.polymorphic.ecs

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object EntityEditorSpec: Spek({
    class C1: Component

    val entityReference by memoized { mock<EntityReference>() }
    val componentTypeMap by memoized {
        mutableMapOf<ComponentType, ComponentId>(
            C1::class to 1
        )
    }

    beforeEachTest {
        whenever(entityReference.get<C1>(any())).thenReturn(mock())
    }

    val entityEditor by memoized { EntityEditor(entityReference, componentTypeMap) }

    on("get(...)") {
        entityEditor.get<C1>()

        it("should delegate to EntityReference#get(componentId)") {
            verify(entityReference).get<C1>(eq(componentTypeMap.getValue(C1::class)))
        }
    }

    on("contains(...)") {
        entityEditor.contains<C1>()

        it("should delegate to EntityReference#contains(componentId)") {
            verify(entityReference).contains(eq(componentTypeMap.getValue(C1::class)))
        }
    }

})
