package com.zombachu.stick.integration

import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.helperAsync
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.mapAsync
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.integration.fixtures.Location
import com.zombachu.stick.integration.fixtures.TestExecutor
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.executeOn
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.success
import com.zombachu.stick.withMainContext
import kotlin.test.Test
import kotlin.test.assertEquals

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
}
