package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.LiteralParameter

internal class StructureImpl<E : Environment, S, T_ : Arguments>
private constructor(
    literal: LabelParameter<E, S>,
    private val requirement: Requirement<E, S>,
    signature: (LabelParameter<E, S>) -> Signature<E, S, T_>,
) : BranchImpl<E, S, T_>(literal, signature(literal)), Structure<E, S, T_> {

    constructor(
        name: String,
        aliases: Set<String>,
        description: String,
        requirement: Requirement<E, S>,
        signature: (SignatureElement<E, S, Any?, *>) -> Signature<E, S, T_>,
    ) : this(LabelParameter(name, aliases, description), requirement, signature)

    override val label: String = literal.label
    override val aliases: Set<String> = literal.aliases

    context(validationContext: ValidationContext<E, S>)
    override fun validateSender(): CommandResult<Unit> = requirement.validateSender()
}

private class LabelParameter<E : Environment, S>(name: String, aliases: Set<String>, description: String) :
    LiteralParameter<E, S>(name, aliases, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun getSyntax(): String = name

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<String> =
        if (matches(arg0.lowercase())) ParsingResult.success(arg0) else ParsingResult.failTypeInternal()
}
