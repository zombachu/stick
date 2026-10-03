@file:Suppress("MagicNumber")

package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Execution
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Size
import com.zombachu.stick.SkipAsyncSuggestions
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.withAsyncContext
import kotlin.coroutines.coroutineContext

/**
 * Parameters whose [resolve][AsyncParameter.Bounded.resolve] and [suggest] may suspend. Both run on the async context
 * the command was registered with, and the command continues on its main context afterwards. `match` stays synchronous
 * because it runs on every keystroke: it should check only what is cheap, such as the shape of the tokens, and leave
 * lookups to `resolve`. During completion a ranged or unbounded `match` receives every remaining token, which may be
 * fewer than the size's minimum or more than its maximum.
 */
sealed class AsyncParameter<in E : Environment, S, T, out P : Position>(
    size: Size,
    name: String,
    description: String,
) : Parameter<E, S, T, P>(size, name, description) {

    context(inv: Invocation<E, S>)
    open suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> = []

    context(inv: Invocation<E, S>)
    final override suspend fun routeSuggest(preceding: List<String>, partial: String): List<Suggestion> {
        if (coroutineContext[SkipAsyncSuggestions] != null) return []
        return withAsyncContext { suggest(preceding, partial) }
    }

    abstract class Bounded<in E : Environment, S, T>(
        override val size: Size.Bounded,
        name: String,
        description: String,
    ) : AsyncParameter<E, S, T, Position.Leading>(size, name, description) {

        context(ex: Execution<E, S>)
        final override suspend fun parse(args: List<String>): ConsumingResult<T> = withAsyncContext { resolve(args) }

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(args: List<String>): ConsumingResult<T>
    }

    abstract class Fixed<in E : Environment, S, T>(private val arity: Int, name: String, description: String) :
        Bounded<E, S, T>(Size(arity), name, description) {

        context(inv: Invocation<E, S>)
        final override fun match(args: List<String>): MatchResult {
            if (args.size < arity) return MatchResult.partial()
            return matchArity(args)
        }

        context(inv: Invocation<E, S>)
        protected abstract fun matchArity(args: List<String>): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolve(args: List<String>): ConsumingResult<T> = resolveArity(args).consuming(arity)

        context(inv: Invocation<E, S>)
        protected abstract suspend fun resolveArity(args: List<String>): CommandResult<T>
    }

    abstract class Size1<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(1, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0])

        context(inv: Invocation<E, S>)
        abstract fun match(arg0: String): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> = resolve(args[0])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(arg0: String): CommandResult<T>
    }

    abstract class Size2<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(2, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1])

        context(inv: Invocation<E, S>)
        abstract fun match(arg0: String, arg1: String): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> = resolve(args[0], args[1])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(arg0: String, arg1: String): CommandResult<T>
    }

    abstract class Size3<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(3, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1], args[2])

        context(inv: Invocation<E, S>)
        abstract fun match(arg0: String, arg1: String, arg2: String): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }

    abstract class Size4<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(4, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult = match(args[0], args[1], args[2], args[3])

        context(inv: Invocation<E, S>)
        abstract fun match(arg0: String, arg1: String, arg2: String, arg3: String): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }

    abstract class Size5<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(5, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4])

        context(inv: Invocation<E, S>)
        abstract fun match(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
        ): CommandResult<T>
    }

    abstract class Size6<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(6, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5])

        context(inv: Invocation<E, S>)
        abstract fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
        ): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
        ): CommandResult<T>
    }

    abstract class Size7<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(7, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5], args[6])

        context(inv: Invocation<E, S>)
        abstract fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
        ): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
        ): CommandResult<T>
    }

    abstract class Size8<in E : Environment, S, T>(name: String, description: String) :
        Fixed<E, S, T>(8, name, description) {

        context(inv: Invocation<E, S>)
        final override fun matchArity(args: List<String>): MatchResult =
            match(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])

        context(inv: Invocation<E, S>)
        abstract fun match(
            arg0: String,
            arg1: String,
            arg2: String,
            arg3: String,
            arg4: String,
            arg5: String,
            arg6: String,
            arg7: String,
        ): MatchResult

        context(inv: Invocation<E, S>)
        final override suspend fun resolveArity(args: List<String>): CommandResult<T> =
            resolve(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(
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

    abstract class Unbounded<in E : Environment, S, T>(
        size: Size.Unbounded,
        name: String,
        description: String,
    ) : AsyncParameter<E, S, T, Position.Last>(size, name, description) {

        context(ex: Execution<E, S>)
        final override suspend fun parse(args: List<String>): ConsumingResult<T> = withAsyncContext { resolve(args) }

        context(inv: Invocation<E, S>)
        abstract suspend fun resolve(args: List<String>): ConsumingResult<T>
    }
}
