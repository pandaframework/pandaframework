package io.polymorphicpanda.faux.util

sealed class Option<T: Any> {
    data class Some<K: Any> (val result: K): Option<K>()
    object None: Option<Nothing>()

    fun <K: Any> map(transform: (T) -> K): Option<out K> = when (this) {
        is Some<T> -> Some(transform(result))
        is None -> None
    }

    fun get(): T = when (this) {
        is Some<T> -> result
        else -> throw NoSuchElementException()
    }

    fun getOrElse(producer: () -> T): T = when (this) {
        is Some<T> -> result
        else -> producer()
    }

    fun getOrNull(): T? = when (this) {
        is Some<T> -> result
        else -> null
    }
}

typealias Some<T> = Option.Some<T>
typealias None = Option.None

fun <T: Any> some(result: T): Option<T> = Some(result)
fun <T: Any> none(): Option<out T> = None

sealed class Try<T: Any> {
    data class Success<K: Any>(val result: K): Try<K>()
    data class Failure(val error: Throwable): Try<Nothing>()

    fun <K: Any> map(transform: (T) -> K): Try<out K> = when (this) {
        is Success<T> -> Success(transform(result))
        is Failure -> this
    }

    fun get(): T = when (this) {
        is Success<T> -> result
        is Failure -> throw error
    }

    fun getOrElse(producer: () -> T): T = when (this) {
        is Success<T> -> result
        else -> producer()
    }

    fun getOrNull(): T? = when (this) {
        is Success -> result
        else -> null
    }
}

typealias Success<T> = Try.Success<T>
typealias Failure = Try.Failure

fun <T: Any> success(result: T): Try<T> = Success(result)
fun <T: Any> failure(error: Throwable): Try<out T> = Failure(error)
