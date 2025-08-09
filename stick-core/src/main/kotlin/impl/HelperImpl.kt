package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue

interface Helper<S, T : Any> : SignatureConstraint.NonTerminating<S, T>

internal class HelperImpl<S, T : Any>(
    val value: ContextualValue<S, T>,
) : Helper<S, T> {
    override val size: Size = Size(0)
    override val type: ElementType = ElementType.Helper
}
