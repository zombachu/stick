package com.zombachu.stick.element

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed class Parameter<E : Environment, O, T : Any>(
    override val size: Size,
    override val id: TypedIdentifier<T>,
    override val description: String,
) : Groupable<E, O, T>, SignatureConstraint<E, O, T> {

    override val type: ElementType = ElementType.Default

    context(env: E)
    override fun getSyntax(): String = "<${id.name}>"

    sealed class FixedSize<E : Environment, O, T : Any>(
        override val size: Size.Fixed,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, O, T>(size, id, description), SignatureConstraint.NonTerminating<E, O, T>
    abstract class Size1<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(1), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String): Result<out T>
    }
    abstract class Size2<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(2), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String): Result<out T>
    }
    abstract class Size3<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(3), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): Result<out T>
    }
    abstract class Size4<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(4), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): Result<out T>
    }
    abstract class Size5<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(5), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Result<out T>
    }
    abstract class Size6<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(6), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String,
                           arg5: String): Result<out T>
    }
    abstract class Size7<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(7), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String): Result<out T>
    }
    abstract class Size8<E : Environment, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, O, T>(Size.Companion(8), id, description) {

        context(env: E, inv: Invocation<E, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }

        context(env: E, inv: Invocation<E, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String, arg7: String): Result<out T>
    }

    abstract class UnknownSize<E : Environment, O, T : Any>(
        size: Size,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, O, T>(size, id, description), SignatureConstraint.Terminating<E, O, T>
}
