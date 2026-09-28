package com.zombachu.stick.integration

import com.zombachu.stick.Command
import com.zombachu.stick.Execution
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.textParameter
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeExpectingInvalidSyntax
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.targetPlayerParameter
import kotlin.test.Test
import kotlin.test.assertEquals

class ExecuteTest {

    private val zombachu = Player("zombachu", ["server.heal", "server.feed"])
    private val steve = Player("Steve")
    private val server = SynergyServer([zombachu, steve])

    @Test
    fun `echo - execute can be trailing lambda with typed arguments`() {
        class EchoCommand : Command<Server, Sender> {
            override val structure = structure {
                command(name = "echo")(
                    intParameter("times", min = 1, max = 5),
                    textParameter("message"),
                ) { times: Int, message: String ->
                    repeat(times) { sender.log(message) }
                }
            }
        }
        val echoCommand = EchoCommand().structure

        echoCommand.execute(server, zombachu, "/echo 3 Hello there")
        assertEquals(["Hello there", "Hello there", "Hello there"], zombachu.logs)

        assertEquals("/echo <times> <message>", echoCommand.executeExpectingInvalidSyntax(server, zombachu, "/echo 3"))
    }

    @Test
    fun `heal - execute can be a method reference`() {
        class HealCommand : Command<Server, Sender> {
            override val structure = structure {
                command(
                    name = "heal",
                    requirement = permission("server.heal"),
                )(
                    targetPlayerParameter("player"),
                    ::heal,
                )
            }

            private fun heal(ex: Execution<Server, Sender>, target: Player) {
                target.log("You have been healed")
                if (target !== ex.sender) {
                    ex.sender.log("Healed ${target.name}")
                }
            }
        }
        val healCommand = HealCommand().structure

        healCommand.execute(server, zombachu, "/heal Steve")
        assertEquals(["You have been healed"], steve.logs)
        assertEquals(["Healed Steve"], zombachu.logs)
    }

    @Test
    fun `feed - execute can be a function with execution receiver`() {
        class FeedCommand : Command<Server, Sender> {
            override val structure = structure {
                command(
                    name = "feed",
                    requirement = permission("server.feed"),
                )(
                    targetPlayerParameter("player"),
                    execute = feed(),
                )
            }

            private fun feed() = fun Execution<Server, Sender>.(target: Player) {
                target.log("You have been fed")
                if (target !== sender) {
                    sender.log("Fed ${target.name}")
                }
            }
        }
        val feedCommand = FeedCommand().structure

        feedCommand.execute(server, zombachu, "/feed Steve")
        assertEquals(["You have been fed"], steve.logs)
        assertEquals(["Fed Steve"], zombachu.logs)
    }

    @Test
    fun `nick - execute can be an unbound method reference`() {
        class NickCommand : Command<Server, Sender> {
            override val structure = structure {
                command("nick")(
                    stringParameter("name"),
                    Execution<Server, Sender>::nick,
                )
            }
        }
        val nickCommand = NickCommand().structure

        nickCommand.execute(server, zombachu, "/nick Zomb")
        assertEquals(["Nickname set to Zomb"], zombachu.logs)
    }

    @Test
    fun `tp - a method reference typed at the sender a requireSender narrowed the scope to`() {
        class SpawnCommand : Command<Server, Sender> {
            override val structure = structure {
                requireSender(Player::class) {
                    command("spawn")(
                        stringParameter("world"),
                        ::spawn,
                    )
                }
            }

            private fun spawn(ex: Execution<Server, Player>, world: String) {
                ex.sender.world = world
                ex.sender.log("Teleported to $world's spawn")
            }
        }
        val spawnCommand = SpawnCommand().structure

        spawnCommand.execute(server, zombachu, "/spawn nether")
        assertEquals("nether", zombachu.world)
    }

    @Test
    fun `ping - execute with no elements`() {
        class PingCommand : Command<Server, Sender> {
            override val structure = structure {
                command(name = "ping")() {
                    sender.log("Pong!")
                }
            }
        }
        val pingCommand = PingCommand().structure

        pingCommand.execute(server, zombachu, "/ping")
        assertEquals(["Pong!"], zombachu.logs)
    }
}

private fun Execution<Server, Sender>.nick(name: String) {
    sender.log("Nickname set to $name")
}
