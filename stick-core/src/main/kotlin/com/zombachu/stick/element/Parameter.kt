package com.zombachu.stick.element

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Size

sealed class Parameter<E : Environment, S, T : Any>(
    override val size: Size,
    override val id: TypedIdentifier<T>,
    override val description: String,
) : Groupable<E, S, T>, SignatureConstraint<E, S, T> {

    override val type: ElementType = ElementType.Default

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${id.name}>"

    sealed class FixedSize<E : Environment, S, T : Any>(
        override val size: Size.Fixed,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, S, T>(size, id, description), SignatureConstraint.NonTerminating<E, S, T>
    abstract class Size1<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(1), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String): Result<T>
    }
    abstract class Size2<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(2), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String): Result<T>
    }
    abstract class Size3<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(3), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): Result<T>
    }
    abstract class Size4<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(4), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2], args[3])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): Result<T>
    }
    abstract class Size5<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(5), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2], args[3], args[4])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Result<T>
    }
    abstract class Size6<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(6), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String,
                           arg5: String): Result<T>
    }
    abstract class Size7<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(7), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String): Result<T>
    }
    abstract class Size8<E : Environment, S, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size.Companion(8), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): Result<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String, arg7: String): Result<T>
    }

    abstract class UnknownSize<E : Environment, S, T : Any>(
        size: Size,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, S, T>(size, id, description), SignatureConstraint.Terminating<E, S, T>
}
