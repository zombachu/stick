package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.Size

sealed class Parameter<E : Environment, S, T>(
    override val size: Size,
    override val id: TypedIdentifier<T>,
    override val description: String,
) : Groupable<E, S, T>, SignatureConstraint<E, S, T> {

    override val type: ElementType = ElementType.Default

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = "<${id.name}>"

    sealed class FixedSize<E : Environment, S, T>(
        override val size: Size.Fixed,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, S, T>(size, id, description), SignatureConstraint.NonTerminating<E, S, T>
    abstract class Size1<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(1), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String): CommandResult<T>
    }
    abstract class Size2<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(2), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String): CommandResult<T>
    }
    abstract class Size3<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(3), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String): CommandResult<T>
    }
    abstract class Size4<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(4), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String): CommandResult<T>
    }
    abstract class Size5<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(5), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): CommandResult<T>
    }
    abstract class Size6<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(6), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String,
                           arg5: String): CommandResult<T>
    }
    abstract class Size7<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(7), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String): CommandResult<T>
    }
    abstract class Size8<E : Environment, S, T>(id: TypedIdentifier<T>, description: String) :
        FixedSize<E, S, T>(Size(8), id, description) {

        context(inv: Invocation<E, S>)
        override fun parse(args: List<String>): CommandResult<T> {
            return parse(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }

        context(inv: Invocation<E, S>)
        abstract fun parse(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
                           arg6: String, arg7: String): CommandResult<T>
    }

    abstract class UnknownSize<E : Environment, S, T>(
        size: Size,
        id: TypedIdentifier<T>,
        description: String,
    ) : Parameter<E, S, T>(size, id, description), SignatureConstraint.Terminating<E, S, T>
}

internal class TransformedParameter<E : Environment, S : Any, S2 : Any, T>(
    val base: Parameter<E, S2, T>,
    val transform: (S) -> S2,
    val requirement: Requirement<E, S>,
) : ValidatedParameter<E, S, T>, SenderValidator<E, S> {

    override val size: Size = base.size
    override val type: ElementType = base.type
    override val id: TypedIdentifier<T> = base.id
    override val description: String = base.description

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        val transformedInvocation = (inv as InvocationImpl).forSender(transform)
        context(transformedInvocation) {
            return base.parse(args)
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String {
        val transformedValidationContext = validationContext.forSender(transform)
        context(transformedValidationContext) {
            return base.getSyntax()
        }
    }

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}
