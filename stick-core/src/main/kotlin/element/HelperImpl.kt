package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.impl.Size

internal class HelperImpl<E : Environment, O, T : Any>(
    val value: ContextualValue<E, O, T>,
) : Helper<E, O, T> {
    override val size: Size = Size.Companion(0)
    override val type: ElementType = ElementType.Helper
}
