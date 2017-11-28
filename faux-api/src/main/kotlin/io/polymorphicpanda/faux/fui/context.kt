package io.polymorphicpanda.faux.fui

import kotlin.reflect.KClass

abstract class FuiContext {
    abstract fun <T: State> T.mutate(action: () -> T)

    inline fun <reified T: State> FuiContext.state() = state(T::class)
    inline fun <reified T: State> globalState() = globalState(T::class)
    inline fun <reified T: State> customState(noinline provider: () -> T,
                                              noinline onMutate: (T) -> Unit) = customState(
        T::class, provider, onMutate
    )


    @PublishedApi
    abstract internal fun <T: State> state(type: KClass<T>): Lazy<T>
    @PublishedApi
    abstract internal fun <T: State> globalState(type: KClass<T>): Lazy<T>
    @PublishedApi
    abstract internal fun <T: State> customState(type: KClass<T>,
                                                 provider: () -> T,
                                                 onMutate: (T) -> Unit): Lazy<T>

    companion object {
        const val INHERIT_SIZE = -1.0
    }
}
