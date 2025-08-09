package com.zombachu.stick.impl

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.id

typealias StructureElement<S, T> = StructureContext<S>.() -> T

class StructureContext<S>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureContext<*>?,
    internal val requirement: Requirement<S>,
): SenderScope<S> {

    val root: StructureContext<*> = parent?.root ?: this

    fun <S2> forSender(): StructureContext<S2> {
        return StructureContext(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // To get to this point they must have already passed the previous requirement so should be safe to set to true
            requirement = { true }
        )
    }

    internal fun build(signature: Signature<S>): Structure<S> {
        return Structure(
            id(this.name),
            this.aliases,
            this.description,
            { this.requirement(it) },
            signature
        )
    }

    companion object {
        fun <S> empty(): StructureContext<S> = StructureContext(
            name = "",
            aliases = setOf(),
            description = "",
            parent = null,
            requirement = { true }
        )
    }
}

open class Structure<S> internal constructor(
    override val id: TypedIdentifier<Unit>,
    override val aliases: Set<String>,
    override val description: String,
    override val validate: (S) -> Boolean,
    internal val signature: Signature<S>,
) : Groupable<S, Unit>, SignatureConstraint.Terminating<S, Unit>, Validator<S>, Aliasable {

    override val label by id
    override val size: Size = Size.Deferred
    override val type: ElementType = ElementType.Literal

    override fun parse(context: ExecutionContext<S>, args: List<String>): ParsingResult<Unit> {
        val peeked = context.peek(Size(1))
        if (peeked !is PeekingResult.Success) {
            return ParsingResult.failType()
        }

        val label = peeked.args.first().lowercase()
        if (!matches(label)) {
            return ParsingResult.failType()
        }
        peeked.consume()

        if (!validate(context.sender)) {
            return ParsingResult.failSyntax()
        }

        val result = signature.execute(context)
        when (result) {
            is Result.Success -> return ParsingResult.success()
            is ParsingResult.Failure<*> -> return result.cast()
            is ExecutionResult.Failure -> return result.toParsingResult()
        }
    }

    override fun getSyntax(sender: S): String {
        val signatureSyntax = signature.getSyntax(sender)
        return if (signatureSyntax.isEmpty()) {
            id.name
        } else {
            "${id.name} $signatureSyntax"
        }
    }
}
