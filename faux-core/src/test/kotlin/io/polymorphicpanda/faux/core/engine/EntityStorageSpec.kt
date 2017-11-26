package io.polymorphicpanda.faux.core.engine

import com.natpryce.hamkrest.assertion.assert
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.sameInstance
import io.polymorphicpanda.faux.component.Component
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object EntityStorageSpec: Spek({
    class C1: Component
    val componentId1 = 1
    val entity = 1
    val entityStorage by memoized { EntityStorage() }

    describe("manage(...)") {
        it("should return the same reference for an entity") {
            assert.that(entityStorage.manage(entity), sameInstance(entityStorage.manage(entity)))
        }
    }

    describe("entity references") {
        val entityReference by memoized { entityStorage.manage(entity) }

        it("should have a reference to the entity") {
            assertThat(entity, equalTo(entityReference.entity))
        }


        on("adding a component") {
            val component = C1()
            entityReference.add(componentId1, component)

            var ref: EntityStorageRef? = null
            entityStorage.resolve {
                ref = it
            }

            it("should be returned by get(...)") {
                assert.that(entityReference.get(componentId1), sameInstance(component))
            }

            it("should set the corresponding bit in bitSet") {
                assertThat(entityReference.bitSet.contains(componentId1), equalTo(true))
            }

            it("should mark it as dirty") {

                assertThat(ref, equalTo(entityReference))
            }
        }

        on("removing a managed component") {
            entityReference.add(componentId1, C1())
            entityStorage.resolve {  }
            entityReference.remove(componentId1)

            var ref: EntityStorageRef? = null
            entityStorage.resolve {
                ref = it
            }

            it("should mark it as dirty when that component is removed") {
                assertThat(ref, equalTo(entityReference))
            }

            it("should unset the corresponding bit in bitSet") {
                assertThat(entityReference.bitSet.contains(componentId1), equalTo(false))
            }
        }

        on("removing a non managed component") {
            entityReference.remove(componentId1)

            it("should not do anything if it doesn't have that component") {
                var ref: EntityStorageRef? = null
                entityStorage.resolve {
                    ref = it
                }
                assertThat(ref, equalTo<EntityStorageRef>(null))
            }
        }
    }

})
