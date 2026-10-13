package com.zombachu.stick.velocity

import com.mojang.brigadier.tree.CommandNode
import com.velocitypowered.api.command.BrigadierCommand
import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.velocitypowered.api.event.EventHandler
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.config.ProxyConfig
import com.velocitypowered.api.proxy.messages.ChannelRegistrar
import com.velocitypowered.api.proxy.player.ResourcePackInfo
import com.velocitypowered.api.proxy.server.RegisteredServer
import com.velocitypowered.api.proxy.server.ServerInfo
import com.velocitypowered.api.util.ProxyVersion
import com.zombachu.stick.Arguments
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import net.kyori.adventure.text.Component
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.LegacyAbstractLogger
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.CompletableFuture

class FakeCommandSource(private val permissions: Set<String> = []) : CommandSource {
    val sentMessages: MutableList<Component> = mutableListOf()

    override fun getPermissionValue(permission: String): Tristate =
        if (permission in permissions) Tristate.TRUE else Tristate.FALSE

    override fun sendMessage(message: Component) {
        sentMessages += message
    }
}

class FakeInvocation(private val src: CommandSource, private val aliasValue: String, private val argsValue: String) :
    RawCommand.Invocation {
    override fun source(): CommandSource = src

    override fun arguments(): String = argsValue

    override fun alias(): String = aliasValue
}

class FakeCommandMeta(private val aliasesValue: Collection<String>, private val pluginValue: Any) : CommandMeta {
    override fun getAliases(): Collection<String> = aliasesValue

    override fun getHints() = emptyList<Nothing>()

    override fun getPlugin(): Any = pluginValue
}

class FakeCommandMetaBuilder(private val primaryAlias: String) : CommandMeta.Builder {
    private var aliasesValue: List<String> = [primaryAlias]
    private var pluginValue: Any? = null

    override fun aliases(vararg aliases: String): CommandMeta.Builder {
        aliasesValue = [primaryAlias] + aliases.toList()
        return this
    }

    override fun hint(node: CommandNode<CommandSource>): CommandMeta.Builder = this

    override fun plugin(plugin: Any): CommandMeta.Builder {
        pluginValue = plugin
        return this
    }

    override fun build(): CommandMeta = FakeCommandMeta(aliasesValue, pluginValue!!)
}

@Suppress("OVERRIDE_DEPRECATION")
class FakeCommandManager : CommandManager {
    var registeredMeta: CommandMeta? = null
        private set

    var registeredCommand: Any? = null
        private set

    var registerCalls: Int = 0
        private set

    override fun metaBuilder(alias: String): CommandMeta.Builder = FakeCommandMetaBuilder(alias)

    override fun metaBuilder(command: BrigadierCommand): CommandMeta.Builder = error("unused")

    override fun register(command: BrigadierCommand) = error("unused")

    override fun register(meta: CommandMeta, command: Command) {
        registerCalls++
        registeredMeta = meta
        registeredCommand = command
    }

    override fun unregister(alias: String) = error("unused")

    override fun unregister(meta: CommandMeta) = error("unused")

    override fun getCommandMeta(alias: String): CommandMeta = error("unused")

    override fun executeAsync(source: CommandSource, cmdLine: String) = error("unused")

    override fun executeImmediatelyAsync(source: CommandSource, cmdLine: String) = error("unused")

    override fun offerSuggestions(source: CommandSource, cmdLine: String) = error("unused")

    override fun offerBrigadierSuggestions(source: CommandSource, cmdLine: String) = error("unused")

    override fun getAliases(): Collection<String> = error("unused")

    override fun hasCommand(alias: String): Boolean = error("unused")

    override fun hasCommand(alias: String, source: CommandSource): Boolean = error("unused")
}

class FakeEventManager : EventManager {
    val handlers: MutableList<Pair<Class<*>, EventHandler<*>>> = mutableListOf()

    override fun register(plugin: Any, listener: Any) = error("unused")

    override fun <E> register(plugin: Any, eventClass: Class<E>, postOrder: PostOrder, handler: EventHandler<E>) {
        handlers += eventClass to handler
    }

    override fun <E> register(plugin: Any, eventClass: Class<E>, postOrder: Short, handler: EventHandler<E>) =
        error("unused")

    override fun <E> fire(event: E): CompletableFuture<E> {
        for ((eventClass, handler) in handlers) {
            @Suppress("UNCHECKED_CAST")
            if (eventClass.isInstance(event)) (handler as EventHandler<E>).execute(event)
        }
        return CompletableFuture.completedFuture(event)
    }

    override fun unregisterListeners(plugin: Any) = error("unused")

    override fun unregisterListener(plugin: Any, listener: Any) = error("unused")

    override fun <E> unregister(plugin: Any, handler: EventHandler<E>) = error("unused")
}

class FakeProxyServer(
    private val manager: CommandManager = FakeCommandManager(),
    private val events: EventManager = FakeEventManager(),
) : ProxyServer {
    override fun shutdown(reason: Component?) = error("unused")

    override fun shutdown() = error("unused")

    override fun isShuttingDown(): Boolean = error("unused")

    override fun closeListeners() = error("unused")

    override fun getPlayer(username: String?): Optional<Player> = error("unused")

    override fun getPlayer(uuid: UUID?): Optional<Player> = error("unused")

    override fun getAllPlayers(): Collection<Player> = error("unused")

    override fun getPlayerCount(): Int = error("unused")

    override fun getServer(name: String?): Optional<RegisteredServer> = error("unused")

    override fun getAllServers(): Collection<RegisteredServer> = error("unused")

    override fun matchPlayer(partialName: String?): Collection<Player> = error("unused")

    override fun matchServer(partialName: String?): Collection<RegisteredServer> = error("unused")

    override fun createRawRegisteredServer(server: ServerInfo?): RegisteredServer = error("unused")

    override fun registerServer(server: ServerInfo?): RegisteredServer = error("unused")

    override fun unregisterServer(server: ServerInfo?) = error("unused")

    override fun getConsoleCommandSource(): ConsoleCommandSource = error("unused")

    override fun getPluginManager() = error("unused")

    override fun getEventManager(): EventManager = events

    override fun getCommandManager(): CommandManager = manager

    override fun getScheduler() = error("unused")

    override fun getChannelRegistrar(): ChannelRegistrar = error("unused")

    override fun getBoundAddress(): InetSocketAddress = error("unused")

    override fun getConfiguration(): ProxyConfig = error("unused")

    override fun getVersion(): ProxyVersion = error("unused")

    override fun createResourcePackBuilder(url: String?): ResourcePackInfo.Builder = error("unused")
}

fun <T_ : Arguments> velocityStructure(
    block: StructureScope<VelocityEnvironment, CommandSource>.() -> Structure<VelocityEnvironment, CommandSource, T_>
): Structure<VelocityEnvironment, CommandSource, T_> =
    structure(VelocityEnvironment::class, CommandSource::class, block)

class FakeLogger : LegacyAbstractLogger() {
    val logged: MutableList<Pair<String?, Throwable?>> = mutableListOf()

    override fun isTraceEnabled(): Boolean = false

    override fun isTraceEnabled(marker: Marker?): Boolean = false

    override fun isDebugEnabled(): Boolean = false

    override fun isDebugEnabled(marker: Marker?): Boolean = false

    override fun isInfoEnabled(): Boolean = false

    override fun isInfoEnabled(marker: Marker?): Boolean = false

    override fun isWarnEnabled(): Boolean = false

    override fun isWarnEnabled(marker: Marker?): Boolean = false

    override fun isErrorEnabled(): Boolean = true

    override fun isErrorEnabled(marker: Marker?): Boolean = true

    override fun getFullyQualifiedCallerName(): String? = null

    override fun handleNormalizedLoggingCall(
        level: Level,
        marker: Marker?,
        messagePattern: String?,
        arguments: Array<out Any>?,
        throwable: Throwable?,
    ) {
        logged += messagePattern to throwable
    }
}
