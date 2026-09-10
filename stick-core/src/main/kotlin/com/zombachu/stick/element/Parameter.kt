@file:Suppress("MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.InvocationImpl
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.toMatchResult

sealed class Parameter<in E : Environment, S, out T, out P : Position>(
    override val size: Size,
    override val name: String,
    override val description: String,
) : Groupable.Positioned<E, S, T, P>, ConsumingElement<E, S, T> {

    override val type: GroupableType = GroupableType.Default

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${name}>"

    abstract class Bounded<in E : Environment, S, out T>(
        override val size: Size.Bounded,
        name: String,
        description: String,
    ) : Parameter<E, S, T, Position.Leading>(size, name, description) {

        context(inv: Invocation<E, S>)
        final override fun parse(args: List<String>): ConsumingResult<T> {
            val matched = (inv as InvocationImpl).currentMatch
            if (matched != null && matched.resolvedBy === this) {
                @Suppress("UNCHECKED_CAST")
                return ParsingResult.success(matched.resolved as T).consuming(matched.consumed)
            }
            return resolve(args)
        }

        context(validationContext: ValidationContext<E, S>)
        override fun match(args: List<String>): MatchResult = resolve(args).toMatchResult(args.size, this)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(args: List<String>): ConsumingResult<T>
    }

    abstract class Fixed<in E : Environment, S, out T>(internal val arity: Int, name: String, description: String) :
        Bounded<E, S, T>(Size(arity), name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < arity) return MatchResult.partial(args.size)
            return matchArity(args)
        }

        context(validationContext: ValidationContext<E, S>)
        protected abstract fun matchArity(args: List<String>): MatchResult

        context(validationContext: ValidationContext<E, S>)
        final override fun resolve(args: List<String>): ConsumingResult<T> = resolveArity(args).consuming(arity)

        context(validationContext: ValidationContext<E, S>)
        protected abstract fun resolveArity(args: List<String>): CommandResult<T>

        internal fun CommandResult<*>.toArityMatchResult(): MatchResult =
            this.consuming(arity).toMatchResult(arity, this@Fixed)
    }

    abstract class Size1<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(1, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0])

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String): MatchResult = resolve(arg0).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> = resolve(args[0])

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String): CommandResult<T>
    }

    abstract class Size2<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(2, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1])

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String): MatchResult = resolve(arg0, arg1).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> = resolve(args[0], args[1])

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String): CommandResult<T>
    }

    abstract class Size3<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(3, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1], args[2])

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String): MatchResult =
            resolve(arg0, arg1, arg2).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> = resolve(args[0], args[1], args[2])

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }

    abstract class Size4<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(4, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1], args[2], args[3])

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String, arg3: String): MatchResult =
            resolve(arg0, arg1, arg2, arg3).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3])

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }

    abstract class Size5<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(5, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4])

        context(validationContext: ValidationContext<E, S>)
        open fun match(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): MatchResult =
            resolve(arg0, arg1, arg2, arg3, arg4).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4])

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): CommandResult<T>
    }

    abstract class Size6<in E : Environment, S, out T>(name: String, description: String) :
        Fixed<E, S, T>(6, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5])

        context(validationContext: ValidationContext<E, S>)
        open fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5])

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
        Fixed<E, S, T>(7, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5], args[6])

        context(validationContext: ValidationContext<E, S>)
        open fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5, arg6).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6])

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
        Fixed<E, S, T>(8, name, description) {

        context(validationContext: ValidationContext<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])

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
        ): MatchResult = resolve(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7).toArityMatchResult()

        context(validationContext: ValidationContext<E, S>)
        final override fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])

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
        final override fun parse(args: List<String>): ConsumingResult<T> {
            val matched = (inv as InvocationImpl).currentMatch
            if (matched != null && matched.resolvedBy === this) {
                @Suppress("UNCHECKED_CAST")
                return ParsingResult.success(matched.resolved as T).consuming(matched.consumed)
            }
            return resolve(args)
        }

        context(validationContext: ValidationContext<E, S>)
        override fun match(args: List<String>): MatchResult = resolve(args).toMatchResult(args.size, this)

        context(validationContext: ValidationContext<E, S>)
        abstract fun resolve(args: List<String>): ConsumingResult<T>
    }
}
