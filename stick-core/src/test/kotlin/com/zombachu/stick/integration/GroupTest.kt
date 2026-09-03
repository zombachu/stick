package com.zombachu.stick.integration

import com.zombachu.stick.Arguments1
import com.zombachu.stick.Arguments2
import com.zombachu.stick.Command
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.dsl.booleanParameter
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.doubleParameter
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listElementParameter
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.dsl.uuidParameter
import com.zombachu.stick.element.parameters.ListElementResult
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.Weather
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.integration.fixtures.worldHelper
import kotlin.test.Test
import kotlin.test.assertEquals

class GroupTest {

    private val zombachu = Player("zombachu", ["server.warp", "server.warp.tp", "server.warp.create"])
    private val steve = Player("Steve", ["server.warp"])
    private val console = Console()
    private val server =
        SynergyServer(
            [zombachu, steve],
            WarpRegistry([Warp("spawn", "Console", "overworld"), Warp("shop", "zombachu", "nether")]),
        )

    @Test
    fun `warp - subcommands can be defined separately`() {
        class WarpTpCommand : Command<WarpableServer, Player> {
            override val structure = structure {
                command(name = "tp", aliases = ["goto"], requirement = permission("server.warp.tp"))(
                    warpParameter("warp"),
                ) { warp ->
                    sender.world = warp.world
                    sender.log("Teleported to ${warp.name}")
                }
            }
        }
        class WarpCreateCommand : Command<WarpableServer, Player> {
            override val structure = structure {
                command(name = "create", requirement = permission("server.warp.create"))(
                    worldHelper(),
                    stringParameter("name"),
                ) { world, name ->
                    env.warps.add(Warp(name, sender.name, world))
                    sender.log("Created warp $name in $world")
                }
            }
        }
        class WarpInfoCommand : Command<WarpableServer, Sender> {
            override val structure = structure {
                command(name = "info")(
                    group(
                        literalParameter("all"),
                        listElementParameter("index", { ParsingResult.success(env.warps.warps.values.toList()) }),
                    ),
                ) { selection ->
                    val warps = env.warps.names
                    when (selection) {
                        is GroupResult.ResultA -> sender.log("Warps: ${warps.joinToString(", ")}")
                        is GroupResult.ResultB -> {
                            val warp = selection.value.result
                            sender.log("Info about ${warp.name}: $warp")
                        }
                    }
                }
            }
        }
        class WarpCommand : Command<WarpableServer, Sender> {
            override val structure = structure {
                command(name = "warp", aliases = ["warps"], requirement = permission("server.warp"))(
                    group(
                        requireIs(Player::class) { WarpTpCommand().structure },
                        requireIs(Player::class) { WarpCreateCommand().structure },
                        WarpInfoCommand().structure,
                    )
                )
            }
        }
        val warpCommand = WarpCommand().structure

        warpCommand.execute(server, zombachu, "/warp info all")
        assertEquals(["Warps: spawn, shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warps info all")
        assertEquals(["Warps: spawn, shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp tp shop")
        assertEquals("nether", zombachu.world)

        warpCommand.execute(server, zombachu, "/warp goto spawn")
        assertEquals("overworld", zombachu.world)

        warpCommand.execute(server, zombachu, "/warp info 1")
        assertEquals(["Info about shop: Warp(name=shop, owner=zombachu, world=nether)"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/warp <tp|create|info>"),
            warpCommand.executeExpectingError(server, zombachu, "/warp delete"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/warp <info>"),
            warpCommand.executeExpectingError(server, console, "/warp delete"),
        )
    }

    @Test
    fun `KNOWN LIMITATION - warp - requireIs makes inaccessible commands visible`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp", requirement = permission("server.warp"))(
                group(
                    requireIs(Player::class) {
                        command("tp", requirement = permission("server.warp.tp"))(
                            warpParameter("warp"),
                        ) { warp ->
                            sender.world = warp.world
                        }
                    },
                    command("info")() {},
                )
            )
        }

        assertEquals(
            Feedback.InvalidSyntax("/warp <tp|info>"),
            warpCommand.executeExpectingError(server, steve, "/warp delete"),
        )

        assertEquals(
            Feedback.InvalidPermission,
            warpCommand.executeExpectingError(server, steve, "/warp tp spawn"),
        )
    }

    @Test
    fun `warp - subcommands can be defined inline`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                group(
                    requireIs(Player::class) {
                        command("tp", requirement = permission("server.warp.tp"))(
                            warpParameter("warp"),
                        ) { warp ->
                            sender.world = warp.world
                            sender.log("Teleported to ${warp.name}")
                        }
                    },
                    command("info")(
                        group(
                            literalParameter("all"),
                            listElementParameter("index", { ParsingResult.success(env.warps.warps.values.toList()) }),
                        ),
                    ) { selection ->
                        val warps = env.warps.names
                        when (selection) {
                            is GroupResult.ResultA -> sender.log("Warps: ${warps.joinToString(", ")}")
                            is GroupResult.ResultB -> {
                                val warp = selection.value.result
                                sender.log("Info about ${warp.name}: $warp")
                            }
                        }
                    },
                    requireIs(Player::class) {
                        command("create", requirement = permission("server.warp.create"))(
                            worldHelper(),
                            stringParameter("name"),
                        ) { world, name ->
                            env.warps.add(Warp(name, sender.name, world))
                            sender.log("Created warp $name in $world")
                        }
                    },
                    stringParameter("foo"),
                    enumParameter("bar", Weather::class),
                )
            ) {
                selection: GroupResult5<
                        Arguments1<Warp>,
                        Arguments1<GroupResult2<String, ListElementResult<Warp>>>,
                        Arguments2<String, String>,
                        String,
                        Weather> ->
                when (selection) {
                    is GroupResult.ResultA -> sender.log("Result A: Teleported to ${selection.value.a.name}")
                    is GroupResult.ResultB -> sender.log("Result B: ${selection.value}")
                    is GroupResult.ResultC -> sender.log("Result C: ${selection.value.b.uppercase()} created")
                    is GroupResult.ResultD -> sender.log("Result D: World ${selection.value.lowercase()}")
                    is GroupResult.ResultE -> sender.log("Result E: Weather ${selection.value.label}")
                }
            }
        }

        warpCommand.execute(server, zombachu, "/warp info all")
        assertEquals(["Warps: spawn, shop", "Result B: Arguments1(a=ResultA(value=all))"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp hub")
        assertEquals(["Result D: World hub"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp tp shop")
        assertEquals(["Teleported to shop", "Result A: Teleported to shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp create hub")
        assertEquals(["Created warp hub in nether", "Result C: HUB created"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp storm")
        assertEquals(["Result E: Weather storm"], zombachu.logs)
    }

    @Test
    fun `foo - parameters parse by priority`() {
        val fooCommand = structure(WarpableServer::class, Sender::class) {
            command("foo")(
                group(
                    intParameter("int"),
                    doubleParameter("double"),
                    booleanParameter("boolean"),
                    literalParameter("literal"),
                    uuidParameter("uuid"),
                    enumParameter("weather", Weather::class),
                    textParameter("text"),
                    playerParameter("player"),
                )
            ) { selection ->
                when (selection) {
                    is GroupResult.ResultA -> sender.log("ResultA, ${selection.value + 1}")
                    is GroupResult.ResultB -> sender.log("ResultB, ${selection.value / 2}")
                    is GroupResult.ResultC -> sender.log("ResultC, ${!selection.value}")
                    is GroupResult.ResultD -> sender.log("ResultD, ${selection.value.uppercase()}")
                    is GroupResult.ResultE -> sender.log("ResultE, ${selection.value.version()}")
                    is GroupResult.ResultF -> sender.log("ResultF, ${selection.value.label}")
                    is GroupResult.ResultG -> sender.log("ResultH, ${selection.value.split(" ").size}")
                    is GroupResult.ResultH -> sender.log("ResultG, ${selection.value.name}, ${selection.value.world}")
                }
            }
        }

        fooCommand.execute(server, zombachu, "/foo 7")
        assertEquals(["ResultA, 8"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo 2.5")
        assertEquals(["ResultB, 1.25"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo true")
        assertEquals(["ResultC, false"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo literal")
        assertEquals(["ResultD, LITERAL"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo 8ee7c2d6-0f1a-4c3b-9f5e-1d2a3b4c5d6e")
        assertEquals(["ResultE, 4"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo thunder")
        assertEquals(["ResultF, storm"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo zombachu")
        assertEquals(["ResultG, zombachu, overworld"], zombachu.logs)

        fooCommand.execute(server, zombachu, "/foo the quick brown fox")
        assertEquals(["ResultH, 4"], zombachu.logs)
    }

    @Test
    fun `warp - unmatched argument returns parsing error of last element`() {
        val describeCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                group(
                    command("list")() {
                        sender.log("Warps: ${env.warps.names.joinToString(", ")}")
                    },
                    warpParameter("warp"),
                )
            ) { target ->
                if (target is GroupResult.ResultB) {
                    sender.log("Teleporting to ${target.value.name} in ${target.value.world}")
                }
            }
        }

        describeCommand.execute(server, zombachu, "/warp list")
        assertEquals(["Warps: spawn, shop"], zombachu.logs)

        describeCommand.execute(server, zombachu, "/warp shop")
        assertEquals(["Teleporting to shop in nether"], zombachu.logs)

        assertEquals(
            "Unknown warp: nowhere",
            describeCommand.executeExpectingError(server, zombachu, "/warp nowhere").message,
        )
    }

    @Test
    fun `cookie - parameter parses after finite-size group`() {
        val cookieCommand = structure(Server::class, Sender::class) {
            command("cookie")(
                group(
                    playerParameter("player"),
                    literalParameter("everyone")),
                intParameter("amount"),
            ) { recipient, amount ->
                when (recipient) {
                    is GroupResult.ResultA -> sender.log("Gave ${recipient.value.name} $amount cookies")
                    is GroupResult.ResultB -> sender.log("Gave everyone $amount cookies")
                }
            }
        }

        cookieCommand.execute(server, zombachu, "/cookie zombachu 10")
        assertEquals(["Gave zombachu 10 cookies"], zombachu.logs)

        cookieCommand.execute(server, zombachu, "/cookie everyone 50")
        assertEquals(["Gave everyone 50 cookies"], zombachu.logs)
    }

    @Test
    fun `KNOWN LIMITATION - portal - invalid syntax in branch returns root syntax`() {
        val portalCommand = structure(Server::class, Sender::class) {
            command("portal")(
                group(
                    command("link")(
                        stringParameter("name")) { name ->
                        sender.log("Linked $name")
                    },
                    command("unlink")(
                        stringParameter("name")) { name ->
                        sender.log("Unlinked $name")
                    },
                )
            )
        }

        portalCommand.execute(server, zombachu, "/portal link nether")
        assertEquals(["Linked nether"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/portal <link|unlink>"),
            portalCommand.executeExpectingError(server, zombachu, "/portal link"),
        )
    }

}
