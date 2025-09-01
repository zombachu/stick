package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.impl.Size

internal class HelperImpl<E : Environment, S, T : Any>(
    val value: ContextualValue<E, S, T>,
) : Helper<E, S, T> {
    override val size: Size = Size(0)
    override val type: ElementType = ElementType.Helper
}
