package com.zombachu.stick.velocity

import com.mojang.brigadier.tree.CommandNode
import com.velocitypowered.api.command.Command
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.command.CommandMeta
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.velocitypowered.api.event.EventHandler
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.permission.Tristate
import com.velocitypowered.api.proxy.ProxyServer
import com.zombachu.stick.Arguments
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import com.zombachu.stick.unused
import net.kyori.adventure.text.Component
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

class FakeCommandManager : CommandManager by unused() {
    var registeredMeta: CommandMeta? = null
        private set

    var registeredCommand: Any? = null
        private set

    var registerCalls: Int = 0
        private set

    override fun metaBuilder(alias: String): CommandMeta.Builder = FakeCommandMetaBuilder(alias)

    override fun register(meta: CommandMeta, command: Command) {
        registerCalls++
        registeredMeta = meta
        registeredCommand = command
    }
}

class FakeEventManager : EventManager by unused() {
    val handlers: MutableList<Pair<Class<*>, EventHandler<*>>> = mutableListOf()

    override fun <E> register(plugin: Any, eventClass: Class<E>, postOrder: PostOrder, handler: EventHandler<E>) {
        handlers += eventClass to handler
    }

    override fun <E> fire(event: E): CompletableFuture<E> {
        for ((eventClass, handler) in handlers) {
            @Suppress("UNCHECKED_CAST")
            if (eventClass.isInstance(event)) (handler as EventHandler<E>).execute(event)
        }
        return CompletableFuture.completedFuture(event)
    }
}

class FakeProxyServer(
    private val manager: CommandManager = FakeCommandManager(),
    private val events: EventManager = FakeEventManager(),
) : ProxyServer by unused() {
    override fun getEventManager(): EventManager = events

    override fun getCommandManager(): CommandManager = manager
}

fun <T_ : Arguments> velocityStructure(
    block: StructureScope<VelocityEnvironment, CommandSource>.() -> Structure<VelocityEnvironment, CommandSource, T_>
): Structure<VelocityEnvironment, CommandSource, T_> =
    structure(VelocityEnvironment::class, CommandSource::class, block)
