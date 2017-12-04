package io.polymorphicpanda.faux.fui.property

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object SimplePropertySpec: Spek({
    val initialValue = 10
    val property by memoized { SimpleProperty(initialValue) }

    describe("subscriptions") {
        val subscriber by memoized { mock<ValueChangeSubscriber<Int>>() }

        beforeEachTest {
            property.subscribe(subscriber)
        }


        on("value changed") {
            val value = 20
            property.setValue(value)
            it("should notify when value has changed") {
                verify(subscriber).valueChanged(initialValue, value)
            }
        }

        on("value changed but same") {
            property.setValue(initialValue)
            it("should notify when value has changed") {
                verify(subscriber, never()).valueChanged(any(), any())
            }
        }

        on("unsubscribe") {
            property.unsubscribe(subscriber)

            it("should not be notified when value has changed") {
                property.setValue(20)
                verify(subscriber, never()).valueChanged(any(), any())
            }
        }
    }

    describe("bindings") {
        val otherProperty by memoized { SimpleProperty(30) }

        on("bind") {
            val subscriber = mock<ValueChangeSubscriber<Int>>()
            property.subscribe(subscriber)

            property.bind(otherProperty)

            it("should notify subscribers") {
                verify(subscriber).valueChanged(initialValue, otherProperty.getValue())
            }

            it("should receive the new value") {
                assertThat(property.getValue(), equalTo(otherProperty.getValue()))
            }
        }

        context("already bounded") {
            beforeEachTest { property.bind(otherProperty) }

            it("should throw an IllegalStateException when value is set") {
                assertThat({
                    property.setValue(100)
                }, throws<IllegalStateException>())
            }

            on("unbind") {
                property.unbind()

                it("should allow setting value again") {
                    assertThat({
                        property.setValue(100)
                    }, !throws<IllegalStateException>())
                }
            }
        }
    }
})
