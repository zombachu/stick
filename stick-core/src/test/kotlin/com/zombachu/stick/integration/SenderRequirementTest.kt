package com.zombachu.stick.integration

import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.dsl.branch
import com.zombachu.stick.dsl.branchMapSender
import com.zombachu.stick.dsl.branchRequireSender
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.mapSender
import com.zombachu.stick.dsl.optional
import com.zombachu.stick.dsl.require
import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.subcommands
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SocialData
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.bioParameter
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.executeExpectingInvalidSyntax
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.realNameParameter
import com.zombachu.stick.integration.fixtures.requireSocialData
import kotlin.test.Test
import kotlin.test.assertEquals

class SenderRequirementTest {

    private val zombachu = Player("zombachu", ["server.broadcast", "server.whois.ip", "server.echo", "server.get"])
    private val steve = Player("Steve")
    private val console = Console()
    private val server = SynergyServer([zombachu, steve])

    // KNOWN LIMITATION: mapSender's overloads differ only in the block's return type, so a lambda-literal or
    // callable-reference transform is ambiguous even with explicit type arguments; it needs a function-typed value
    // TODO: fix
    private val toSocialData: (Player) -> SocialData = { it.socialData }

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
            Reason.InvalidPermission,
            broadcastCommand.executeExpectingError(server, steve, "/broadcast Hello"),
        )

        broadcastCommand.execute(server, zombachu, "/broadcast Hello")
        assertEquals(["[Server] Hello"], zombachu.logs)
    }

    @Test
    fun `spawn - requireSender validates and transforms sender`() {
        val spawnCommand = structure(Server::class, Sender::class) {
            requireSender(Player::class) {
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
                    requireSender(Player::class) {
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
            "/whois <ip|player>",
            whoisCommand.executeExpectingInvalidSyntax(server, console, "/whois me"),
        )

        whoisCommand.execute(server, zombachu, "/whois address 127.0.0.1")
        assertEquals(["Looked up 127.0.0.1"], zombachu.logs)

        assertEquals(
            "/whois <me|player>",
            whoisCommand.executeExpectingInvalidSyntax(server, steve, "/whois address 127.0.0.1"),
        )
    }

    @Test
    fun `bio - mapSender transforms narrowed sender for parameter`() {
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
            "/bio <read>",
            bioCommand.executeExpectingInvalidSyntax(server, console, "/bio My name is Console"),
        )
    }

    @Test
    fun `realname - mapSender transforms narrowed sender for command`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                group(
                    literalParameter("me"),
                    requireSender(Player::class) {
                        mapSender(toSocialData) {
                            command("player")(
                                realNameParameter("nickname")
                            ) { realName ->
                                sender.player.log("That player's real name is: $realName")
                            }
                        }
                    },
                )
            )
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname player Alexandra")
        assertEquals(["That player's real name is: Alex"], zombachu.logs)

        assertEquals(
            "/realname <me>",
            realNameCommand.executeExpectingInvalidSyntax(server, console, "/realname Alexandra"),
        )
    }

    @Test
    fun `realname - mapSender transforms narrowed sender for branch`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                group(
                    literalParameter("me"),
                    branchRequireSender(Player::class) {
                        branchMapSender({ it.socialData }) {
                            branch(realNameParameter("nickname"))() { realName ->
                                sender.player.log("That player's real name is: $realName")
                            }
                        }
                    },
                )
            )
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname Alexandra")
        assertEquals(["That player's real name is: Alex"], zombachu.logs)

        assertEquals(
            "/realname <me>",
            realNameCommand.executeExpectingInvalidSyntax(server, console, "/realname Alexandra"),
        )
    }

    @Test
    fun `realname - mapSender transforms narrowed sender for flag`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                requireSender(Player::class, default = "   ") {
                    mapSender(toSocialData) {
                        valueFlag(name = "nickname", parameter = realNameParameter("name"), default = null)
                    }
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
    fun `realname - mapSender transforms narrowed sender for hybrid flag`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                requireSender(Player::class) {
                    mapSender(toSocialData) {
                        hybridFlag("nickname", realNameParameter("name"))
                    }
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
            "/realname",
            realNameCommand.executeExpectingInvalidSyntax(server, console, "/realname -nickname"),
        )
    }

    @Test
    fun `realname - mapSender transforms narrowed sender for optional`() {
        val realNameCommand = structure(Server::class, Sender::class) {
            command("realname")(
                requireSender(Player::class, default = null) {
                    mapSender(toSocialData) {
                        optional(realNameParameter("name"), default = null)
                    }
                }
            ) { realName ->
                if (realName == null) {
                    sender.log("Your name is ${sender.name}")
                } else {
                    sender.log("That player's real name is $realName")
                }
            }
        }
        zombachu.socialData.nicknames["Alex"] = "Alexandra"

        realNameCommand.execute(server, zombachu, "/realname Alexandra")
        assertEquals(["That player's real name is Alex"], zombachu.logs)

        realNameCommand.execute(server, zombachu, "/realname")
        assertEquals(["Your name is zombachu"], zombachu.logs)

        realNameCommand.execute(server, console, "/realname")
        assertEquals(["Your name is Console"], console.logs)

        assertEquals(
            Reason.InvalidSenderType(Player::class),
            realNameCommand.executeExpectingError(server, console, "/realname Alexandra"),
        )
    }

    @Test
    fun `home - mapSender swaps the sender for the whole command scope`() {
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
    fun `echo - requireSender gates command`() {
        val echoCommand = structure(Server::class, Sender::class) {
            requireSender(Player::class) {
                command("echo")(
                    stringParameter("text")
                ) { text ->
                    sender.log(text)
                }
            }
        }

        echoCommand.execute(server, zombachu, "/echo hello")
        assertEquals(["hello"], zombachu.logs)

        assertEquals(
            Reason.InvalidSenderType(Player::class),
            echoCommand.executeExpectingError(server, console, "/echo hello"),
        )
    }

    @Test
    fun `echo - requireSender gates subcommand`() {
        val echoCommand = structure(Server::class, Sender::class) {
            command("echo")(
                subcommands(
                    requireSender(Player::class) {
                        command("raw")(
                            stringParameter("text")
                        ) { text ->
                            sender.log(text)
                        }
                    },
                )
            )
        }

        echoCommand.execute(server, zombachu, "/echo raw hello")
        assertEquals(["hello"], zombachu.logs)

        // KNOWN LIMITATION: if all groupables are inaccessible the syntax renders as <>
        // TODO: fix
        assertEquals(
            "/echo <>",
            echoCommand.executeExpectingInvalidSyntax(server, console, "/echo raw hello"),
        )
    }

    @Test
    fun `get - require wraps requireSender on parameter`() {
        val getCommand = structure(Server::class, Sender::class) {
            command("get")(
                group(
                    require(permission("server.get")) {
                        requireSender(Player::class) {
                            literalParameter("world")
                        }
                    },
                    literalParameter("name"),
                )
            ) { result ->
                when (result) {
                    is GroupResult.ResultA -> sender.log((sender as Player).world)
                    is GroupResult.ResultB -> sender.log(sender.name)
                }
            }
        }

        getCommand.execute(server, zombachu, "/get world")
        assertEquals(["overworld"], zombachu.logs)

        assertEquals("/get <name>", getCommand.executeExpectingInvalidSyntax(server, steve, "/get world"))

        assertEquals("/get <name>", getCommand.executeExpectingInvalidSyntax(server, console, "/get world"))
    }

    @Test
    fun `get - requireSender narrows sender for nested require on parameter`() {
        val getCommand = structure(Server::class, Sender::class) {
            command("get")(
                group(
                    requireSender(Player::class) {
                        require(requirement { sender.world == "nether" }) {
                            literalParameter("world")
                        }
                    },
                    literalParameter("name"),
                )
            ) { result ->
                when (result) {
                    is GroupResult.ResultA -> sender.log((sender as Player).world)
                    is GroupResult.ResultB -> sender.log(sender.name)
                }
            }
        }
        steve.world = "nether"

        getCommand.execute(server, steve, "/get world")
        assertEquals(["nether"], steve.logs)

        assertEquals("/get <name>", getCommand.executeExpectingInvalidSyntax(server, zombachu, "/get world"))

        assertEquals("/get <name>", getCommand.executeExpectingInvalidSyntax(server, console, "/get world"))
    }
}
