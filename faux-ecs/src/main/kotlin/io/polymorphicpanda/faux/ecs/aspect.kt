package io.polymorphicpanda.faux.ecs

data class Aspect internal constructor(internal val included: List<ComponentType>,
                                       internal val excluded: List<ComponentType>) {
    fun with(vararg componentTypes: ComponentType) = copy(included = included + componentTypes)
    fun without(vararg componentTypes: ComponentType) = copy(excluded = excluded + componentTypes)
}

private val initial = Aspect(emptyList(), emptyList())
fun aspects() = initial
