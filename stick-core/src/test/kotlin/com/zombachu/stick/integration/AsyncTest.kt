package com.zombachu.stick.integration

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.helperAsync
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.map
import com.zombachu.stick.dsl.mapAsync
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.AsyncParameter
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.integration.fixtures.Location
import com.zombachu.stick.integration.fixtures.TestExecutor
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.executeOn
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.suggest
import com.zombachu.stick.integration.fixtures.suggestAsync
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import com.zombachu.stick.withMainContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

class AsyncTest {

    private val zombachu = Player("zombachu")
    private val steve = Player("Steve")
    private val server =
        SynergyServer(
            [zombachu, steve],
            WarpRegistry([Warp("spawn", "zombachu", "overworld"), Warp("shop", "Steve", "overworld")]),
        )
    private val main = TestExecutor("main")
    private val async = TestExecutor("async")

    @Test
    fun `warp - async parameter resolves on async context`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                AsyncWarpParameter("warp"),
            ) { warp ->
                sender.log("Warped to ${warp.name} on ${TestExecutor.running}")
            }
        }

        val failures = warpCommand.executeOn(main, async, server, zombachu, "/warp shop")
        main.drain()
        assertEquals([], zombachu.logs)

        async.drain()
        assertEquals(["Resolved shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Resolved shop on async", "Warped to shop on main"], zombachu.logs)
        assertNull(failures.reason)
    }

    @Test
    fun `link - async parameters rejoin main after parsing`() {
        val linkCommand = structure(WarpableServer::class, Sender::class) {
            command("link")(
                AsyncWarpParameter("from"),
                stringParameter("note").map { note ->
                    sender.log("Parsed $note on ${TestExecutor.running}")
                    success(note)
                },
                AsyncWarpParameter("to"),
            ) { from, _, to ->
                sender.log("Linked ${from.name} to ${to.name} on ${TestExecutor.running}")
            }
        }

        val failures = linkCommand.executeOn(main, async, server, zombachu, "/link shop hello spawn")
        main.drain()
        async.drain()
        assertEquals(["Resolved shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Resolved shop on async", "Parsed hello on main"], zombachu.logs)

        async.drain()
        main.drain()
        assertEquals(
            [
                "Resolved shop on async",
                "Parsed hello on main",
                "Resolved spawn on async",
                "Linked shop to spawn on main",
            ],
            zombachu.logs,
        )
        assertNull(failures.reason)
    }

    @Test
    fun `warp - async parameter failure reaches failure handler`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(AsyncWarpParameter("warp")) { warp -> sender.log("Warped to ${warp.name}") }
        }

        assertEquals(
            Reason.TypeNotMatched("warp", "nowhere"),
            warpCommand.executeExpectingError(server, zombachu, "/warp nowhere"),
        )
    }

    @Test
    fun `warp - exception in async parameter fails with Unknown on main`() {
        class FailingWarpParameter : AsyncParameter.Size1<WarpableServer, Sender, Warp>("warp", "") {
            context(inv: Invocation<WarpableServer, Sender>)
            override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

            context(inv: Invocation<WarpableServer, Sender>)
            override suspend fun resolve(arg0: String): CommandResult<Warp> = error("Lookup failed")
        }
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(FailingWarpParameter()) { warp -> sender.log("Warped to ${warp.name}") }
        }

        val failures = warpCommand.executeOn(main, async, server, zombachu, "/warp shop")
        main.drain()
        async.drain()
        assertNull(failures.reason)

        main.drain()
        assertEquals("Lookup failed", assertIs<Reason.Unknown>(failures.reason).cause?.message)
    }

    @Test
    fun `tp - async alternative falls through to next`() {
        val tpCommand = structure(WarpableServer::class, Sender::class) {
            command("tp")(
                group(
                    AsyncWarpParameter("warp"),
                    playerParameter("player"),
                )
            ) { destination ->
                when (destination) {
                    is GroupResult.ResultA -> sender.log("Teleported to warp ${destination.value.name}")
                    is GroupResult.ResultB -> sender.log("Teleported to player ${destination.value.name}")
                }
            }
        }

        tpCommand.executeOn(main, async, server, zombachu, "/tp spawn")
        main.drain()
        async.drain()
        main.drain()
        assertEquals(["Resolved spawn on async", "Teleported to warp spawn"], zombachu.logs)

        val failures = tpCommand.executeOn(main, async, server, zombachu, "/tp Steve")
        main.drain()
        async.drain()
        main.drain()
        assertEquals(["Resolved Steve on async", "Teleported to player Steve"], zombachu.logs)
        assertNull(failures.reason)
    }

    @Test
    fun `warp - suggest omits async suggestions`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                AsyncWarpParameter("warp"),
                flag("silent"),
            ) { warp, _ ->
                sender.log("Warped to ${warp.name}")
            }
        }

        assertEquals(["-silent"], warpCommand.suggest(server, zombachu, "/warp "))
    }

    @Test
    fun `warp - suggestAsync completes with async suggestions`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                AsyncWarpParameter("warp"),
                flag("silent"),
            ) { warp, _ ->
                sender.log("Warped to ${warp.name}")
            }
        }

        val suggestions =
            warpCommand.suggestAsync(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        assertFalse(suggestions.isDone)

        async.drain()
        main.drain()
        assertEquals(["-silent", "spawn", "shop"], suggestions.join())
    }

    @Test
    fun `uppercase - mapAsync transform runs on async context`() {
        val uppercaseCommand = structure(WarpableServer::class, Sender::class) {
            command("uppercase")(
                warpParameter("warp").mapAsync { warp ->
                    sender.log("Mapped ${warp.name} on ${TestExecutor.running}")
                    success(warp.name.uppercase())
                },
            ) { name ->
                sender.log("Renamed to $name on ${TestExecutor.running}")
            }
        }

        uppercaseCommand.executeOn(main, async, server, zombachu, "/uppercase shop")
        main.drain()
        async.drain()
        assertEquals(["Mapped shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Mapped shop on async", "Renamed to SHOP on main"], zombachu.logs)
    }

    @Test
    fun `fullname - withMainContext block runs on main context`() {
        val fullNameCommand = structure(WarpableServer::class, Sender::class) {
            command("fullname")(
                warpParameter("warp").mapAsync { warp ->
                    val owner = withMainContext {
                        sender.log("Read owner on ${TestExecutor.running}")
                        warp.owner
                    }
                    sender.log("Mapped ${warp.name} on ${TestExecutor.running}")
                    success("$owner's ${warp.name}")
                },
            ) { name ->
                sender.log("Renamed to $name on ${TestExecutor.running}")
            }
        }

        fullNameCommand.executeOn(main, async, server, zombachu, "/fullname shop")
        main.drain()
        async.drain()
        assertEquals([], zombachu.logs)

        main.drain()
        assertEquals(["Read owner on main"], zombachu.logs)

        async.drain()
        assertEquals(["Read owner on main", "Mapped shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Read owner on main", "Mapped shop on async", "Renamed to Steve's shop on main"], zombachu.logs)
    }

    @Test
    fun `home - helperAsync value runs on async context`() {
        val homeCommand = structure(WarpableServer::class, Sender::class) {
            command("home")(
                helperAsync {
                    sender.log("Found home on ${TestExecutor.running}")
                    success(Location(1, 64, 1))
                },
            ) { location ->
                sender.log("Teleported to $location on ${TestExecutor.running}")
            }
        }

        homeCommand.executeOn(main, async, server, zombachu, "/home")
        main.drain()
        async.drain()
        assertEquals(["Found home on async"], zombachu.logs)

        main.drain()
        assertEquals(["Found home on async", "Teleported to Location(x=1, y=64, z=1) on main"], zombachu.logs)
    }

    private class AsyncWarpParameter(name: String) : AsyncParameter.Size1<WarpableServer, Sender, Warp>(name, "") {
        context(inv: Invocation<WarpableServer, Sender>)
        override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            inv.env.warps.names.toSuggestions()

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun resolve(arg0: String): CommandResult<Warp> {
            inv.sender.log("Resolved $arg0 on ${TestExecutor.running}")
            val warp = inv.env.warps[arg0] ?: return failType("warp", arg0)
            return success(warp)
        }
    }
}
