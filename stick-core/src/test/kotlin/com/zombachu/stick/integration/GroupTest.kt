package com.zombachu.stick.integration

import com.zombachu.stick.Arguments1
import com.zombachu.stick.Arguments2
import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.dsl.booleanParameter
import com.zombachu.stick.dsl.branch
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.doubleParameter
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.id
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listElementParameter
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.store
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.subcommands
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.dsl.uuidParameter
import com.zombachu.stick.element.Parameter
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
import com.zombachu.stick.integration.fixtures.suggest
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
    fun `warp - requireIs hides inaccessible commands`() {
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
            Feedback.InvalidSyntax("/warp <info>"),
            warpCommand.executeExpectingError(server, steve, "/warp delete"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/warp <info>"),
            warpCommand.executeExpectingError(server, steve, "/warp tp spawn"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/warp tp <warp>"),
            warpCommand.executeExpectingError(server, zombachu, "/warp tp"),
        )

        assertEquals(["info"], warpCommand.suggest(server, steve, "/warp "))
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
    fun `portal - invalid syntax in branch returns branch syntax`() {
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
            Feedback.InvalidSyntax("/portal link <name>"),
            portalCommand.executeExpectingError(server, zombachu, "/portal link"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/portal link <name>"),
            portalCommand.executeExpectingError(server, zombachu, "/portal link nether overworld"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/portal <link|unlink>"),
            portalCommand.executeExpectingError(server, zombachu, "/portal delete"),
        )
    }

    @Test
    fun `portal - matched subcommand with flags does not fall through`() {
        val portalCommand = structure(Server::class, Sender::class) {
            command("portal")(
                group(
                    command("link")(
                        flag("confirm"),
                        stringParameter("name")
                    ) { _, name ->
                        sender.log("Linked $name")
                    },
                    command("unlink")(
                        flag("confirm"),
                        stringParameter("name")
                    ) { _, name ->
                        sender.log("Unlinked $name")
                    },
                    stringParameter("dimension"),
                )
            ) { result ->
                if (result is GroupResult.ResultC) {
                    sender.log("Teleported to ${result.value}")
                }
            }
        }

        portalCommand.execute(server, zombachu, "/portal nether")
        assertEquals(["Teleported to nether"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/portal link <name> [-confirm]"),
            portalCommand.executeExpectingError(server, zombachu, "/portal link"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/portal link <name> [-confirm]"),
            portalCommand.executeExpectingError(server, zombachu, "/portal link -confirm"),
        )
    }

    @Test
    fun `portal - invalid syntax in nested subcommand echoes typed args`() {
        val portalCommand = structure(Server::class, Sender::class) {
            command("portal", aliases = ["p"])(
                subcommands(
                    command("link")(
                        stringParameter("name"),
                        flag("confirm"),
                        subcommands(
                            command("to")(
                                stringParameter("destination")
                            )
                        ),
                    )
                )
            )
        }

        assertEquals(
            Feedback.InvalidSyntax("/p link -confirm nether to <destination>"),
            portalCommand.executeExpectingError(server, zombachu, "/p link -confirm nether to"),
        )
    }

    @Test
    fun `plot - subcommands can exceed group arity`() {
        val plotCommand = structure(Server::class, Sender::class) {
            command("plot")(
                subcommands(
                    command("claim")() { sender.log("Claimed plot") },
                    command("unclaim")() { sender.log("Unclaimed plot") },
                    command("home")() { sender.log("Teleported home") },
                    command("info")() { sender.log("Plot info") },
                    command("visit")(playerParameter("player")) { player -> sender.log("Visiting ${player.name}") },
                    command("trust")(playerParameter("player")) { player -> sender.log("Trusted ${player.name}") },
                    command("untrust")(playerParameter("player")) { player -> sender.log("Untrusted ${player.name}") },
                    command("deny")(playerParameter("player")) { player -> sender.log("Denied ${player.name}") },
                    command("kick")(playerParameter("player")) { player -> sender.log("Kicked ${player.name}") },
                )
            )
        }

        plotCommand.execute(server, zombachu, "/plot claim")
        assertEquals(["Claimed plot"], zombachu.logs)

        plotCommand.execute(server, zombachu, "/plot kick Steve")
        assertEquals(["Kicked Steve"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/plot <claim|unclaim|home|info|visit|trust|untrust|deny|kick>"),
            plotCommand.executeExpectingError(server, zombachu, "/plot sell"),
        )
    }

    @Test
    fun `warp - subcommands parse before fallback parameter`() {
        server.warps.add(Warp("list", "zombachu", "nether"))
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                group(
                    subcommands(
                        command("list")() {
                            sender.log("Warps: ${env.warps.names.joinToString(", ")}")
                        },
                        requireIs(Player::class) {
                            command("delete")(warpParameter("warp")) { warp ->
                                sender.log("Deleted ${warp.name}")
                            }
                        },
                    ),
                    warpParameter("warp"),
                )
            ) { target ->
                if (target is GroupResult.ResultB) {
                    sender.log("Teleporting to ${target.value.name}")
                }
            }
        }

        warpCommand.execute(server, zombachu, "/warp list")
        assertEquals(["Warps: spawn, shop, list"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp delete shop")
        assertEquals(["Deleted shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp shop")
        assertEquals(["Teleporting to shop"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/warp <list|delete|warp>"),
            warpCommand.executeExpectingError(server, zombachu, "/warp"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/warp <list|warp>"),
            warpCommand.executeExpectingError(server, console, "/warp"),
        )
    }

    @Test
    fun `warp - branch parses parameter before its subcommands`() {
        val targetWarp: TypedIdentifier<Warp> = id("warp")
        val warpCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                subcommands(
                    command("list")() {
                        sender.log("Warps: ${env.warps.names.joinToString(", ")}")
                    },
                    command("create")(
                        stringParameter("name")
                    ) { name ->
                        env.warps.add(Warp(name, sender.name, sender.world))
                        sender.log("Created warp $name")
                    },
                    branch(warpParameter("warp").store(targetWarp))(
                        subcommands(
                            command("tp")(
                                helper(targetWarp)
                            ) { warp ->
                                sender.world = warp.world
                                sender.log("Teleported to ${warp.name}")
                            },
                            command("delete")(
                                helper(targetWarp)
                            ) { warp ->
                                val unused = env.warps.remove(warp.name)
                                sender.log("Deleted ${warp.name}")
                            },
                        ),
                    ),
                )
            )
        }

        warpCommand.execute(server, zombachu, "/warp list")
        assertEquals(["Warps: spawn, shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp create home")
        assertEquals(["Created warp home"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp shop tp")
        assertEquals(["Teleported to shop"], zombachu.logs)
        assertEquals("nether", zombachu.world)

        warpCommand.execute(server, zombachu, "/warp shop delete")
        assertEquals(["Deleted shop"], zombachu.logs)
        assertEquals(["spawn", "home"], server.warps.names)
    }

    @Test
    fun `warp - branch suggests leading parameter`() {
        val targetWarp: TypedIdentifier<Warp> = id("warp")
        val warpCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                subcommands(
                    command("list")() {
                        sender.log("Warps: ${env.warps.names.joinToString(", ")}")
                    },
                    branch(warpParameter("warp").store(targetWarp))(
                        subcommands(
                            command("tp")(
                                helper(targetWarp)
                            ) { warp ->
                                sender.log("Teleported to ${warp.name}")
                            },
                            command("delete")(
                                helper(targetWarp)
                            ) { warp ->
                                sender.log("Deleted ${warp.name}")
                            },
                        ),
                    ),
                )
            )
        }

        assertEquals(["list", "spawn", "shop"], warpCommand.suggest(server, zombachu, "/warp "))
        assertEquals(["spawn", "shop"], warpCommand.suggest(server, zombachu, "/warp s"))
        assertEquals(["tp", "delete"], warpCommand.suggest(server, zombachu, "/warp shop "))
        assertEquals([], warpCommand.suggest(server, zombachu, "/warp nowhere "))
    }

    @Test
    fun `pay - unmatched branch falls through to next branch`() {
        val payCommand = structure(Server::class, Player::class) {
            command("pay")(
                subcommands(
                    branch(intParameter("amount"))(
                        playerParameter("player"),
                    ) { amount, player ->
                        sender.log("Paid ${player.name} $amount")
                    },
                    branch(playerParameter("player"))(
                        intParameter("amount"),
                    ) { player, amount ->
                        sender.log("Paid ${player.name} $amount")
                    },
                )
            )
        }

        payCommand.execute(server, zombachu, "/pay 5 Steve")
        assertEquals(["Paid Steve 5"], zombachu.logs)

        payCommand.execute(server, zombachu, "/pay Steve 5")
        assertEquals(["Paid Steve 5"], zombachu.logs)

        assertEquals(
            Feedback.InvalidSyntax("/pay <player> <amount>"),
            payCommand.executeExpectingError(server, zombachu, "/pay Steve"),
        )
    }

    @Test
    fun `warp - branch parses in group using result`() {
        val targetWarp: TypedIdentifier<Warp> = id("warp")
        val warpCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                group(
                    branch(warpParameter("warp").store(targetWarp))(
                        subcommands(
                            command("tp")(
                                helper(targetWarp)
                            ) { warp ->
                                sender.log("Teleported to ${warp.name}")
                            },
                        ),
                    ),
                    intParameter("page"),
                )
            ) { target ->
                if (target is GroupResult.ResultB) {
                    sender.log("Warps page ${target.value}")
                }
            }
        }

        warpCommand.execute(server, zombachu, "/warp shop tp")
        assertEquals(["Teleported to shop"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp 2")
        assertEquals(["Warps page 2"], zombachu.logs)
    }

    @Test
    fun `warp - branch with literal leading parameter parses before before sibling parameter`() {
        server.warps.add(Warp("all", "zombachu", "nether"))
        val warpCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                group(
                    warpParameter("warp"),
                    branch(literalParameter("all"))(
                        intParameter("page"),
                    ) { _, page ->
                        sender.log("All warps, page $page")
                    },
                )
            ) { target ->
                if (target is GroupResult.ResultA) {
                    sender.log("Warp ${target.value.name}")
                }
            }
        }

        warpCommand.execute(server, zombachu, "/warp all 2")
        assertEquals(["All warps, page 2"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/warp shop")
        assertEquals(["Warp shop"], zombachu.logs)
    }

    @Test
    fun `waypoint - branch with missing arguments falls through to next branch`() {
        val columnCommand = structure(Server::class, Player::class) {
            command("waypoint")(
                subcommands(
                    branch(PointParameter())() { point ->
                        sender.log("waypoint point $point")
                    },
                    branch(stringParameter("name"))() { name ->
                        sender.log("waypoint name $name")
                    },
                )
            )
        }

        columnCommand.execute(server, zombachu, "/waypoint 4 7")
        assertEquals(["waypoint point 4,7"], zombachu.logs)

        columnCommand.execute(server, zombachu, "/waypoint spawn")
        assertEquals(["waypoint name spawn"], zombachu.logs)
    }

    @Test
    fun `warp - subcommand parses before branch leading parameter`() {
        server.warps.add(Warp("list", "zombachu", "nether"))
        val targetWarp: TypedIdentifier<Warp> = id("warp")
        val warpCommand = structure(WarpableServer::class, Player::class) {
            command("warp")(
                subcommands(
                    command("list")() {
                        sender.log("Warps: ${env.warps.names.joinToString(", ")}")
                    },
                    branch(warpParameter("warp").store(targetWarp))(
                        subcommands(
                            command("tp")(
                                helper(targetWarp)
                            ) { warp ->
                                sender.log("Teleported to ${warp.name}")
                            },
                        ),
                    ),
                )
            )
        }

        assertEquals(
            Feedback.InvalidSyntax("/warp list"),
            warpCommand.executeExpectingError(server, zombachu, "/warp list tp"),
        )

        // KNOWN LIMITATION: the list subcommand should be resolved, but the group suggests branches by size, leading to
        // tp still being suggested
        // TODO: fix
        assertEquals(["tp"], warpCommand.suggest(server, zombachu, "/warp list "))
    }

    private class PointParameter<E : Environment, S> : Parameter.Size2<E, S, String>("point", "") {

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(arg0: String, arg1: String): CommandResult<String> = ParsingResult.success("$arg0,$arg1")
    }
}
