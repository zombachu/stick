package com.zombachu.stick

sealed class HybridFlagResult<T> {
    class Absent<T> : HybridFlagResult<T>()
    class Present<T> : HybridFlagResult<T>()
    class Value<T>(val value: T) : HybridFlagResult<T>()
}
