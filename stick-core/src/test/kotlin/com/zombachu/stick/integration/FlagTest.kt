package com.zombachu.stick.integration

import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.enumFlag
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.nullableEnumFlag
import com.zombachu.stick.dsl.nullableValueFlag
import com.zombachu.stick.dsl.require
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.GameMode
import com.zombachu.stick.integration.fixtures.Material
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Weather
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.permissionedValue
import com.zombachu.stick.integration.fixtures.playerParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class FlagTest {

    private val zombachu = Player("zombachu", ["server.me.staff", "server.broadcast.raw"])
    private val steve = Player("Steve")
    private val console = Console()
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `broadcast - presence flag parses`() {
        val broadcastCommand = structure(Server::class, Sender::class) {
            command("broadcast")(
                flag(name = "raw", aliases = ["r"]),
                textParameter("message"),
            ) { raw, message ->
                sender.log(if (raw) message else "[Server] $message")
            }
        }

        broadcastCommand.execute(server, zombachu, "/broadcast Server restarting")
        assertEquals(["[Server] Server restarting"], zombachu.logs)

        broadcastCommand.execute(server, zombachu, "/broadcast -raw Server restarting")
        assertEquals(["Server restarting"], zombachu.logs)

        broadcastCommand.execute(server, zombachu, "/broadcast -r Server restarting")
        assertEquals(["Server restarting"], zombachu.logs)

        broadcastCommand.execute(server, zombachu, "/broadcast Server restarting -raw")
        assertEquals(["[Server] Server restarting -raw"], zombachu.logs)
    }

    @Test
    fun `give - flags parse anywhere`() {
        val giveCommand = structure(Server::class, Sender::class) {
            command("give")(
                flag("silent"),
                playerParameter("player"),
                literalParameter("stone"),
            ) { silent, target, item ->
                target.log("Received $item")
                if (!silent) {
                    target.log("${sender.name} gifted you $item")
                }
            }
        }

        giveCommand.execute(server, zombachu, "/give -silent Steve stone")
        assertEquals(["Received stone"], steve.logs)

        giveCommand.execute(server, zombachu, "/give Steve -silent stone")
        assertEquals(["Received stone"], steve.logs)

        giveCommand.execute(server, zombachu, "/give Steve stone -silent")
        assertEquals(["Received stone"], steve.logs)

        giveCommand.execute(server, zombachu, "/give Steve stone")
        assertEquals(["Received stone", "zombachu gifted you stone"], steve.logs)

        assertEquals(
            Feedback.InvalidSyntax("/give <player> <stone> [-silent]"),
            giveCommand.executeExpectingError(server, zombachu, "/give"),
        )
    }

    @Test
    fun `echo - value flag parses with parameter`() {
        val echoCommand = structure(Server::class, Sender::class) {
            command("echo")(
                valueFlag(name = "times", default = 1, parameter = intParameter("times", min = 1, max = 5)),
                textParameter("message"),
            ) { times, message ->
                repeat(times) { sender.log(message) }
            }
        }

        echoCommand.execute(server, zombachu, "/echo -times 3 Hello")
        assertEquals(["Hello", "Hello", "Hello"], zombachu.logs)

        echoCommand.execute(server, zombachu, "/echo Hello")
        assertEquals(["Hello"], zombachu.logs)

        assertEquals(
            Feedback.TypeNotMatched("integer", "many"),
            echoCommand.executeExpectingError(server, zombachu, "/echo -times many Hello"),
        )
    }

    @Test
    fun `prefix - value flags accept contextual defaults`() {
        val prefixCommand = structure(Server::class, Sender::class) {
            command("me")(
                valueFlag(
                    name = "prefix",
                    default = permissionedValue("server.me.staff", value = "Staff", fallback = "Player"),
                    parameter = stringParameter("prefix"),
                ),
                textParameter("message"),
            ) { prefix, message ->
                sender.log("[$prefix] ${sender.name}: $message")
            }
        }

        prefixCommand.execute(server, steve, "/me hello")
        assertEquals(["[Player] Steve: hello"], steve.logs)

        prefixCommand.execute(server, steve, "/me -prefix ABC hello")
        assertEquals(["[ABC] Steve: hello"], steve.logs)

        prefixCommand.execute(server, zombachu, "/me hello")
        assertEquals(["[Staff] zombachu: hello"], zombachu.logs)

        prefixCommand.execute(server, zombachu, "/me -prefix ABC hello")
        assertEquals(["[ABC] zombachu: hello"], zombachu.logs)
    }

    @Test
    fun `mail - value flags can have nullable default`() {
        val mailCommand = structure(Server::class, Sender::class) {
            command("mail")(
                nullableValueFlag(
                    name = "player",
                    parameter = playerParameter("player")
                )
            ) { from ->
                sender.log(from?.let { "Showing mail from ${it.name}" } ?: "Showing all mail")
            }
        }

        mailCommand.execute(server, zombachu, "/mail")
        assertEquals(["Showing all mail"], zombachu.logs)

        mailCommand.execute(server, zombachu, "/mail -player Steve")
        assertEquals(["Showing mail from Steve"], zombachu.logs)
    }

    @Test
    fun `weather - enum flags match to enum values`() {
        val weatherCommand = structure(Server::class, Sender::class) {
            command("weather")(
                enumFlag(
                    default = Weather.Clear,
                    from = enumParameter("weather", Weather::class)
                )
            ) { weather ->
                sender.log("Weather set to $weather")
            }
        }

        weatherCommand.execute(server, zombachu, "/weather")
        assertEquals(["Weather set to Clear"], zombachu.logs)

        weatherCommand.execute(server, zombachu, "/weather -storm")
        assertEquals(["Weather set to Storm"], zombachu.logs)

        weatherCommand.execute(server, zombachu, "/weather -thunder")
        assertEquals(["Weather set to Storm"], zombachu.logs)
    }

    @Test
    fun `list - enum flags can have nullable default`() {
        val listCommand = structure(Server::class, Sender::class) {
            command("list")(
                nullableEnumFlag(enumParameter("mode", GameMode::class))
            ) { gamemode ->
                sender.log(gamemode?.let { "Showing all players in $it:" } ?: "Showing all players:")
            }
        }

        listCommand.execute(server, zombachu, "/list")
        assertEquals(["Showing all players:"], zombachu.logs)

        listCommand.execute(server, zombachu, "/list -creative")
        assertEquals(["Showing all players in Creative:"], zombachu.logs)
    }

    @Test
    fun `music - hybrid flags parse`() {
        val musicCommand = structure(Server::class, Sender::class) {
            command("music")(
                hybridFlag("volume", intParameter("level", min = 1, max = 10))
            ) { lock ->
                when (lock) {
                    is HybridFlagResult.Absent -> sender.log("Music is playing")
                    is HybridFlagResult.Present -> sender.log("Volume muted")
                    is HybridFlagResult.Value -> sender.log("Volume set to ${lock.value}")
                }
            }
        }

        musicCommand.execute(server, zombachu, "/music")
        assertEquals(["Music is playing"], zombachu.logs)

        musicCommand.execute(server, zombachu, "/music -volume")
        assertEquals(["Volume muted"], zombachu.logs)

        musicCommand.execute(server, zombachu, "/music -volume 3")
        assertEquals(["Volume set to 3"], zombachu.logs)
    }

    @Test
    fun `nonsense - multiple flags parse`() {
        val nonsenseCommand = structure(Server::class, Sender::class) {
            command("nonsense")(
                intParameter("x"),
                intParameter("y"),
                intParameter("z"),
                enumParameter("material", Material::class),
                valueFlag(name = "count", default = 1, parameter = intParameter("count", min = 1, max = 64)),
                nullableValueFlag(name = "owner", parameter = playerParameter("owner")),
                flag("replace"),
                flag("notify"),
                valueFlag(name = "world", default = "overworld", parameter = stringParameter("world")),
                enumFlag(Weather.Clear, enumParameter("weather", Weather::class)),
                flag("force"),
                textParameter("comment"),
            ) { x, y, z, material, count, owner, replace, notify, world, weather, force, comment ->
                sender.log(
                    "Placed $count ${material.name.lowercase()} at ${x + y + z} in $world during ${weather.label}" +
                        " for ${owner?.name ?: "the server"}," +
                        " flags ${[replace, notify, force].count { it }}: ${comment.uppercase()}"
                )
            }
        }

        nonsenseCommand.execute(
            server,
            zombachu,
            "/nonsense -storm 1 2 -owner Steve 3 dirt -count 5 -replace hello there",
        )
        assertEquals(["Placed 5 dirt at 6 in overworld during storm for Steve, flags 1: HELLO THERE"], zombachu.logs)

        nonsenseCommand.execute(server, zombachu, "/nonsense 1 2 3 dirt hello")
        assertEquals(
            ["Placed 1 dirt at 6 in overworld during clear for the server, flags 0: HELLO"],
            zombachu.logs,
        )
    }

    @Test
    fun `broadcast - flags can be gated by requirement`() {
        val broadcastCommand = structure(Server::class, Sender::class) {
            command("broadcast")(
                require(invalidDefault("Player", permission("server.broadcast.raw"))) {
                    valueFlag(name = "prefix", default = "#", parameter = stringParameter("prefix"))
                },
                textParameter("message"),
            ) { prefix, message ->
                sender.log("[$prefix] $message")
            }
        }

        broadcastCommand.execute(server, zombachu, "/broadcast Server restarting")
        assertEquals(["[#] Server restarting"], zombachu.logs)

        broadcastCommand.execute(server, zombachu, "/broadcast -prefix abc123 Server restarting")
        assertEquals(["[abc123] Server restarting"], zombachu.logs)

        broadcastCommand.execute(server, steve, "/broadcast Server restarting")
        assertEquals(["[Player] Server restarting"], steve.logs)

        broadcastCommand.execute(server, steve, "/broadcast -prefix abc123 Server restarting")
        assertEquals(["[Player] -prefix abc123 Server restarting"], steve.logs)
    }

    @Test
    fun `profile -  flags can be gated by sender type`() {
        val profileCommand = structure(Server::class, Sender::class) {
            command("profile")(
                requireIs(Player::class, invalidDefault("*")) {
                    valueFlag(name = "world", default = "overworld", parameter = stringParameter("world"))
                }
            ) { world ->
                sender.log("Profile at $world:")
            }
        }

        profileCommand.execute(server, console, "/profile")
        assertEquals(["Profile at *:"], console.logs)

        profileCommand.execute(server, zombachu, "/profile -world lobby")
        assertEquals(["Profile at lobby:"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/profile"),
            profileCommand.executeExpectingError(server, console, "/profile -world lobby"),
        )
    }
}
