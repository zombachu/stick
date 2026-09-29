package com.zombachu.stick.element

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.noMatch
import com.zombachu.stick.success

internal class StructureImpl<E : Environment, S, T_ : Arguments>
private constructor(
    literal: LabelParameter<E, S>,
    signature: (LabelParameter<E, S>) -> Signature<E, S, T_>,
) : BranchImpl<E, S, T_>(signature(literal)), Structure<E, S, T_> {

    constructor(
        name: String,
        aliases: Set<String>,
        description: String,
        signature: (Parameter<E, S, *, *>) -> Signature<E, S, T_>,
    ) : this(LabelParameter(name, aliases, description), signature)

    override val label: String = literal.label
    override val aliases: Set<String> = literal.aliases
}

private class LabelParameter<E : Environment, S>(name: String, aliases: Set<String>, description: String) :
    LiteralParameter<E, S>(name, aliases, description) {

    context(inv: Invocation<E, S>)
    override fun getSyntax(): String = name

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<String> =
        if (matches(arg0.lowercase())) success(arg0) else noMatch()
}
