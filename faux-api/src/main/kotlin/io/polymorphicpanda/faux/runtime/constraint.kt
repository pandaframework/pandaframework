package io.polymorphicpanda.faux.runtime

interface Constraint<out T: Any>
class RangedConstraint<out T: Number>(val min: T, val max: T): Constraint<T>
class MinimumConstraint<out T: Number>(val min: T): Constraint<T>
class MaximumConstraint<out T: Number>(val max: T): Constraint<T>
class ValueConstraint<out T: Any>(val values: List<T>): Constraint<T>
