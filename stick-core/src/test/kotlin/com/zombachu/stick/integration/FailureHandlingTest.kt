package com.zombachu.stick.integration

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listElementParameter
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.failure.CustomReason
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.UnknownWarp
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.executeWithHandler
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.success
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
        assertEquals(["level IS NOT A integer: high"], zombachu.logs)

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
    fun `warp delete - handlers receive custom reason`() {
        val warpDeleteCommand = structure(WarpableServer::class, Sender::class) {
            command("delete")(
                warpParameter("warp")
            ) { warp ->
                sender.log("Deleted ${warp.name}")
            }
        }

        warpDeleteCommand.executeWithHandler(handler, server, zombachu, "/delete nowhere")
        assertEquals(["UNKNOWN WARP: nowhere"], zombachu.logs)
    }

    @Test
    fun `mail delete - handlers do not receive error on empty list`() {
        val mailDeleteCommand = structure(Server::class, Player::class) {
            command("delete")(
                listElementParameter(
                    name = "index",
                    list = { success(sender.mail) },
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

    @Test
    fun `throw - handler catches exception from element`() {
        class ThrowingParameter(name: String) : Parameter.Size1<Server, Sender, String>(name, "") {
            override val type: GroupableType = GroupableType.Passthrough

            context(validationContext: ValidationContext<Server, Sender>)
            override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

            context(validationContext: ValidationContext<Server, Sender>)
            override fun resolve(arg0: String): CommandResult<String> = error("this is an exception")
        }
        val throwCommand = structure(Server::class, Sender::class) {
            command("throw")(
                ThrowingParameter("egg")
            ) {
                sender.log("this is not an exception")
            }
        }

        val reason = throwCommand.executeExpectingError(server, zombachu, "/throw blah")

        assertIs<Reason.Unknown>(reason)
        assertEquals("this is an exception", reason.cause?.message)
    }

    @Test
    fun `throw - handler catches exception from command body`() {
        val throwCommand = structure(Server::class, Sender::class) {
            command("throw")() {
                error("this is an exception")
            }
        }

        val reason = throwCommand.executeExpectingError(server, zombachu, "/throw")

        assertIs<Reason.Unknown>(reason)
        assertEquals("this is an exception", reason.cause?.message)
    }

    private class TestFailureHandler<S : Sender> : FailureHandler<Server, S> {
        context(inv: Invocation<Server, S>)
        override fun onFailure(reason: Reason, origin: FailureOrigin) {
            val message =
                when (reason) {
                    is Reason.Unknown -> "SOMETHING BROKE"
                    Reason.InvalidPermission -> "PERMISSION DENIED"
                    Reason.InvalidSender -> "NOT FOR YOU"
                    is Reason.InvalidSenderType -> "${reason.required.simpleName} ONLY"
                    Reason.InvalidSyntax -> "USAGE: ${origin.usage}"
                    is Reason.LiteralNotMatched ->
                        "EXPECTED ${reason.validValues.joinToString("|")} NOT ${reason.provided}"
                    is Reason.OutOfRange -> "${reason.min} TO ${reason.max}, NOT ${reason.provided}"
                    is Reason.TypeNotMatched ->
                        "${origin.elementName} IS NOT A ${reason.expectedType}: ${reason.provided}"
                    is UnknownWarp -> "UNKNOWN WARP: ${reason.name}"
                    is CustomReason -> reason.message(origin)
                }
            inv.sender.log(message)
        }
    }
}
