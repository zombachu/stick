package com.zombachu.stick.paper

import com.zombachu.stick.Arguments
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import io.papermc.paper.plugin.configuration.PluginMeta
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.logging.Logger

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
class FakeCommandSender(private val permissions: Set<String> = []) : CommandSender {
    val sentMessages: MutableList<Component> = mutableListOf()

    override fun hasPermission(name: String): Boolean = name in permissions

    override fun hasPermission(perm: Permission): Boolean = perm.name in permissions

    override fun sendMessage(message: Component) {
        sentMessages += message
    }

    override fun sendMessage(message: String) = error("unused")

    override fun sendMessage(vararg messages: String) = error("unused")

    override fun sendMessage(sender: UUID?, message: String) = error("unused")

    override fun sendMessage(sender: UUID?, vararg messages: String) = error("unused")

    override fun getServer(): Server = error("unused")

    override fun getName(): String = "fake"

    override fun spigot(): CommandSender.Spigot = error("unused")

    override fun name(): Component = Component.text("fake")

    override fun isPermissionSet(name: String): Boolean = error("unused")

    override fun isPermissionSet(perm: Permission): Boolean = error("unused")

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean): PermissionAttachment =
        error("unused")

    override fun addAttachment(plugin: Plugin): PermissionAttachment = error("unused")

    override fun addAttachment(plugin: Plugin, name: String, value: Boolean, ticks: Int): PermissionAttachment =
        error("unused")

    override fun addAttachment(plugin: Plugin, ticks: Int): PermissionAttachment = error("unused")

    override fun removeAttachment(attachment: PermissionAttachment) = error("unused")

    override fun recalculatePermissions() = error("unused")

    override fun getEffectivePermissions(): MutableSet<PermissionAttachmentInfo> = error("unused")

    override fun isOp(): Boolean = error("unused")

    override fun setOp(value: Boolean) = error("unused")
}

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
object FakePlugin : Plugin {
    override fun getDataFolder(): File = error("unused")

    override fun getDescription(): PluginDescriptionFile = error("unused")

    override fun getPluginMeta(): PluginMeta = error("unused")

    override fun getConfig(): FileConfiguration = error("unused")

    override fun getResource(filename: String): InputStream = error("unused")

    override fun saveConfig() = error("unused")

    override fun saveDefaultConfig() = error("unused")

    override fun saveResource(resourcePath: String, replace: Boolean) = error("unused")

    override fun reloadConfig() = error("unused")

    override fun getPluginLoader(): PluginLoader = error("unused")

    override fun getServer(): Server = error("unused")

    override fun isEnabled(): Boolean = error("unused")

    override fun onDisable() = error("unused")

    override fun onLoad() = error("unused")

    override fun onEnable() = error("unused")

    override fun isNaggable(): Boolean = error("unused")

    override fun setNaggable(canNag: Boolean) = error("unused")

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator =
        error("unused")

    override fun getDefaultBiomeProvider(worldName: String, id: String?): BiomeProvider =
        error("unused")

    override fun getLogger(): Logger = error("unused")

    override fun getName(): String = "fake-plugin"

    override fun namespace(): String = "fake-plugin"

    override fun getLifecycleManager(): LifecycleEventManager<Plugin> = error("unused")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean =
        error("unused")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>,
    ): MutableList<String> = error("unused")
}

class FakeBukkitEnvironment(override val plugin: Plugin = FakePlugin) : BukkitEnvironment {
    override val server: Server
        get() = error("unused")
}

fun <T_ : Arguments> bukkitStructure(
    block: StructureScope<BukkitEnvironment, CommandSender>.() -> Structure<BukkitEnvironment, CommandSender, T_>
): Structure<BukkitEnvironment, CommandSender, T_> = structure(BukkitEnvironment::class, CommandSender::class, block)
