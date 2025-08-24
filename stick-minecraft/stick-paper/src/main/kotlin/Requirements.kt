package com.zombachu.stick.paper

import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.structure.requirement

// TODO: Permission message
fun SenderScope<BukkitContext>.permission(permission: String): Requirement<BukkitContext> = requirement {
    it.sender.hasPermission(permission)
}
