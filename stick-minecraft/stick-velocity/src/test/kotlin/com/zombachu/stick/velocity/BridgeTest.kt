package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandSource
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
import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.net.InetSocketAddress
import java.util.*

class BridgeTest {

    fun test() {
        val env = BasicVelocityEnvironment(fakeProxyServer)
        val failureHandler = BasicVelocityFailureHandler()
        val bridge = VelocityStick(Any(), fakeProxyServer)
        bridge.withContext(env, failureHandler) {
            register {
                command("hi")(
                    stringParameter("message")
                ) { message ->
                    sender.sendMessage(PlainTextComponentSerializer.plainText().deserialize(message))
                }
            }
        }

        val wrapperFailureHandler = WrapperFailureHandler()
        bridge.withContext(
            env,
            wrapperFailureHandler,
            { SourceWrapper(it) },
            { SenderValidationResult.success() }
        ) {

            register(WrapperCommand())
            register(Wrapper2Command())
            register(UnderCommand())
//            register(WrongSenderCommand()) Shouldn't compile
//            register(WrongEnvironmentCommand()) // Shouldn't compile
        }
    }
}

class WrapperFailureHandler : FailureHandler<VelocityEnvironment, SourceWrapper> {
    context(inv: Invocation<VelocityEnvironment, SourceWrapper>)
    override fun onFailure(failure: CommandResult.Failure<*>) {
        TODO("Not yet implemented")
    }
}

class WrongEnvironmentCommand : Command<DisjointEnvironment, SourceWrapper> {
    override val structure: StructureElement<DisjointEnvironment, SourceWrapper, Structure<DisjointEnvironment, SourceWrapper>> = command("hi")(stringParameter("hey"))
}

class WrapperCommand : VelocityCommand<SourceWrapper> {
    override val structure = command("hi")(stringParameter("hey"))
}

class Wrapper2Command : VelocityCommand<Source2Wrapper> {
    override val structure = command("hi")(stringParameter("hey"))
}

class UnderCommand : Command<Environment, SourceWrapper> {
    override val structure = command("hi")(stringParameter("hey"))
}

class WrongSenderCommand : Command<VelocityEnvironment, Int> {
    override val structure = command("hi")(stringParameter("hey"))
}


open class SourceWrapper(val source: CommandSource)

class Source2Wrapper(source: CommandSource) : SourceWrapper(source)

class DisjointEnvironment : Environment

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
