package com.zombachu.stick.integration

import com.zombachu.stick.AliasEntry
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listElementParameter
import com.zombachu.stick.dsl.listParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.parameters.by
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.GameMode
import com.zombachu.stick.integration.fixtures.Material
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Weather
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.playerParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class ParameterTest {

    private val zombachu = Player("zombachu")
    private val steve = Player("Steve")
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `whitelist - list parameter parses`() {
        val whitelistCommand = structure(Server::class, Sender::class) {
            command("whitelist")(
                listParameter("players", playerParameter("player"))
            ) { players ->
                sender.log("Whitelisted ${players.joinToString(", ") { it.name }}")
            }
        }

        whitelistCommand.execute(server, zombachu, "/whitelist zombachu,sTeve")
        assertEquals(["Whitelisted zombachu, Steve"], zombachu.logs)

        assertEquals(
            Feedback.TypeNotMatched("player", "nobody"),
            whitelistCommand.executeExpectingError(server, zombachu, "/whitelist zombachu,nobody"),
        )
    }

    @Test
    fun `mail delete - list element parameter parses 1-indexed contextual list`() {
        val mailDeleteCommand = structure(Server::class, Player::class) {
            command("delete")(
                listElementParameter(
                    name = "index",
                    list = { ParsingResult.success(sender.mail) },
                    oneIndexed = true,
                    onEmpty = { sender.log("You have no mail") },
                )
            ) { selected ->
                val message = "Deleted message ${selected.index + 1} of ${selected.list.size}: ${selected.result}"
                sender.mail.removeAt(selected.index)
                sender.log(message)
            }
        }
        zombachu.mail += "Hello"
        zombachu.mail += "there"

        mailDeleteCommand.execute(server, zombachu, "/delete 2")
        assertEquals(["Deleted message 2 of 2: there"], zombachu.logs)
        assertEquals(["Hello"], zombachu.mail)

        zombachu.mail += "abcd"

        mailDeleteCommand.execute(server, zombachu, "/delete 1")
        assertEquals(["Deleted message 1 of 2: Hello"], zombachu.logs)
        assertEquals(["abcd"], zombachu.mail)
    }

    @Test
    fun `gamemode - enum parameter matches to names`() {
        val gameModeCommand = structure(Server::class, Sender::class) {
            command("gamemode")(
                enumParameter("mode", GameMode::class)
            ) { mode ->
                sender.log("Gamemode set to $mode")
            }
        }

        gameModeCommand.execute(server, zombachu, "/gamemode creative")
        assertEquals(["Gamemode set to Creative"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/gamemode <survival|creative|spectator>"),
            gameModeCommand.executeExpectingError(server, zombachu, "/gamemode"),
        )
    }

    @Test
    fun `weather - enum parameter for aliasable matches to aliases`() {
        val weatherCommand = structure(Server::class, Sender::class) {
            command("weather")(
                enumParameter("weather", Weather::class)
            ) { weather ->
                sender.log("Weather set to $weather")
            }
        }

        weatherCommand.execute(server, zombachu, "/weather storm")
        assertEquals(["Weather set to Storm"], zombachu.logs)

        weatherCommand.execute(server, zombachu, "/weather sunny")
        assertEquals(["Weather set to Clear"], zombachu.logs)
    }

    @Test
    fun `setblock - enum parameter matches subset`() {
        val setBlockCommand = structure(Server::class, Sender::class) {
            command("setblock")(
                enumParameter(
                    "material",
                    [Material.Dirt by "dirt", Material.Grass by AliasEntry("grass", ["mycelium", "podzol"])],
                )
            ) { material -> sender.log("Set $material") }
        }

        setBlockCommand.execute(server, zombachu, "/setblock dirt")
        assertEquals(["Set Dirt"], zombachu.logs)

        setBlockCommand.execute(server, zombachu, "/setblock podzol")
        assertEquals(["Set Grass"], zombachu.logs)

        assertEquals(
            Feedback.LiteralNotMatched(["dirt", "grass"], "bedrock"),
            setBlockCommand.executeExpectingError(server, zombachu, "/setblock bedrock"),
        )
    }
}
