@file:Suppress("MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.toMatchResult
import com.zombachu.stick.toMatchResultIn
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
    ) : Parameter<E, S, T, Position.Leading>(size, name, description) {

        context(inv: Invocation<E, S>)
        final override fun parse(args: List<String>): CommandResult<T> = resolve(args)

        context(validationContext: ValidationContext<E, S>)
        override fun match(args: List<String>): MatchResult = resolve(args).toMatchResultIn(args)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(args: List<String>): CommandResult<T>
    }

    abstract class Size1<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(1), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.isEmpty()) return MatchResult.partial(args.size)
            return match(args[0])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String): MatchResult = resolve(arg0).toMatchResult(1)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> = resolve(args[0]).withConsumed(1)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String): CommandResult<T>
    }

    abstract class Size2<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(2), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 2) return MatchResult.partial(args.size)
            return match(args[0], args[1])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String): MatchResult = resolve(arg0, arg1).toMatchResult(2)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> = resolve(args[0], args[1]).withConsumed(2)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String): CommandResult<T>
    }

    abstract class Size3<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(3), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 3) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String): MatchResult =
            resolve(arg0, arg1, arg2).toMatchResult(3)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2]).withConsumed(3)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }

    abstract class Size4<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(4), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 4) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2], args[3])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String, arg3: String): MatchResult =
            resolve(arg0, arg1, arg2, arg3).toMatchResult(4)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3]).withConsumed(4)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }

    abstract class Size5<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(5), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 5) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2], args[3], args[4])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): MatchResult =
            resolve(arg0, arg1, arg2, arg3, arg4).toMatchResult(5)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4]).withConsumed(5)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): CommandResult<T>
    }

    abstract class Size6<in E : Environment, S, out T>(name: String, description: String) :
        Bounded<E, S, T>(Size(6), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 6) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2], args[3], args[4], args[5])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5).toMatchResult(6)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5]).withConsumed(6)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(
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

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 7) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5, arg6).toMatchResult(7)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6]).withConsumed(7)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(
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

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < 8) return MatchResult.partial(args.size)
            return match(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }

        context(validationContext: ValidationContext<E, S>)
        open fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
            arg7: String,
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7).toMatchResult(8)

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]).withConsumed(8)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(
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
    ) : Parameter<E, S, T, Position.Last>(size, name, description) {

        context(inv: Invocation<E, S>)
        final override fun parse(args: List<String>): CommandResult<T> = resolve(args)

        context(validationContext: ValidationContext<E, S>)
        override fun match(args: List<String>): MatchResult = resolve(args).toMatchResultIn(args)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(args: List<String>): CommandResult<T>
    }
}
