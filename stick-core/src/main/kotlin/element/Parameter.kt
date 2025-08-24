package com.zombachu.stick.element

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Size

sealed class Parameter<O, S : SenderContext, T : Any>(
    override val size: Size,
    override val id: TypedIdentifier<T>,
    override val description: String,
) : Groupable<O, S, T>, SignatureConstraint<O, S, T> {

    override val type: ElementType = ElementType.Default

    override fun getSyntax(senderContext: S): String = "<${id.name}>"

    sealed class FixedSize<O, S : SenderContext, T : Any>(
        override val size: Size.Fixed,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<O, S, T>(size, id, description), SignatureConstraint.NonTerminating<O, S, T>
    abstract class Size1<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(1), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String): Result<out T>
    }
    abstract class Size2<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(2), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String): Result<out T>
    }
    abstract class Size3<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(3), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String): Result<out T>
    }
    abstract class Size4<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(4), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2], args[3])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String,
                           arg3: String): Result<out T>
    }
    abstract class Size5<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(5), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2], args[3], args[4])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String, arg3: String,
                           arg4: String): Result<out T>
    }
    abstract class Size6<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(6), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2], args[3], args[4], args[5])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String, arg3: String,
                           arg4: String, arg5: String): Result<out T>
    }
    abstract class Size7<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(7), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String, arg3: String,
                           arg4: String, arg5: String, arg6: String): Result<out T>
    }
    abstract class Size8<O, S : SenderContext, T : Any>(id: TypedIdentifier<T>, description: String) :
        FixedSize<O, S, T>(Size.Companion(8), id, description) {
        final override fun parse(context: ExecutionContext<O, S>, args: List<String>): Result<out T> {
            return parse(context, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }
        abstract fun parse(context: ExecutionContext<O, S>, arg0: String, arg1: String, arg2: String, arg3: String,
                           arg4: String, arg5: String, arg6: String, arg7: String): Result<out T>
    }

    abstract class UnknownSize<O, S : SenderContext, T : Any>(
        size: Size,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<O, S, T>(size, id, description), SignatureConstraint.Terminating<O, S, T>
}
