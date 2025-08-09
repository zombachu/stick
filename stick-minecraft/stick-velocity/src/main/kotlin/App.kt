package com.zombachu.stick.velocity

import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.someFunction
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun broadcastMessage(proxy: ProxyServer) {
    proxy.allPlayers.forEach {
        it.sendMessage(LegacyComponentSerializer.legacySection().deserialize(someFunction()))
    }
}
