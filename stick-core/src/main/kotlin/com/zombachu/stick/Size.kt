package com.zombachu.stick

sealed class Size(internal val parsingPriority: Int) {

    abstract val min: Int

    abstract fun matches(size: Int): Boolean

    operator fun plus(other: Size): Size {
        return if (this is Bounded && other is Bounded) {
            between(min + other.min, max + other.max)
        } else {
            atLeast(min + other.min)
        }
    }

    class Bounded internal constructor(override val min: Int, val max: Int) : Size(parsingPriority = 0) {
        override fun matches(size: Int) = size in min..max

        operator fun plus(other: Bounded): Bounded {
            return between(min + other.min, max + other.max)
        }
    }

    class Unbounded internal constructor(override val min: Int) : Size(parsingPriority = 1) {
        override fun matches(size: Int) = size >= min
    }

    companion object {
        operator fun invoke(size: Int): Bounded {
            require(size >= 0)
            return Bounded(size, size)
        }

        fun between(min: Int, max: Int): Bounded {
            require(min >= 0)
            require(max >= min)
            return Bounded(min, max)
        }

        fun atLeast(min: Int): Unbounded {
            require(min >= 0)
            return Unbounded(min)
        }
    }
}
