package com.zombachu.stick.integration

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listElementParameter
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.feedback.CustomFeedback
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.handle
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.executeWithHandler
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.warpParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class FailureHandlingTest {

    private val zombachu = Player("zombachu", ["server.setlevel"])
    private val server = SynergyServer([zombachu], WarpRegistry([Warp("shop", "zombachu", "overworld")]))
    private val handler = TestFailureHandler<Sender>()
    private val playerHandler = TestFailureHandler<Player>()

    @Test
    fun `setlevel - handlers receive permission, type, range, and syntax failures`() {
        val setLevelCommand = structure(Server::class, Sender::class) {
            command("setlevel", requirement = permission("server.setlevel"))(
                intParameter("level", min = 0, max = 15)
            ) { level ->
                sender.log("Level set to $level")
            }
        }
        val steve = Player("Steve")

        setLevelCommand.executeWithHandler(handler, server, steve, "/setlevel 10")
        assertEquals(["PERMISSION DENIED"], steve.logs)

        setLevelCommand.executeWithHandler(handler, server, zombachu, "/setlevel high")
        assertEquals(["NOT A integer: high"], zombachu.logs)

        setLevelCommand.executeWithHandler(handler, server, zombachu, "/setlevel 99")
        assertEquals(["0 TO 15, NOT 99"], zombachu.logs)

        setLevelCommand.executeWithHandler(handler, server, zombachu, "/setlevel")
        assertEquals(["USAGE: /setlevel <level>"], zombachu.logs)
    }

    @Test
    fun `toggle - handlers receive literal failure values`() {
        val toggleCommand = structure(Server::class, Sender::class) {
            command("toggle")(
                literalParameter("on")
            ) {
                sender.log("Turned on")
            }
        }

        toggleCommand.executeWithHandler(handler, server, zombachu, "/toggle maybe")
        assertEquals(["EXPECTED on NOT maybe"], zombachu.logs)
    }

    @Test
    fun `warp delete - handlers receive custom feedback`() {
        val warpDeleteCommand = structure(WarpableServer::class, Sender::class) {
            command("delete")(
                warpParameter("warp")
            ) { warp ->
                sender.log("Deleted ${warp.name}")
            }
        }

        warpDeleteCommand.executeWithHandler(handler, server, zombachu, "/delete nowhere")
        assertEquals(["Unknown warp: nowhere"], zombachu.logs)
    }

    @Test
    fun `mail delete - handlers do not receive error on empty list`() {
        val mailDeleteCommand = structure(Server::class, Player::class) {
            command("delete")(
                listElementParameter(
                    name = "index",
                    list = { ParsingResult.success(sender.mail) },
                    oneIndexed = true,
                    onEmpty = { sender.log("You have no mail") },
                )
            ) { selected ->
                sender.mail.removeAt(selected.index)
            }
        }

        mailDeleteCommand.executeWithHandler(playerHandler, server, zombachu, "/delete 1")
        assertEquals(["You have no mail"], zombachu.logs)
    }

    private class TestFailureHandler<S : Sender> : FailureHandler<Server, S> {
        context(inv: Invocation<Server, S>)
        override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
            val message =
                failure.handle {
                    when (this) {
                        Feedback.Unknown -> "SOMETHING BROKE"
                        Feedback.InvalidPermission -> "PERMISSION DENIED"
                        Feedback.InvalidSender,
                        Feedback.InvalidSenderType -> "NOT FOR YOU"
                        is Feedback.InvalidSyntax -> "USAGE: $usage"
                        is Feedback.LiteralNotMatched -> "EXPECTED ${validValues.joinToString("|")} NOT $provided"
                        is Feedback.OutOfRange -> "$min TO $max, NOT $provided"
                        is Feedback.TypeNotMatched -> "NOT A $expectedType: $provided"
                        is CustomFeedback -> message
                    }
                }
            inv.sender.log(message)
        }
    }
}
