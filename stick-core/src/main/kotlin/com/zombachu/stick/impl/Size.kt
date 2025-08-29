package com.zombachu.stick.impl

sealed class Size(internal val parsingPriority: Int) {
    class Fixed internal constructor(val size: Int): Size(parsingPriority = 0) {
        operator fun plus(other: Fixed): Fixed {
            return Fixed(this.size + other.size)
        }

        override fun matches(size: Int) = this.size == size
    }
    class Unbounded internal constructor(): Size(parsingPriority = 1) {
        override fun matches(size: Int) = true
    }
    class Deferred internal constructor(): Size(parsingPriority = 2) {
        override fun matches(size: Int) = true
    }

    operator fun plus(other: Size): Size {
        return if (this is Deferred || other is Deferred) {
            Deferred
        } else {
            Unbounded
        }
    }

    abstract fun matches(size: Int): Boolean

    companion object {
        val Unbounded = Unbounded()
        val Deferred = Deferred()

        operator fun invoke(size: Int): Fixed {
            return Fixed(size)
        }
    }
}
