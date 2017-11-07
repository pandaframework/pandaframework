package io.polymorphicpanda.faux.ecs


sealed class Option<out T> {
    data class Some<out T> (val result: T): Option<T>()
    object None: Option<Nothing>()

    fun <K> map(transform: (T) -> K): Option<K> = when (this) {
        is Some<T> -> Some(transform(result))
        is None -> None
    }
}

typealias Some<T> = Option.Some<T>
typealias None = Option.None

sealed class Try<out T> {
    data class Success<out T>(val result: T): Try<T>()
    data class Failure(val error: Throwable): Try<Nothing>()

    fun <K> map(transform: (T) -> K): Try<K> = when (this) {
        is Success<T> -> Success(transform(result))
        is Failure -> this
    }
}

typealias Success<T> = Try.Success<T>
typealias Failure = Try.Failure
