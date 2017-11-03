package io.polymorphicpanda.polymorphic.ecs

class Aspect internal constructor(internal val included: List<ComponentType>,
                                  internal val excluded: List<ComponentType>) {

    fun with(vararg componentTypes: ComponentType): Aspect {
        return Aspect(
            included + componentTypes,
            excluded
        )
    }

    fun without(vararg componentTypes: ComponentType): Aspect {
        return Aspect(
            included,
            excluded + componentTypes
        )
    }
}

fun aspects() = Aspect(emptyList(), emptyList())
