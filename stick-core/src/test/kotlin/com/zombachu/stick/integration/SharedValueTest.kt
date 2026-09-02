package com.zombachu.stick.integration

import com.zombachu.stick.Command
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.id
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.store
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SocialData
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.socialDataHelper
import com.zombachu.stick.integration.fixtures.warpParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class SharedValueTest {

    private val zombachu = Player("zombachu")
    private val steve = Player("Steve")
    private val server =
        SynergyServer([zombachu, steve], WarpRegistry([Warp("shop", "zombachu", "nether")]))

    @Test
    fun `warp - subcommands can access previously parsed value`() {
        class WarpActionCommand : Command<WarpableServer, Player> {
            private val targetWarp: TypedIdentifier<Warp> = id("warp")
            override val structure = structure {
                command("warp")(
                    warpParameter("warp").store(targetWarp),
                    group(
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
                )
            }
        }
        val warpCommand = WarpActionCommand().structure

        warpCommand.execute(server, zombachu, "/warp shop tp")
        assertEquals("nether", zombachu.world)

        assertEquals(
            Feedback.InvalidSyntax("/warp <warp> <tp|delete>"),
            warpCommand.executeExpectingError(server, zombachu, "/warp shop rename"),
        )

        warpCommand.execute(server, zombachu, "/warp shop delete")
        assertEquals([], server.warps.names)
    }

    @Test
    fun `poke - get retrieves stored values`() {
        val poker: TypedIdentifier<SocialData> = id("poker")
        val times: TypedIdentifier<Int> = id("times")
        val reportCommand = structure(Server::class, Player::class) {
            command("poke")(
                socialDataHelper().store(poker),
                playerParameter("player"),
                optionally(ifAbsent = default(3), parameter = intParameter("times", min = 1, max = 5)).store(times),
            ) { _, target, _ ->
                target.log("Poked ${target.name} ${get(times)} times")
                sender.log("Poked by ${get(poker).player.name}")
            }
        }

        reportCommand.execute(server, zombachu, "/poke Steve")
        assertEquals(["Poked by zombachu"], zombachu.logs)
        assertEquals(["Poked Steve 3 times"], steve.logs)

        reportCommand.execute(server, zombachu, "/poke Steve 5")
        assertEquals(["Poked Steve 5 times"], steve.logs)
    }

    @Test
    fun `broadcast - stored values can be referenced by reconstructed id`() {
        val broadcastCommand = structure(Server::class, Sender::class) {
            command("broadcast")(
                textParameter("message").store(id("message")),
            ) {
                sender.log(get(id("message")))
            }
        }

        broadcastCommand.execute(server, zombachu, "/broadcast Server restarting")
        assertEquals(["Server restarting"], zombachu.logs)
    }
}
