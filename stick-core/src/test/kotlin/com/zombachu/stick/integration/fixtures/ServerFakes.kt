package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Environment

// --- senders ------------------------------------------------------------------------------------------------------

interface Sender {
    val name: String
    val logs: MutableList<String>

    fun hasPermission(node: String): Boolean

    fun log(message: String) {
        logs += message
    }
}

class Console : Sender {
    override val name: String = "Console"
    override val logs: MutableList<String> = mutableListOf()

    override fun hasPermission(node: String): Boolean = true
}

open class Player(
    override val name: String,
    private val permissions: Set<String> = [],
) : Sender {
    override val logs: MutableList<String> = mutableListOf()

    val socialData: SocialData = SocialData(this)
    val mail: MutableList<String> = mutableListOf()
    var position: Location = Location(0, 64, 0)
    var world: String = "overworld"

    override fun hasPermission(node: String): Boolean = node in permissions
}

class SocialData(val player: Player) {
    val bio: MutableList<String> = mutableListOf()
    val nicknames: MutableMap<String, String> = mutableMapOf()
}

// --- environments -------------------------------------------------------------------------------------------------

interface Server : Environment {
    fun getPlayer(name: String): Player?
}

interface WarpableServer : Server {
    val warps: WarpRegistry
}

class SynergyServer(
    private val players: List<Player> = [],
    override val warps: WarpRegistry = WarpRegistry(),
) : WarpableServer {

    override fun getPlayer(name: String): Player? = players.find { it.name.equals(name, ignoreCase = true) }

    fun clearMessages() {
        players.forEach { it.logs.clear() }
    }
}

// --- types --------------------------------------------------------------------------------------------------------

data class Location(val x: Int, val y: Int, val z: Int)

enum class Weather(override val label: String, override val aliases: Set<String>) : Aliasable {
    Clear("clear", ["sun", "sunny"]),
    Rain("rain", ["rainy", "wet"]),
    Storm("storm", ["thunder"]),
}

enum class GameMode {
    Survival,
    Creative,
    Spectator,
}

enum class Material {
    Dirt,
    Grass,
    Bedrock,
}

val WORLDS = ["overworld", "nether", "end"]

data class Warp(val name: String, val owner: String, val world: String)

class WarpRegistry(warps: List<Warp> = []) {
    val warps: MutableMap<String, Warp> = warps.associateByTo(mutableMapOf()) { it.name.lowercase() }

    val names: List<String>
        get() = warps.values.map { it.name }

    operator fun get(name: String): Warp? = warps[name.lowercase()]

    fun add(warp: Warp) {
        warps[warp.name.lowercase()] = warp
    }

    fun remove(name: String): Boolean = warps.remove(name.lowercase()) != null
}
