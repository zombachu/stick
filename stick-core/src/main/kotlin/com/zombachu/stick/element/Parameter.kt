@file:Suppress("MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.withConsumed

sealed class Parameter<in E : Environment, S, out T, out P : Position>(
    override val size: Size,
    override val name: String,
    override val description: String,
) : Groupable.Positioned<E, S, T, P> {

    override val type: ElementType = ElementType.Default

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${name}>"

    abstract class Bounded<in E : Environment, S, out T>(
        override val size: Size.Bounded,
        name: String,
        description: String,
    ) : Parameter<E, S, T, Position.Leading>(size, name, description)

    abstract class Size1<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(1), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0]).withConsumed(1)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String): CommandResult<T>
    }

    abstract class Size2<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(2), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1]).withConsumed(2)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String): CommandResult<T>
    }

    abstract class Size3<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(3), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2]).withConsumed(3)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }

    abstract class Size4<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(4), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3]).withConsumed(4)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }

    abstract class Size5<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(5), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4]).withConsumed(5)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): CommandResult<T>
    }

    abstract class Size6<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(6), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5]).withConsumed(6)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
        ): CommandResult<T>
    }

    abstract class Size7<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(7), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6]).withConsumed(7)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
        ): CommandResult<T>
    }

    abstract class Size8<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(8), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]).withConsumed(8)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
            arg7: String,
        ): CommandResult<T>
    }

    abstract class Unbounded<in E : Environment, S, out T>(
        size: Size.Unbounded,
        name: String,
        description: String,
    ) : Parameter<E, S, T, Position.Last>(size, name, description)
}
