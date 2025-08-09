package com.zombachu.stick.impl

import com.zombachu.stick.ContextualValue

class HelperElement<S, T : Any>(
    val value: ContextualValue<S, T>,
) : SignatureConstraint.NonTerminating<S, T> {
    override val size: Size = Size(0)
    override val type: ElementType = ElementType.Helper
}
