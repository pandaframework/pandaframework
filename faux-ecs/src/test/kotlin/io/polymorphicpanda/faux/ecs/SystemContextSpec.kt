package io.polymorphicpanda.faux.ecs

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.hasElement
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.roaringbitmap.buffer.MutableRoaringBitmap

object SystemContextSpec: Spek({
    val worldContext by memoized { mock<WorldContext>() }
    val componentId1 = 1
    val componentId2 = 2
    val componentId3 = 3
    val includedBitSet by memoized {
        MutableRoaringBitmap().apply {
            add(componentId1)
            add(componentId2)
        }
    }
    val excludedBitSet by memoized { MutableRoaringBitmap().apply { add(componentId3) } }
    val systemContext by memoized { SystemContext(worldContext, includedBitSet, excludedBitSet) }

    on("manage") {
        val entity = 1
        systemContext.manage(entity)

        it("should delegate to worldContext") {
            verify(worldContext).manage(eq(entity))
        }
    }

    on("changeSet") {
        val changeSet: EditScope.() -> Unit = {}
        systemContext.changeSet(changeSet)

        it("should delegate to worldContext") {
            verify(worldContext).changeSet(eq(changeSet))
        }
    }

    describe("update") {
        it("should not track an invalid entity") {
            val entity1 = 1
            val entityRef1 = mock<EntityReference>()
            whenever(entityRef1.entity).thenReturn(entity1)
            whenever(entityRef1.isValid()).thenReturn(false)

            val dirtyEntities = setOf(entityRef1)

            systemContext.update(dirtyEntities)

            assert.that(systemContext.entities(), !hasElement(entity1))

        }

        it("should un-track an entity if bitSet does not match") {
            val entity1 = 1
            val entityRef1 = mock<EntityReference>()
            whenever(entityRef1.entity).thenReturn(entity1)
            val entityBits1 = MutableRoaringBitmap()
            entityBits1.add(componentId1)
            entityBits1.add(componentId2)
            whenever(entityRef1.bitSet).thenReturn(entityBits1)
            whenever(entityRef1.isValid()).thenReturn(true)
            val dirtyEntities = setOf(entityRef1)
            systemContext.update(dirtyEntities)

            entityBits1.remove(componentId1)
            systemContext.update(dirtyEntities)

            assert.that(systemContext.entities(), !hasElement(entity1))
        }

        it("should keep track of entities it's interested in") {
            val entity1 = 1
            val entityRef1 = mock<EntityReference>()
            whenever(entityRef1.entity).thenReturn(entity1)
            val entityBits1 = MutableRoaringBitmap()
            entityBits1.add(componentId1)
            entityBits1.add(componentId2)
            whenever(entityRef1.bitSet).thenReturn(entityBits1)
            whenever(entityRef1.isValid()).thenReturn(true)

            val entity2 = 2
            val entityRef2 = mock<EntityReference>()
            whenever(entityRef2.entity).thenReturn(entity2)
            val entityBits2 = MutableRoaringBitmap()
            entityBits2.add(componentId1)
            entityBits2.add(componentId2)
            entityBits2.add(componentId3)
            whenever(entityRef2.bitSet).thenReturn(entityBits2)
            whenever(entityRef2.isValid()).thenReturn(true)

            val entity3 = 3
            val entityRef3 = mock<EntityReference>()
            whenever(entityRef3.entity).thenReturn(entity3)
            val entityBits3 = MutableRoaringBitmap()
            entityBits3.add(componentId1)
            whenever(entityRef3.bitSet).thenReturn(entityBits3)
            whenever(entityRef3.isValid()).thenReturn(true)

            val dirtyEntities = setOf(entityRef1, entityRef2, entityRef3)

            systemContext.update(dirtyEntities)

            assert.that(systemContext.entities(), hasElement(entity1))
            assert.that(systemContext.entities(), !hasElement(entity2))
            assert.that(systemContext.entities(), !hasElement(entity3))
        }
    }
})
