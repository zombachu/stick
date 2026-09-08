package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext

internal class HelperImpl<E : Environment, S, T>(private val value: ContextualValue<E, S, T>) : Helper<E, S, T> {
    override val size: Size.Bounded = Size(0)
    override val type: ElementType = ElementType.Helper

    context(validationContext: ValidationContext<E, S>)
    override fun match(args: List<String>): MatchResult = MatchResult.matched(0)

    context(inv: Invocation<E, S>)
    override fun parse(args: List<String>): CommandResult<T> {
        return inv.value()
    }
}
