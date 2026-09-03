package com.zombachu.stick.integration

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.map
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.pipeline
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.integration.fixtures.CustomError
import com.zombachu.stick.integration.fixtures.Material
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.WORLDS
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.playerParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class PipelineTest {

    private val zombachu = Player("zombachu", ["server.tp"])
    private val steve = Player("Steve")
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `page - pipeline applies to optional default`() {
        val pageCommand = structure(Server::class, Sender::class) {
            command("page")(
                optionally(ifAbsent = default(1), parameter = intParameter("page", min = 1)).pipeline(
                    map { it - 1 }
                )
            ) { index ->
                sender.log("Page: $index")
            }
        }

        pageCommand.execute(server, zombachu, "/page 3")
        assertEquals(["Page: 2"], zombachu.logs)

        pageCommand.execute(server, zombachu, "/page")
        assertEquals(["Page: 0"], zombachu.logs)
    }

    @Test
    fun `tp - pipeline is contextual`() {
        val tpCommand = structure(Server::class, Sender::class) {
            command("tp")(
                playerParameter("player").pipeline(
                    map { TargetPlayer(it, it === sender) }
                )
            ) { target ->
                sender.log(if (target.isSelf) "Teleported to yourself" else "Teleported to ${target.player.name}")
            }
        }

        tpCommand.execute(server, zombachu, "/tp zombachu")
        assertEquals(["Teleported to yourself"], zombachu.logs)

        tpCommand.execute(server, zombachu, "/tp Steve")
        assertEquals(["Teleported to Steve"], zombachu.logs)
    }

    @Test
    fun `tphere - pipeline can fail`() {
        val tpHereCommand = structure(Server::class, Player::class) {
            command("tphere")(
                playerParameter("player").pipeline {
                    if (it.world == sender.world) {
                        ParsingResult.success(it)
                    } else {
                        CustomError("${it.name} is in another world")
                    }
                }
            ) { target ->
                sender.log("Teleported ${target.name} to you")
            }
        }
        zombachu.world = "overworld"
        steve.world = "nether"

        assertEquals(
            "Steve is in another world",
            tpHereCommand.executeExpectingError(server, zombachu, "/tphere Steve").message,
        )

        tpHereCommand.execute(server, zombachu, "/tphere zombachu")
        assertEquals(["Teleported zombachu to you"], zombachu.logs)
    }

    @Test
    fun `give - pipeline supports multiple stages`() {
        val giveCommand = structure(Server::class, Sender::class) {
            command("give")(
                stringParameter("item").pipeline(
                    map { it.lowercase() },
                    { name ->
                        Material.entries.find { it.name.lowercase() == name }
                            ?.let { ParsingResult.success(it) }
                            ?: CustomError("No item called $name")
                    },
                    map { "Gave a $it block" },
                )
            ) { description ->
                sender.log(description)
            }
        }

        giveCommand.execute(server, zombachu, "/give DIRT")
        assertEquals(["Gave a Dirt block"], zombachu.logs)

        assertEquals(
            "No item called diamond",
            giveCommand.executeExpectingError(server, zombachu, "/give diamond").message,
        )
    }

    @Test
    fun `spawn - pipeline applies to flag default`() {
        val spawnCommand = structure(Server::class, Sender::class) {
            command("spawn")(
                valueFlag(name = "world", default = "overworld", parameter = stringParameter("world")).pipeline(
                    map { it.lowercase() },
                    { name ->
                        if (name in WORLDS) ParsingResult.success(name)
                        else CustomError("Unknown world: $name")
                    },
                    map { "the $it" },
                ),
                playerParameter("player"),
            ) { world, target -> sender.log("Sending ${target.name} to $world") }
        }

        spawnCommand.execute(server, zombachu, "/spawn Steve -world NETHER")
        assertEquals(["Sending Steve to the nether"], zombachu.logs)

        spawnCommand.execute(server, zombachu, "/spawn Steve")
        assertEquals(["Sending Steve to the overworld"], zombachu.logs)
    }

    @Test
    fun `stop - pipeline inside optional does not apply to default`() {
        val stopCommand = structure(Server::class, Sender::class) {
            command("stop")(
                optionally(
                    ifAbsent = default("server shutting down"),
                    parameter = stringParameter("reason").pipeline(
                        map { it.replaceFirstChar(Char::uppercase) }
                    ),
                )
            ) { reason ->
                sender.log("Stopping: $reason")
            }
        }

        stopCommand.execute(server, zombachu, "/stop maintenance")
        assertEquals(["Stopping: Maintenance"], zombachu.logs)

        stopCommand.execute(server, zombachu, "/stop")
        assertEquals(["Stopping: server shutting down"], zombachu.logs)
    }

    @Test
    fun `stop - pipeline outside optional applies to default`() {
        val restartCommand = structure(Server::class, Sender::class) {
            command("stop")(
                optionally(
                    ifAbsent = default("server shutting down"),
                    parameter = stringParameter("reason"),
                ).pipeline(map { it.replaceFirstChar(Char::uppercase) })
            ) { reason ->
                sender.log("Stopping: $reason")
            }
        }

        restartCommand.execute(server, zombachu, "/stop maintenance")
        assertEquals(["Stopping: Maintenance"], zombachu.logs)

        restartCommand.execute(server, zombachu, "/stop")
        assertEquals(["Stopping: Server shutting down"], zombachu.logs)
    }

    @Test
    fun `cookie - pipeline applies to flag`() {
        val cookieCommand = structure(Server::class, Sender::class) {
            command("cookie")(
                flag("max").pipeline(
                    map { if (it) Int.MAX_VALUE else 1 }
                )
            ) { limit ->
                sender.log("Giving $limit cookies")
            }
        }

        cookieCommand.execute(server, zombachu, "/cookie -max")
        assertEquals(["Giving 2147483647 cookies"], zombachu.logs)

        cookieCommand.execute(server, zombachu, "/cookie")
        assertEquals(["Giving 1 cookies"], zombachu.logs)
    }

    @Test
    fun `caps - pipeline applies to unbounded parameter`() {
        val capsCommand = structure(Server::class, Sender::class) {
            command("caps")(
                textParameter("message").pipeline(
                    map { it.uppercase() },
                    { ParsingResult.success("$it!") },
                )
            ) { message ->
                sender.log(message)
            }
        }

        capsCommand.execute(server, zombachu, "/caps hello")
        assertEquals(["HELLO!"], zombachu.logs)
    }

    @Test
    fun `KNOWN LIMITATION - speed - ifAbsent type can differ from pipeline output`() {
        val speedCommand = structure(Server::class, Sender::class) {
            command("speed")(
                optionally(
                    ifAbsent = default("one"),
                    parameter = stringParameter("level").pipeline(map { it.length / 2f }),
                )
            ) { level ->
                sender.log(level::class.simpleName ?: "?")
            }
        }

        speedCommand.execute(server, zombachu, "/speed")
        assertEquals(["String"], zombachu.logs)

        speedCommand.execute(server, zombachu, "/speed 9")
        assertEquals(["Float"], zombachu.logs)
    }

    private data class TargetPlayer(val player: Player, val isSelf: Boolean)
}
