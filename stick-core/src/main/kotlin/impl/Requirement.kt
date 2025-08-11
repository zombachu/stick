package com.zombachu.stick.impl

fun interface Requirement<S> {
    operator fun invoke(sender: S): Boolean

    operator fun plus(other: Requirement<S>): Requirement<S> {
        return Requirement {
                sender -> this(sender) && other(sender)
        }
    }
}
