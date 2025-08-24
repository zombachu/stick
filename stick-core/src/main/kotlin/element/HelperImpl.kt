package com.zombachu.stick.element

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.SenderContext
import com.zombachu.stick.impl.Size

internal class HelperImpl<S : SenderContext, T : Any>(
    val value: ContextualValue<S, T>,
) : Helper<S, T> {
    override val size: Size = Size.Companion(0)
    override val type: ElementType = ElementType.Helper
}
