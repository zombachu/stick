package com.zombachu.stick.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Environment

interface VelocityEnvironment : Environment {
    val proxy: ProxyServer
}

open class BasicVelocityEnvironment(
    override val proxy: ProxyServer
) : VelocityEnvironment
