package com.zombachu.stick.integration

import com.zombachu.stick.Arguments1
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.nullableValueFlag
import com.zombachu.stick.dsl.require
import com.zombachu.stick.dsl.requireAs
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SocialData
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.bioParameter
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.realNameParameter
import com.zombachu.stick.integration.fixtures.requireSocialData
import kotlin.test.Test
import kotlin.test.assertEquals

class SenderRequirementTest {

    private val zombachu = Player("zombachu", ["server.broadcast", "server.whois.ip", "server.echo"])
    private val steve = Player("Steve")
    private val console = Console()
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `broadcast - permission validates sender permission`() {
        val broadcastCommand = structure(Server::class, Sender::class) {
            command(name = "broadcast", requirement = permission("server.broadcast"))(
                stringParameter("message")
            ) { message ->
                sender.log("[Server] $message")
            }
        }

        assertEquals(
            Feedback.InvalidPermission,
            broadcastCommand.executeExpectingError(server, steve, "/broadcast Hello"),
        )

        broadcastCommand.execute(server, zombachu, "/broadcast Hello")
        assertEquals(["[Server] Hello"], zombachu.logs)
    }

    @Test
    fun `spawn - requireIs validates and transforms sender`() {
        val spawnCommand = structure(Server::class, Sender::class) {
            requireIs(Player::class) {
                command("spawn")() {
                    sender.world = "overworld"
                    sender.log("Teleported to spawn")
                }
            }
        }

        spawnCommand.execute(server, zombachu, "/spawn")
        assertEquals(["Teleported to spawn"], zombachu.logs)
    }

    @Test
    fun `whois - group validates elements`() {
        val whoisCommand = structure(Server::class, Sender::class) {
            command("whois")(
                group(
                    requireIs(Player::class) {
                        literalParameter("me")
                    },
                    command("ip", requirement = permission("server.whois.ip"), aliases = ["address"])(
                        stringParameter("address"),
                    ) { address: String ->
                        sender.log("Looked up $address")
                    },
                    playerParameter("player"),
                )
            ) { result ->
                when (result) {
                    is GroupResult.ResultA -> sender.log("You are ${sender.name}, in ${(sender as Player).world}")
                    is GroupResult.ResultB -> Unit
                    is GroupResult.ResultC -> sender.log("${result.value.name} is in ${result.value.world}")
                }
            }
        }

        whoisCommand.execute(server, zombachu, "/whois me")
        assertEquals(["You are zombachu, in overworld"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/whois <ip|player>"),
            whoisCommand.executeExpectingError(server, console, "/whois me"),
        )

        whoisCommand.execute(server, zombachu, "/whois address 127.0.0.1")
        assertEquals(["Looked up 127.0.0.1"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/whois <me|player>"),
            whoisCommand.executeExpectingError(server, steve, "/whois address 127.0.0.1"),
        )
    }

    @Test
    fun `bio - requireAs validates and transforms sender for parameter`() {
        val bioCommand = structure(Server::class, Sender::class) {
            command("bio")(
                group(
                    literalParameter("read"),
                    requireSocialData { bioParameter("text") },
                )
            ) { result ->
                when (result) {
                    is GroupResult.ResultA -> sender.log("todo")
                    is GroupResult.ResultB -> sender.log("Added to bio: ${result.value}")
                }
            }
        }

        bioCommand.execute(server, zombachu, "/bio My name is zombachu")
        assertEquals(["Added to bio: My name is zombachu"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/bio <read>"),
            bioCommand.executeExpectingError(server, console, "/bio My name is Console"),
        )
    }

    @Test
    fun `realname - requireAs validates and transforms sender for command`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                group(
                    literalParameter("me"),
                    requireAs<Server, Sender, SocialData, Arguments1<String>>(
                        { (it as Player).socialData },
                        requirement { it.sender is Player },
                    ) {
                        command("player")(
                            realNameParameter("nickname")
                        ) { realName ->
                            sender.player.log("That player's real name is: $realName")
                        }
                    },
                )
            )
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname player Alexandra")
        assertEquals(["That player's real name is: Alex"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/realname <me>"),
            realNameCommand.executeExpectingError(server, console, "/realname Alexandra"),
        )
    }

    @Test
    fun `realname - requireAs validates and transforms sender for flag`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                requireAs(
                    { (it as Player).socialData },
                    invalidDefault("   ", requirement { it.sender is Player }),
                ) {
                    nullableValueFlag(name = "nickname", parameter = realNameParameter("name"))
                }
            ) { realName ->
                if (realName == null) {
                    sender.log("Your name is ${sender.name}")
                } else if (realName == "   ") {
                    sender.log("Your name is Console")
                } else {
                    sender.log("That player's real name is $realName")
                }
            }
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname -nickname Alexandra")
        assertEquals(["That player's real name is Alex"], zombachu.logs)

        realNameCommand.execute(server, zombachu, "/realname")
        assertEquals(["Your name is zombachu"], zombachu.logs)

        realNameCommand.execute(server, console, "/realname")
        assertEquals(["Your name is Console"], console.logs)
    }

    @Test
    fun `realname - requireAs validates and transforms sender for hybrid flag`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                requireAs<Server, Sender, SocialData, String>(
                    { (it as Player).socialData },
                    invalidDefault(HybridFlagResult.Absent(), requirement { it.sender is Player }),
                ) {
                    hybridFlag("nickname", realNameParameter("name"))
                }
            ) { realName ->
                when (realName) {
                    is HybridFlagResult.Absent -> sender.log("Your name is ${sender.name}")
                    is HybridFlagResult.Present -> sender.log("Your nickname is *")
                    is HybridFlagResult.Value -> sender.log("That player's real name is ${realName.value}")
                }
            }
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname -nickname Alexandra")
        assertEquals(["That player's real name is Alex"], zombachu.logs)

        realNameCommand.execute(server, zombachu, "/realname -nickname")
        assertEquals(["Your nickname is *"], zombachu.logs)

        realNameCommand.execute(server, zombachu, "/realname")
        assertEquals(["Your name is zombachu"], zombachu.logs)

        realNameCommand.execute(server, console, "/realname")
        assertEquals(["Your name is Console"], console.logs)

        assertEquals(
            Feedback.InvalidSyntax("/realname"),
            realNameCommand.executeExpectingError(server, console, "/realname -nickname"),
        )
    }

    @Test
    fun `home - requireAs swaps the sender for the whole command scope`() {
        val bioLineCommand = structure(Server::class, Sender::class) {
            requireSocialData {
                command("bio")(
                    bioParameter("line")
                ) { bioLine ->
                    sender.bio += bioLine
                    sender.player.log("Added '$bioLine' to your bio")
                }
            }
        }

        bioLineCommand.execute(server, zombachu, "/bio My name is zombachu")
        assertEquals(["My name is zombachu"], zombachu.socialData.bio)
    }

    @Test
    fun `echo - require gates linear element`() {
        val echoCommand = structure(Server::class, Sender::class) {
            command("echo")(
                require(permission("server.echo")) { stringParameter("text") }
            ) { text ->
                sender.log(text)
            }
        }

        echoCommand.execute(server, zombachu, "/echo hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(Feedback.InvalidPermission, echoCommand.executeExpectingError(server, steve, "/echo hello"))
    }

    @Test
    fun `echo - requireIs gates linear element`() {
        val echoCommand = structure(Server::class, Sender::class) {
            command("echo")(
                requireIs(Player::class) { stringParameter("text") }
            ) { text ->
                sender.log(text)
            }
        }

        echoCommand.execute(server, zombachu, "/echo hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(Feedback.InvalidSenderType, echoCommand.executeExpectingError(server, console, "/echo hello"))
    }

    @Test
    fun `echo - require gates command`() {
        val echoCommand = structure(Server::class, Sender::class) {
            require(permission("server.echo")) {
                command("echo")(
                    stringParameter("text")
                ) { text ->
                    sender.log(text)
                }
            }
        }

        echoCommand.execute(server, zombachu, "/echo hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(Feedback.InvalidPermission, echoCommand.executeExpectingError(server, steve, "/echo hello"))
    }

    @Test
    fun `echo - requireIs gates command`() {
        val echoCommand = structure(Server::class, Sender::class) {
            requireIs(Player::class) {
                command("echo")(
                    stringParameter("text")
                ) { text ->
                    sender.log(text)
                }
            }
        }

        echoCommand.execute(server, zombachu, "/echo hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(Feedback.InvalidSenderType, echoCommand.executeExpectingError(server, console, "/echo hello"))
    }

    @Test
    fun `echo - requireIs gates subcommand`() {
        val echoCommand = structure(Server::class, Sender::class) {
            command("echo")(
                requireIs(Player::class) {
                    command("raw")(
                        stringParameter("text")
                    ) { text ->
                        sender.log(text)
                    }
                }
            )
        }

        echoCommand.execute(server, zombachu, "/echo raw hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSenderType,
            echoCommand.executeExpectingError(server, console, "/echo raw hello"),
        )
    }
}
