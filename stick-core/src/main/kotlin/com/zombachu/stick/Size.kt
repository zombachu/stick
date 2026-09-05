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

    sealed class Bounded(parsingPriority: Int) : Size(parsingPriority) {
        abstract val max: Int

        operator fun plus(other: Bounded): Bounded {
            return between(min + other.min, max + other.max)
        }
    }

    class Fixed internal constructor(val size: Int) : Bounded(parsingPriority = 0) {
        override val min: Int = size
        override val max: Int = size

        operator fun plus(other: Fixed): Fixed {
            return Fixed(this.size + other.size)
        }

        override fun matches(size: Int) = this.size == size
    }

    class Variable internal constructor(override val min: Int, override val max: Int) : Bounded(parsingPriority = 1) {
        override fun matches(size: Int) = size in min..max
    }

    class Unbounded internal constructor(override val min: Int) : Size(parsingPriority = 2) {
        override fun matches(size: Int) = size >= min
    }

    companion object {
        operator fun invoke(size: Int): Fixed {
            require(size >= 0)
            return Fixed(size)
        }

        fun between(min: Int, max: Int): Bounded {
            require(min >= 0)
            require(max >= min)
            return if (min == max) Fixed(min) else Variable(min, max)
        }

        fun atLeast(min: Int): Unbounded {
            require(min >= 0)
            return Unbounded(min)
        }
    }
}
