package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.plugin.PluginManager
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.config.ProxyConfig
import com.velocitypowered.api.proxy.messages.ChannelRegistrar
import com.velocitypowered.api.proxy.player.ResourcePackInfo
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerInfo
import com.velocitypowered.api.scheduler.Scheduler
import com.velocitypowered.api.util.ProxyVersion
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.net.InetSocketAddress
import java.util.*

class BridgeTest {

    fun test() {
        val env = VelocityEnvironmentImpl(fakeProxyServer)
        val failureHandler = VelocityFailureHandler()
        VelocityCommandBridge(Any(), fakeProxyServer).withContext(env, failureHandler) {
            register {
                command("hi")(
                    stringParameter("message")
                ) { message ->
                    sender.sendMessage(PlainTextComponentSerializer.plainText().deserialize(message))
                    ExecutionResult.success()
                }
            }
        }
    }

}

val fakeProxyServer = object : ProxyServer {
    override fun shutdown(reason: Component?) { TODO() }

    override fun shutdown() { TODO() }

    override fun isShuttingDown(): Boolean { TODO() }

    override fun closeListeners() { TODO() }

    override fun getPlayer(username: String?): Optional<Player?>? { TODO() }

    override fun getPlayer(uuid: UUID?): Optional<Player?>? { TODO() }

    override fun getAllPlayers(): Collection<Player?>? { TODO() }

    override fun getPlayerCount(): Int { TODO() }

    override fun getServer(name: String?): Optional<RegisteredServer?>? { TODO() }

    override fun getAllServers(): Collection<RegisteredServer?>? { TODO() }

    override fun matchPlayer(partialName: String?): Collection<Player?>? { TODO() }

    override fun matchServer(partialName: String?): Collection<RegisteredServer?>? { TODO() }

    override fun createRawRegisteredServer(server: ServerInfo?): RegisteredServer? { TODO() }

    override fun registerServer(server: ServerInfo?): RegisteredServer? { TODO() }

    override fun unregisterServer(server: ServerInfo?) { TODO() }

    override fun getConsoleCommandSource(): ConsoleCommandSource? { TODO() }

    override fun getPluginManager(): PluginManager? { TODO() }

    override fun getEventManager(): EventManager? { TODO() }

    override fun getCommandManager(): CommandManager? { TODO() }

    override fun getScheduler(): Scheduler? { TODO() }

    override fun getChannelRegistrar(): ChannelRegistrar? { TODO() }

    override fun getBoundAddress(): InetSocketAddress? { TODO() }

    override fun getConfiguration(): ProxyConfig? { TODO() }

    override fun getVersion(): ProxyVersion? { TODO() }

    override fun createResourcePackBuilder(url: String?): ResourcePackInfo.Builder? { TODO() }

}
