package com.zombachu.stick.integration

import com.zombachu.stick.Arguments1
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.requireAs
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Location
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.realNameParameter
import com.zombachu.stick.integration.fixtures.requireSocialData
import com.zombachu.stick.integration.fixtures.warpParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomElementTest {

    private val zombachu = Player("zombachu")
    private val steve = Player("Steve")
    private val server = SynergyServer([zombachu, steve], WarpRegistry([Warp("shop", "zombachu", "nether")]))

    @Test
    fun `tppos - fixed size parameter`() {
        class LocationParameter<E : Environment, S>(name: String) : Parameter.Size3<E, S, Location>(name, "") {
            context(inv: Invocation<E, S>)
            override fun parse(arg0: String, arg1: String, arg2: String): CommandResult<Location> {
                val x = arg0.toIntOrNull() ?: return ParsingResult.failType("integer", arg0)
                val y = arg1.toIntOrNull() ?: return ParsingResult.failType("integer", arg1)
                val z = arg2.toIntOrNull() ?: return ParsingResult.failType("integer", arg2)
                return ParsingResult.success(Location(x, y, z))
            }
        }
        val tpPosCommand = structure(Server::class, Player::class) {
            command("tppos")(
                LocationParameter("position")
            ) { position ->
                sender.position = position
            }
        }

        tpPosCommand.execute(server, zombachu, "/tppos 10 70 -4")
        assertEquals(Location(10, 70, -4), zombachu.position)

        assertEquals(
            Feedback.TypeNotMatched("integer", "red"),
            tpPosCommand.executeExpectingError(server, zombachu, "/tppos red green blue"),
        )

        assertEquals(
            Feedback.InvalidSyntax("/tppos <position>"),
            tpPosCommand.executeExpectingError(server, zombachu, "/tppos 10 70"),
        )
    }

    @Test
    fun `setspawn - unbounded size parameter`() {
        class LocationParameter<E : Environment, S : Player>(name: String) :
            Parameter.UnknownSize<E, S, Location>(Size.Unbounded, name, "") {
            context(inv: Invocation<E, S>)
            override fun parse(args: List<String>): CommandResult<Location> {
                if (args.firstOrNull()?.lowercase() == "here") {
                    return ParsingResult.success(inv.sender.position, Size(1))
                }
                if (args.size < 3) {
                    return ParsingResult.failSyntax(inv.getSyntax())
                }
                val x = args[0].toIntOrNull() ?: return ParsingResult.failType("integer", args[0])
                val y = args[1].toIntOrNull() ?: return ParsingResult.failType("integer", args[1])
                val z = args[2].toIntOrNull() ?: return ParsingResult.failType("integer", args[2])
                return ParsingResult.success(Location(x, y, z), Size(3))
            }
        }
        val setSpawnCommand = structure(Server::class, Player::class) {
            command("setspawn")(
                LocationParameter("location")
            ) { location ->
                sender.log("Spawn set to $location")
            }
        }

        setSpawnCommand.execute(server, zombachu, "/setspawn 10 70 -4")
        assertEquals(["Spawn set to Location(x=10, y=70, z=-4)"], zombachu.logs)

        setSpawnCommand.execute(server, zombachu, "/setspawn here")
        assertEquals(["Spawn set to Location(x=0, y=64, z=0)"], zombachu.logs)
    }

    @Test
    fun `jump - parameter with bound values`() {
        class HeightParameter<E : Environment, S>(name: String) : IntParameter<E, S>(name, "", -64, 320)
        val jumpCommand = structure(Server::class, Sender::class) {
            command("jump")(
                HeightParameter("height")
            ) { height ->
                sender.log("Jumping to $height")
            }
        }

        jumpCommand.execute(server, zombachu, "/jump 64")
        assertEquals(["Jumping to 64"], zombachu.logs)

        assertEquals(
            Feedback.OutOfRange("-64", "320", "500"),
            jumpCommand.executeExpectingError(server, zombachu, "/jump 500"),
        )
    }

    @Test
    fun `broadcast - parameter with super parse`() {
        class SignedStringParameter<E : Environment, S : Sender>(name: String) : StringParameter<E, S>(name, "") {
            context(inv: Invocation<E, S>)
            override fun parse(arg0: String): CommandResult<String> = super.parse("<${inv.sender.name}> $arg0")
        }
        val broadcastCommand = structure(Server::class, Sender::class) {
            command("broadcast")(
                SignedStringParameter("message")
            ) { message ->
                sender.log(message)
            }
        }

        broadcastCommand.execute(server, zombachu, "/broadcast Restarting")
        assertEquals(["<zombachu> Restarting"], zombachu.logs)
    }

    @Test
    fun `nick - parameter with narrower sender scope`() {
        class NicknameParameter<E : Environment>(nickname: String) : StringParameter<E, Player>(nickname, "")
        fun <E : Environment> StructureScope<E, Player>.nicknameParameter(
            nickname: String,
        ): NicknameParameter<E> = NicknameParameter(nickname)
        val setHomeCommand = structure(Server::class, Player::class) {
            command("nick")(
                playerParameter("player"),
                nicknameParameter("nickname")
            ) { player, nickname ->
                sender.socialData.nicknames[player.name] = nickname
            }
        }

        setHomeCommand.execute(server, zombachu, "/nick steve stevie")
        assertEquals("{Steve=stevie}", zombachu.socialData.nicknames.toString())
    }

    @Test
    fun `warpinfo - parameter reading from narrower environment scope`() {
        val warpInfoCommand = structure(WarpableServer::class, Sender::class) {
            command("warpinfo")(
                warpParameter("warp")
            ) { warp ->
                sender.log("Warp ${warp.name} in ${warp.world} belongs to ${warp.owner}")
            }
        }

        warpInfoCommand.execute(server, zombachu, "/warpinfo shop")
        assertEquals(["Warp shop in nether belongs to zombachu"], zombachu.logs)

        assertEquals(
            "Unknown warp: nowhere",
            warpInfoCommand.executeExpectingError(server, zombachu, "/warpinfo nowhere").message,
        )
    }

    @Test
    fun `realname - parameter requiring transformed sender`() {
        val homeInfoCommand = structure(Server::class, Sender::class) {
            requireSocialData {
                command("realname")(
                    realNameParameter("nickname")
                ) { realName ->
                    sender.player.log("${args[1]}'s real name is $realName")
                }
            }
        }
        zombachu.socialData.nicknames["Steve"] = "stevie"

        homeInfoCommand.execute(server, zombachu, "/realname stevie")
        assertEquals(["stevie's real name is Steve"], zombachu.logs)
    }

    @Test
    fun `dsl accepts explicit type arguments`() {
        val scope = StructureScope.empty<Server, Sender>()
        with(scope) {
            val parameter: Parameter.FixedSize<Server, Sender, String> = stringParameter<Server, Sender>("a")
            val flag: ValueFlag<Server, Sender, String> =
                valueFlag<Server, Sender, String>("b", default = "", parameter = parameter)
            val command: Structure<Server, Sender, Arguments1<String>> =
                requireAs<Server, Sender, Player, Arguments1<String>>({ it as Player }) {
                    command("c")(
                        stringParameter("d")
                    )
                }

            assertEquals(["a", "b", "c"], [parameter.name, flag.name, command.name])
        }
    }
}
