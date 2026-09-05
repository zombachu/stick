@file:Suppress("MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.withSize

sealed class Parameter<in E : Environment, S, out T>(
    override val size: Size,
    override val name: String,
    override val description: String,
) : Groupable<E, S, T> {

    override val type: ElementType = ElementType.Default

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${name}>"

    sealed class FixedSize<in E : Environment, S, out T>(
        override val size: Size.Fixed,
        name: String,
        description: String,
    ) : Parameter<E, S, T>(size, name, description), Groupable.Positioned<E, S, T, Position.Leading>

    abstract class Size1<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(1), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0]).withSize(size)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String): CommandResult<T>
    }

    abstract class Size2<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(2), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1]).withSize(size)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String): CommandResult<T>
    }

    abstract class Size3<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(3), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2]).withSize(size)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }

    abstract class Size4<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(4), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3]).withSize(size)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }

    abstract class Size5<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(5), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4]).withSize(size)
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): CommandResult<T>
    }

    abstract class Size6<in E : Environment, S, out T>(name: String, description: String) :
        FixedSize<E, S, T>(Size(6), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5]).withSize(size)
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
        FixedSize<E, S, T>(Size(7), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6]).withSize(size)
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
        FixedSize<E, S, T>(Size(8), name, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]).withSize(size)
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

    abstract class UnknownSize<in E : Environment, S, out T>(size: Size, name: String, description: String) :
        Parameter<E, S, T>(size, name, description), Groupable.Positioned<E, S, T, Position.Last>
}
