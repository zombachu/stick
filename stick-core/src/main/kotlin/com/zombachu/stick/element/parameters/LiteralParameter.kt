package com.zombachu.stick.element.parameters

import com.zombachu.stick.Aliasable
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.Suggestion
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.failLiteral
import com.zombachu.stick.success
import com.zombachu.stick.suggestAliases

open class LiteralParameter<E : Environment, S>(name: String, override val aliases: Set<String>, description: String) :
    Parameter.Size1<E, S, String>(name, description), Aliasable {

    override val label: String = name.lowercase()
    override val type: GroupableType = GroupableType.Literal

    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> = suggestAliases()

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<String> {
        if (!matches(arg0.lowercase())) {
            return failLiteral([label], arg0)
        }
        return success(arg0)
    }
}
