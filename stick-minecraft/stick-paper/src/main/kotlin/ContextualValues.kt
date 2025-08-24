package com.zombachu.stick.paper

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.impl.SenderScope

fun <T> SenderScope<BukkitContext>.permissionedValue(
    permission: String,
    default: T,
    fallback: T
): ContextualValue<BukkitContext, T> = {
    if (senderContext.sender.hasPermission(permission)) default else fallback
}
