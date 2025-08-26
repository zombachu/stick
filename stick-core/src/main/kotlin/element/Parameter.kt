package com.zombachu.stick.element

import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed class Parameter<S : SenderContext, O, T : Any>(
    override val size: Size,
    override val id: TypedIdentifier<T>,
    override val description: String,
) : Groupable<S, O, T>, SignatureConstraint<S, O, T> {

    override val type: ElementType = ElementType.Default

    context(senderContext: S)
    override fun getSyntax(): String = "<${id.name}>"

    sealed class FixedSize<S : SenderContext, O, T : Any>(
        override val size: Size.Fixed,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<S, O, T>(size, id, description), SignatureConstraint.NonTerminating<S, O, T>
    abstract class Size1<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(1), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String): Result<out T>
    }
    abstract class Size2<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(2), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String): Result<out T>
    }
    abstract class Size3<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(3), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): Result<out T>
    }
    abstract class Size4<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(4), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): Result<out T>
    }
    abstract class Size5<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(5), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Result<out T>
    }
    abstract class Size6<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(6), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String,
                           arg5: String): Result<out T>
    }
    abstract class Size7<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(7), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String): Result<out T>
    }
    abstract class Size8<S : SenderContext, O, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<S, O, T>(Size.Companion(8), id, description) {

        context(senderContext: S, invocation: Invocation<S, O>)
        override fun parse(args: List<String>): Result<out T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }

        context(senderContext: S, invocation: Invocation<S, O>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String, arg7: String): Result<out T>
    }

    abstract class UnknownSize<S : SenderContext, O, T : Any>(
        size: Size,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<S, O, T>(size, id, description), SignatureConstraint.Terminating<S, O, T>
}
