package com.zombachu.stick.integration

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.optionallyNullable
import com.zombachu.stick.dsl.optionals
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.suggest
import com.zombachu.stick.integration.fixtures.targetPlayerParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class OptionalTest {

    private val zombachu = Player("zombachu", ["server.gift.amount", "server.speed.change", "server.weather.set"])
    private val steve = Player("Steve")
    private val console = Console()
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `list - optionals have default values`() {
        val listCommand = structure(Server::class, Sender::class) {
            command("list")(
                optionally(ifAbsent = default(1), parameter = intParameter("page", min = 1))
            ) { page ->
                sender.log("Showing page $page")
            }
        }

        listCommand.execute(server, zombachu, "/list")
        assertEquals(["Showing page 1"], zombachu.logs)

        listCommand.execute(server, zombachu, "/list 3")
        assertEquals(["Showing page 3"], zombachu.logs)
    }

    @Test
    fun `heal - optional defaults can validate`() {
        val healCommand = structure(Server::class, Sender::class) {
            command("heal")(
                targetPlayerParameter("player")
            ) { target ->
                target.log("You have been healed")
            }
        }

        healCommand.execute(server, zombachu, "/heal")
        assertEquals(["You have been healed"], zombachu.logs)

        healCommand.execute(server, zombachu, "/heal Steve")
        assertEquals(["You have been healed"], steve.logs)

        assertEquals(Feedback.InvalidSyntax("/heal <player>"), healCommand.executeExpectingError(server, console, "/heal"))

        healCommand.execute(server, console, "/heal Steve")
        assertEquals(["You have been healed"], steve.logs)
    }

    @Test
    fun `nick - optionals can be nullable`() {
        val nickCommand = structure(Server::class, Sender::class) {
            command("nick")(
                optionallyNullable(stringParameter("name"))
            ) { name ->
                sender.log(name?.let { "Nickname set to $it" } ?: "Nickname cleared")
            }
        }

        nickCommand.execute(server, zombachu, "/nick")
        assertEquals(["Nickname cleared"], zombachu.logs)

        nickCommand.execute(server, zombachu, "/nick zomb")
        assertEquals(["Nickname set to zomb"], zombachu.logs)
    }

    @Test
    fun `gift - usage changes if optional inaccessible`() {
        val giftCommand = structure(Server::class, Sender::class) {
            command("gift")(
                playerParameter("player"),
                optionally(
                    ifInvalid = invalidDefault(1, permission("server.gift.amount")),
                    ifAbsent = default(1),
                    parameter = intParameter("amount", min = 1, max = 64),
                ),
            ) { target, amount ->
                target.log("Received $amount items from ${sender.name}")
            }
        }

        assertEquals(
            Feedback.InvalidSyntax("/gift <player> [amount]"),
            giftCommand.executeExpectingError(server, zombachu, "/gift"),
        )

        assertEquals(Feedback.InvalidSyntax("/gift <player>"), giftCommand.executeExpectingError(server, steve, "/gift"))
    }

    @Test
    fun `gamemode - usage changes to required if absent default inaccessible`() {
        val gamemodeCommand = structure(Server::class, Sender::class) {
            command("gamemode")(
                stringParameter("mode"),
                targetPlayerParameter("player"),
            ) { mode, target ->
                target.log("Game mode set to $mode")
            }
        }

        assertEquals(
            Feedback.InvalidSyntax("/gamemode <mode> [player]"),
            gamemodeCommand.executeExpectingError(server, zombachu, "/gamemode"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/gamemode <mode> <player>"),
            gamemodeCommand.executeExpectingError(server, console, "/gamemode"),
        )
    }

    @Test
    fun `speed - optionals can have different defaults`() {
        val speedCommand = structure(Server::class, Sender::class) {
            command("speed")(
                optionally(
                    ifInvalid = invalidDefault(1, permission("server.speed.change")),
                    ifAbsent = default(5),
                    parameter = intParameter("speed", min = 1, max = 10),
                ),
            ) { speed ->
                sender.log("Speed changed to $speed")
            }
        }

        speedCommand.execute(server, zombachu, "/speed")
        assertEquals(["Speed changed to 5"], zombachu.logs)

        speedCommand.execute(server, zombachu, "/speed 10")
        assertEquals(["Speed changed to 10"], zombachu.logs)

        speedCommand.execute(server, steve, "/speed")
        assertEquals(["Speed changed to 1"], steve.logs)

        assertEquals(Feedback.InvalidPermission, speedCommand.executeExpectingError(server, steve, "/speed 10"))
        assertEquals(Feedback.InvalidPermission, speedCommand.executeExpectingError(server, steve, "/speed asdf"))
    }

    @Test
    fun `tp - optionals process left to right`() {
        val tpCommand = structure(Server::class, Sender::class) {
            command("tp")(
                optionals(
                    optionallyNullable(literalParameter("here")),
                    optionallyNullable(literalParameter("there")),
                )
            ) { (here: String?, there: String?) ->
                sender.log("$here $there")
            }
        }

        tpCommand.execute(server, zombachu, "/tp here there")
        assertEquals(["here there"], zombachu.logs)

        assertEquals(
            Feedback.LiteralNotMatched(["here"], "there"),
            tpCommand.executeExpectingError(server, zombachu, "/tp there"),
        )
    }

    @Test
    fun `home - subcommand group can be optional`() {
        val homeCommand = structure(Server::class, Sender::class) {
            command("home")(
                optionallyNullable(
                    group(
                        command("set")(stringParameter("name")) { name -> sender.log("Home $name set") },
                        command("delete")(stringParameter("name")) { name -> sender.log("Home $name deleted") },
                    )
                )
            ) { subcommand ->
                if (subcommand == null) sender.log("Teleported to bed")
            }
        }

        homeCommand.execute(server, zombachu, "/home")
        assertEquals(["Teleported to bed"], zombachu.logs)

        homeCommand.execute(server, zombachu, "/home set farm")
        assertEquals(["Home farm set"], zombachu.logs)

        assertEquals(["set", "delete"], homeCommand.suggest(server, zombachu, "/home "))

        assertEquals(
            Feedback.InvalidSyntax("/home [set|delete]"),
            homeCommand.executeExpectingError(server, zombachu, "/home list"),
        )
    }

    @Test
    fun `weather - optional group can be gated by permission`() {
        val weatherCommand = structure(Server::class, Sender::class) {
            command("weather")(
                optionallyNullable(
                    group(literalParameter("rain"), literalParameter("sun")),
                    requirement = permission("server.weather.set"),
                )
            ) { weather ->
                sender.log("Weather set to ${weather?.value ?: "clear"}")
            }
        }

        weatherCommand.execute(server, zombachu, "/weather")
        assertEquals(["Weather set to clear"], zombachu.logs)

        weatherCommand.execute(server, zombachu, "/weather sun")
        assertEquals(["Weather set to sun"], zombachu.logs)

        weatherCommand.execute(server, steve, "/weather")
        assertEquals(["Weather set to clear"], steve.logs)

        assertEquals(Feedback.InvalidPermission, weatherCommand.executeExpectingError(server, steve, "/weather rain"))

        assertEquals(
            Feedback.InvalidSyntax("/weather [rain|sun]"),
            weatherCommand.executeExpectingError(server, zombachu, "/weather snow"),
        )
    }
}
