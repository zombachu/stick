package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.PeekingResult
import com.zombachu.stick.Size
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.element.Parameter

open class TextParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Unbounded<E, S, String>(Size.atLeast(1), name, description) {

    override val type: ElementType = ElementType.Passthrough

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<String> {
        if (args.isEmpty()) return PeekingResult.failSize()
        return ParsingResult.success(args.joinToString(" "), args.size)
    }
}
