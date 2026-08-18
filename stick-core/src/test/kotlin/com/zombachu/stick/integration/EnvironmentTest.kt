package com.zombachu.stick.integration

import com.zombachu.stick.Arguments1
import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult3
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpParameter
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.executeWithHandler
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.valueOrPropagateError
import kotlin.test.Test
import kotlin.test.assertEquals

class EnvironmentTest {

    private val zombachu = Player("zombachu", ["server.world.set"])
    private val steve = Player("Steve")
    private val server = SynergyServer([zombachu, steve], WarpRegistry([Warp("shop", "zombachu", "nether")]))

    @Test
    fun `world + version - commands run narrower environments`() {
        class WorldCommand : Command<Server, Sender> {
            override val structure = structure {
                command("world")(
                    playerParameter("player"),
                ) { player -> sender.log("${player.name} is in ${player.world}.") }
            }
        }
        val worldCommand: Structure<WarpableServer, Sender, *> = WorldCommand().structure
        class VersionCommand : Command<Environment, Sender> {
            override val structure = structure {
                command("version")() {
                    sender.log("Stick 0.4.0")
                }
            }
        }
        val versionCommand: Structure<SynergyServer, Sender, *> = VersionCommand().structure

        worldCommand.execute(server, zombachu, "/world steve")
        assertEquals(["Steve is in overworld."], zombachu.logs)

        versionCommand.execute(server, zombachu, "/version")
        assertEquals(["Stick 0.4.0"], zombachu.logs)
    }

    @Test
    fun `nonsense - elements compose into narrower environments`() {
        val baseScope = StructureScope.empty<Server, Sender>()
        val worldFlag: ValueFlag<WarpableServer, Sender, String> =
            with(baseScope) { valueFlag("world", default = "overworld", parameter = stringParameter("world")) }
        val worldParameter: Groupable<WarpableServer, Sender, String> = WorldParameter<Server, Sender>("world")
        val someSubCommand: Structure<WarpableServer, Sender, Arguments1<String>> =
            with(baseScope) { command("somesubcommand")(WorldParameter<Server, Sender>("world")) { _ -> } }
        // Shouldn't compile
        // val warpParameter: Groupable<GameServer, Sender, Warp> =
        //     with(StructureScope.empty<HasWarps, Sender>()) { warpParameter("warp") }
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("nonsense")(
                worldFlag,
                group(
                    someSubCommand,
                    command("anothersubcommand")(
                        warpParameter("warp")
                    ) { warp ->
                        sender.log("anothersubcommand: ${warp.name}")
                    },
                    worldParameter,
                ),
            ) { world: String, selection: GroupResult3<Arguments1<String>, Arguments1<Warp>, String> ->
                when (selection) {
                    is GroupResult.ResultA -> sender.log("Base subcommand = ${selection.value.a}, flag = $world")
                    is GroupResult.ResultB ->
                        sender.log("Warpable subcommand = ${selection.value.a.name}, flag = $world")
                    is GroupResult.ResultC -> sender.log("Bare parameter = ${selection.value}, flag = $world")
                }
            }
        }

        warpCommand.execute(server, zombachu, "/nonsense somesubcommand nether")
        assertEquals(["Base subcommand = nether, flag = overworld"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/nonsense anothersubcommand shop")
        assertEquals(["anothersubcommand: shop", "Warpable subcommand = shop, flag = overworld"], zombachu.logs)

        warpCommand.execute(server, zombachu, "/nonsense -world end nether")
        assertEquals(["Bare parameter = nether, flag = end"], zombachu.logs)
    }

    @Test
    fun `spawn - base environment parameter composes into narrower optional`() {
        val worldCommand = structure(WarpableServer::class, Sender::class) {
            command("spawn")(
                optionally(
                    ifInvalid = invalidDefault("lobby", permission("server.world.set")),
                    ifAbsent = default("overworld"),
                    parameter = WorldParameter<Server, Sender>("world"),
                )
            ) { world -> sender.log("Teleporting to $world spawn") }
        }

        worldCommand.execute(server, zombachu, "/spawn")
        assertEquals(["Teleporting to overworld spawn"], zombachu.logs)

        worldCommand.execute(server, zombachu, "/spawn nether")
        assertEquals(["Teleporting to nether spawn"], zombachu.logs)

        worldCommand.execute(server, steve, "/spawn")
        assertEquals(["Teleporting to lobby spawn"], steve.logs)

        assertEquals(Feedback.InvalidPermission, worldCommand.executeExpectingError(server, steve, "/spawn nether"))
    }

    @Test
    fun `warpcount - commands have access to environment services`() {
        val warpCountCommand = structure(WarpableServer::class, Sender::class) {
            command("warpcount")() {
                sender.log("There are ${env.warps.names.size} warps")
            }
        }

        warpCountCommand.execute(server, zombachu, "/warpcount")
        assertEquals(["There are 1 warps"], zombachu.logs)
    }

    @Test
    fun `warpowner - base failure handler works for narrower environments`() {
        class BaseFailureHandler : FailureHandler<Server, Sender> {
            context(inv: Invocation<Server, Sender>)
            override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
                inv.sender.log(failure.feedback.message)
            }
        }
        val ownerCommand = structure(WarpableServer::class, Sender::class) {
            command("warpowner")(
                warpParameter("warp")
            ) { warp -> sender.log("Warp ${warp.name} belongs to ${warp.owner}") }
        }

        ownerCommand.execute(server, zombachu, "/warpowner shop")
        assertEquals(["Warp shop belongs to zombachu"], zombachu.logs)

        val handler: FailureHandler<WarpableServer, Sender> = BaseFailureHandler()
        ownerCommand.executeWithHandler(handler, server, zombachu, "/warpowner nowhere")
        assertEquals(["Unknown warp: nowhere"], zombachu.logs)
    }

    @Test
    fun `warpname - parameter with bound environment but general dsl only works in bound environment`() {
        fun <E : Environment, S> StructureScope<E, S>.warpNameParameter(
            name: String,
        ): Parameter.Size1<WarpableServer, S, String> = WarpNameParameter(name)
        val warpNameCommand = structure(WarpableServer::class, Sender::class) {
            command("warpname",)(
                warpNameParameter("warp")
            ) { name -> sender.log("Warp resolved to $name") }
        }

        // Should not compile
        // val plain = structure(GameServer::class, Sender::class) {
        //     command("warpname")(warpNameParameter("warp")) { _ -> }
        // }

        warpNameCommand.execute(server, zombachu, "/warpname SHOP")
        assertEquals(["Warp resolved to shop"], zombachu.logs)
    }

    private class WorldParameter<E : Server, S>(name: String) : Parameter.Size1<E, S, String>(name, "") {
        context(inv: Invocation<E, S>)
        override fun parse(arg0: String): CommandResult<String> = ParsingResult.success(arg0)
    }

    private class WarpNameParameter<S>(name: String) : Parameter.Size1<WarpableServer, S, String>(name, "") {
        private val warpParameter = WarpParameter<WarpableServer, S>(name)

        context(inv: Invocation<WarpableServer, S>)
        override fun parse(arg0: String): CommandResult<String> {
            val warp = warpParameter.parse(arg0).valueOrPropagateError { return it }
            return ParsingResult.success(warp.name)
        }
    }
}
