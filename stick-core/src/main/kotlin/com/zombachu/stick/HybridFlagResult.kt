package com.zombachu.stick

sealed class HybridFlagResult<out T> {
    class Absent<out T> : HybridFlagResult<T>()

    class Present<out T> : HybridFlagResult<T>()

    data class Value<out T>(val value: T) : HybridFlagResult<T>()
}
