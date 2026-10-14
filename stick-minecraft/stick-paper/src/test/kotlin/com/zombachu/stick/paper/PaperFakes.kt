package com.zombachu.stick.paper

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.LiteralCommandNode
import com.zombachu.stick.Arguments
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import com.zombachu.stick.unused
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger

class FakeCommandSender(private val permissions: Set<String> = []) : CommandSender by unused() {
    val sentMessages: MutableList<Component> = mutableListOf()

    override fun hasPermission(name: String): Boolean = name in permissions

    override fun hasPermission(perm: Permission): Boolean = perm.name in permissions

    override fun sendMessage(message: Component) {
        sentMessages += message
    }

    override fun getName(): String = "fake"

    override fun name(): Component = Component.text("fake")
}

class FakeCommandSourceStack(private val sender: CommandSender) : CommandSourceStack by unused() {
    override fun getSender(): CommandSender = sender
}

object FakePlugin : Plugin by unused() {
    override fun getLogger(): Logger = Logger.getLogger("fake-plugin")

    override fun getName(): String = "fake-plugin"

    override fun namespace(): String = "fake-plugin"
}

class FakePaperEnvironment(override val plugin: Plugin = FakePlugin) : PaperEnvironment {
    override val server: Server
        get() = error("unused")
}

fun <T_ : Arguments> paperStructure(
    block: StructureScope<PaperEnvironment, CommandSender>.() -> Structure<PaperEnvironment, CommandSender, T_>
): Structure<PaperEnvironment, CommandSender, T_> = structure(PaperEnvironment::class, CommandSender::class, block)

class FakeLifecyclePlugin : Plugin by FakePlugin {
    val lifecycleEvents: FakeLifecycleEventManager = FakeLifecycleEventManager()
    val listeners: MutableList<Listener> = mutableListOf()

    override fun getLifecycleManager(): LifecycleEventManager<Plugin> = lifecycleEvents

    override fun getServer(): Server =
        object : Server by unused() {
            override fun getPluginManager(): PluginManager =
                object : PluginManager by unused() {
                    override fun registerEvents(listener: Listener, plugin: Plugin) {
                        listeners += listener
                    }
                }
        }
}

class FakeLifecycleEventManager : LifecycleEventManager<Plugin> by unused() {
    private val handlers: MutableList<Pair<LifecycleEventType<*, *, *>, LifecycleEventHandler<*>>> = mutableListOf()

    override fun <E : LifecycleEvent> registerEventHandler(
        eventType: LifecycleEventType<in Plugin, out E, *>,
        eventHandler: LifecycleEventHandler<in E>,
    ) {
        handlers += eventType to eventHandler
    }

    fun <E : LifecycleEvent> fire(eventType: LifecycleEventType<*, E, *>, event: E) {
        for ((type, handler) in handlers) {
            @Suppress("UNCHECKED_CAST")
            if (type === eventType) (handler as LifecycleEventHandler<E>).run(event)
        }
    }
}

class FakeCommandsEvent(private val commands: Commands) : ReloadableRegistrarEvent<Commands> by unused() {
    override fun registrar(): Commands = commands
}

data class FakeRegistration(val node: LiteralCommandNode<CommandSourceStack>, val aliases: List<String>)

class FakeRegistrar : Commands by unused() {
    val registered: MutableList<FakeRegistration> = mutableListOf()
    private val dispatcher = CommandDispatcher<CommandSourceStack>()
    private val source = FakeCommandSourceStack(FakeCommandSender())

    fun execute(command: String): Int = dispatcher.execute(command, source)

    fun complete(command: String): List<String> = suggestions(command).join()

    fun suggestions(command: String): CompletableFuture<List<String>> =
        dispatcher.getCompletionSuggestions(dispatcher.parse(command, source)).thenApply { suggestions ->
            suggestions.list.map { it.apply(command) }
        }

    override fun getDispatcher(): CommandDispatcher<CommandSourceStack> = dispatcher

    override fun register(
        node: LiteralCommandNode<CommandSourceStack>,
        description: String?,
        aliases: Collection<String>,
    ): Set<String> {
        registered += FakeRegistration(node, aliases.toList())
        val namespaced =
            Commands.literal("${FakePlugin.namespace()}:${node.literal}")
                .requires(node.requirement)
                .executes(node.command)
        node.children.forEach { namespaced.then(it) }
        dispatcher.root.addChild(node)
        dispatcher.root.addChild(namespaced.build())
        return []
    }
}
