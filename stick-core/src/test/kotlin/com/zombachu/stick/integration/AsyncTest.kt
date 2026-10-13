package com.zombachu.stick.integration

import com.zombachu.stick.CommandResult
import com.zombachu.stick.CommandRunner
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Suggestion
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.map
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.SuspendingParameter
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.integration.fixtures.Location
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.RecordingFailureHandler
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.TestExecutor
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingError
import com.zombachu.stick.integration.fixtures.executeOn
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.suggest
import com.zombachu.stick.integration.fixtures.suggestAsync
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import com.zombachu.stick.withAsyncContext
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
    fun `warp - suspending parameter resolves on main context`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                CachedWarpParameter(),
            ) { warp ->
                sender.log("Warped to ${warp.name} on ${TestExecutor.running}")
            }
        }

        val failures = warpCommand.executeOn(main, async, server, zombachu, "/warp shop")
        main.drain()

        assertEquals(["Resolved shop on main", "Warped to shop on main"], zombachu.logs)
        assertNull(failures.reason)
    }

    @Test
    fun `warp - withAsyncContext block runs on async context`() {
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
        assertEquals(["Looked up shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Looked up shop on async", "Warped to shop on main"], zombachu.logs)
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
        assertEquals(["Looked up shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Looked up shop on async", "Parsed hello on main"], zombachu.logs)

        async.drain()
        main.drain()
        assertEquals(
            [
                "Looked up shop on async",
                "Parsed hello on main",
                "Looked up spawn on async",
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
    fun `warp - exception in withAsyncContext block fails with Unknown on main`() {
        class FailingWarpParameter : SuspendingParameter.Size1<WarpableServer, Sender, Warp>("warp", "") {
            context(inv: Invocation<WarpableServer, Sender>)
            override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

            context(inv: Invocation<WarpableServer, Sender>)
            override suspend fun resolve(arg0: String): CommandResult<Warp> =
                withAsyncContext { error("Lookup failed") }
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
        assertEquals(["Looked up spawn on async", "Teleported to warp spawn"], zombachu.logs)

        val failures = tpCommand.executeOn(main, async, server, zombachu, "/tp Steve")
        main.drain()
        async.drain()
        main.drain()
        assertEquals(["Looked up Steve on async", "Teleported to player Steve"], zombachu.logs)
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
    fun `warp - suggest does not run withAsyncContext block`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                LoggingWarpParameter(),
                flag("silent"),
            ) { warp, _ ->
                sender.log("Warped to $warp")
            }
        }

        val suggestions =
            warpCommand.suggest(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        async.drain()

        assertEquals(["-silent"], suggestions)
        assertEquals([], zombachu.logs)
    }

    @Test
    fun `warp - suggest includes suspending parameter suggestions from main context`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                CachedWarpParameter(),
                flag("silent"),
            ) { warp, _ ->
                sender.log("Warped to ${warp.name}")
            }
        }

        assertEquals(["-silent", "spawn", "shop"], warpCommand.suggest(server, zombachu, "/warp "))
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
    fun `warp - suggestAsync completes with async suggestions of sync parameter`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(AsyncSuggestedWarpParameter()) { warp -> sender.log("Warped to $warp") }
        }

        val suggestions =
            warpCommand.suggestAsync(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        assertFalse(suggestions.isDone)

        async.drain()
        main.drain()
        assertEquals(["spawn", "shop"], suggestions.join())
    }

    @Test
    fun `warp - suspending parameter suggests on main context`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(CachedWarpParameter()) { warp -> sender.log("Warped to ${warp.name}") }
        }
        val suggestions =
            warpCommand.suggestAsync(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        assertEquals(["spawn", "shop"], suggestions.getNow([]))
    }

    @Test
    fun `warp - suggestAsync completes empty after rejected rejoin`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(AsyncWarpParameter("warp")) { warp -> sender.log("Warped to ${warp.name}") }
        }

        val suggestions =
            warpCommand.suggestAsync(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        main.shutdown()
        async.drain()

        assertEquals([], suggestions.getNow(null))
    }

    @Test
    fun `warp - cancelled suggestAsync stops suggest at rejoin`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(LoggingWarpParameter()) { warp -> sender.log("Warped to $warp") }
        }

        val suggestions =
            warpCommand.suggestAsync(server, zombachu, "/warp ", main.asCoroutineContext(), async.asCoroutineContext())
        async.drain()
        val ignored = suggestions.cancel(false)
        main.drain()

        assertEquals(["Looked up warps on async"], zombachu.logs)
    }

    @Test
    fun `warp - suggestAsync abandons sender's previous suggestions`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(LoggingWarpParameter()) { warp -> sender.log("Warped to $warp") }
        }
        val runner = runner(warpCommand)

        val first = runner.suggestAsync(zombachu, "warp", [""])
        val second = runner.suggestAsync(zombachu, "warp", ["s"])
        assertEquals([], first.getNow(null))

        async.drain()
        main.drain()
        assertEquals(["Looked up warps on async", "Suggested warps on main"], zombachu.logs)
        assertEquals(["spawn", "shop"], second.getNow(null))
    }

    @Test
    fun `summon - value flag with async parameter rejoins main after parsing`() {
        val summonCommand = structure(WarpableServer::class, Sender::class) {
            command("summon")(
                playerParameter("player"),
                valueFlag("warp", AsyncWarpParameter("warp"), null),
            ) { player, warp ->
                sender.log("Summoned ${player.name} to ${warp?.name} on ${TestExecutor.running}")
            }
        }

        val failures = summonCommand.executeOn(main, async, server, zombachu, "/summon Steve -warp shop")
        main.drain()
        async.drain()
        assertEquals(["Looked up shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Looked up shop on async", "Summoned Steve to shop on main"], zombachu.logs)
        assertNull(failures.reason)
    }

    @Test
    fun `summon - hybrid flag with async parameter rejoins main after parsing`() {
        val summonCommand = structure(WarpableServer::class, Sender::class) {
            command("summon")(
                playerParameter("player"),
                hybridFlag("warp", AsyncWarpParameter("warp")),
            ) { player, warp ->
                when (warp) {
                    is HybridFlagResult.Value -> sender.log("Summoned ${player.name} to ${warp.value.name}")
                    is HybridFlagResult.Present -> sender.log("Summoned ${player.name} to the default warp")
                    is HybridFlagResult.Absent -> sender.log("Summoned ${player.name}")
                }
            }
        }

        summonCommand.executeOn(main, async, server, zombachu, "/summon Steve -warp shop")
        main.drain()
        async.drain()
        assertEquals(["Looked up shop on async"], zombachu.logs)

        main.drain()
        assertEquals(["Looked up shop on async", "Summoned Steve to shop"], zombachu.logs)

        summonCommand.execute(server, zombachu, "/summon Steve -warp")
        assertEquals(["Summoned Steve to the default warp"], zombachu.logs)
    }

    @Test
    fun `summon - suggest offers label but not values of flag with async parameter`() {
        val summonCommand = structure(WarpableServer::class, Sender::class) {
            command("summon")(
                playerParameter("player"),
                valueFlag("warp", AsyncWarpParameter("warp"), null),
            ) { player, warp ->
                sender.log("Summoned ${player.name} to ${warp?.name}")
            }
        }

        assertEquals(["-warp"], summonCommand.suggest(server, zombachu, "/summon Steve "))
        assertEquals([], summonCommand.suggest(server, zombachu, "/summon Steve -warp "))
    }

    @Test
    fun `summon - suggestAsync offers values of flag with async parameter`() {
        val summonCommand = structure(WarpableServer::class, Sender::class) {
            command("summon")(
                playerParameter("player"),
                valueFlag("warp", AsyncWarpParameter("warp"), null),
            ) { player, warp ->
                sender.log("Summoned ${player.name} to ${warp?.name}")
            }
        }

        assertEquals(["spawn", "shop"], summonCommand.suggestAsync(server, zombachu, "/summon Steve -warp ").join())
    }

    @Test
    fun `fullname - withMainContext block runs on main context`() {
        val fullNameCommand = structure(WarpableServer::class, Sender::class) {
            command("fullname")(
                warpParameter("warp").map { warp ->
                    withAsyncContext {
                        val owner = withMainContext {
                            sender.log("Read owner on ${TestExecutor.running}")
                            warp.owner
                        }
                        sender.log("Mapped ${warp.name} on ${TestExecutor.running}")
                        success("$owner's ${warp.name}")
                    }
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
    fun `home - withAsyncContext block in helper runs on async context`() {
        val homeCommand = structure(WarpableServer::class, Sender::class) {
            command("home")(
                helper {
                    val home = withAsyncContext {
                        sender.log("Looked up home on ${TestExecutor.running}")
                        Location(1, 64, 1)
                    }
                    sender.log("Found home on ${TestExecutor.running}")
                    success(home)
                },
            ) { location ->
                sender.log("Teleported to $location on ${TestExecutor.running}")
            }
        }

        homeCommand.executeOn(main, async, server, zombachu, "/home")
        main.drain()
        async.drain()
        assertEquals(["Looked up home on async"], zombachu.logs)

        main.drain()
        assertEquals(
            ["Looked up home on async", "Found home on main", "Teleported to Location(x=1, y=64, z=1) on main"],
            zombachu.logs,
        )
    }

    @Test
    fun `setwarp - withAsyncContext block in body runs on async context`() {
        val setwarpCommand = structure(WarpableServer::class, Sender::class) {
            command("setwarp")(
                stringParameter("name"),
            ) { name ->
                withAsyncContext { sender.log("Saved $name on ${TestExecutor.running}") }
                sender.log("Created $name on ${TestExecutor.running}")
            }
        }

        setwarpCommand.executeOn(main, async, server, zombachu, "/setwarp home")
        main.drain()
        async.drain()
        assertEquals(["Saved home on async"], zombachu.logs)

        main.drain()
        assertEquals(["Saved home on async", "Created home on main"], zombachu.logs)
    }

    @Test
    fun `setwarp - rejected rejoin abandons command`() {
        val setwarpCommand = structure(WarpableServer::class, Sender::class) {
            command("setwarp")(
                stringParameter("name"),
            ) { name ->
                withAsyncContext { sender.log("Saved $name on ${TestExecutor.running}") }
                sender.log("Created $name on ${TestExecutor.running}")
            }
        }

        val failures = setwarpCommand.executeOn(main, async, server, zombachu, "/setwarp home")
        main.drain()
        main.shutdown()
        async.drain()

        assertEquals(["Saved home on async"], zombachu.logs)
        assertNull(failures.reason)
    }

    private fun runner(structure: Structure<WarpableServer, Sender, *>): CommandRunner<WarpableServer, Sender> =
        CommandRunner(
            server,
            RecordingFailureHandler(),
            structure,
            main.asCoroutineContext(),
            async.asCoroutineContext(),
        )

    private class AsyncWarpParameter(name: String) : SuspendingParameter.Size1<WarpableServer, Sender, Warp>(name, "") {
        context(inv: Invocation<WarpableServer, Sender>)
        override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            withAsyncContext { inv.env.warps.names.toSuggestions() }

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun resolve(arg0: String): CommandResult<Warp> {
            val warp = withAsyncContext {
                inv.sender.log("Looked up $arg0 on ${TestExecutor.running}")
                inv.env.warps[arg0]
            } ?: return failType("warp", arg0)
            return success(warp)
        }
    }

    private class AsyncSuggestedWarpParameter : Parameter.Size1<WarpableServer, Sender, String>("warp", "") {
        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            withAsyncContext { inv.env.warps.names.toSuggestions() }

        context(inv: Invocation<WarpableServer, Sender>)
        override fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }

    private class LoggingWarpParameter : Parameter.Size1<WarpableServer, Sender, String>("warp", "") {
        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
            val names = withAsyncContext {
                inv.sender.log("Looked up warps on ${TestExecutor.running}")
                inv.env.warps.names
            }
            inv.sender.log("Suggested warps on ${TestExecutor.running}")
            return names.toSuggestions()
        }

        context(inv: Invocation<WarpableServer, Sender>)
        override fun resolve(arg0: String): CommandResult<String> = success(arg0)
    }

    private class CachedWarpParameter : SuspendingParameter.Size1<WarpableServer, Sender, Warp>("warp", "") {
        context(inv: Invocation<WarpableServer, Sender>)
        override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            inv.env.warps.names.toSuggestions()

        context(inv: Invocation<WarpableServer, Sender>)
        override suspend fun resolve(arg0: String): CommandResult<Warp> {
            inv.sender.log("Resolved $arg0 on ${TestExecutor.running}")
            return success(inv.env.warps[arg0]!!)
        }
    }
}
