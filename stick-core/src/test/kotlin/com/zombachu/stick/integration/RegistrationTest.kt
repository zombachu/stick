package com.zombachu.stick.integration

import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.Stick
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.intParameter
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.execute
import com.zombachu.stick.integration.fixtures.executeWithHandler
import com.zombachu.stick.noopFailureHandler
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RegistrationTest {

    private val zombachu = Player("zombachu")
    private val console = Console()
    private val server = SynergyServer([zombachu], WarpRegistry([Warp("spawn", "Console", "overworld")]))
    private val stick = TestStick(server)

    @Test
    fun `version - command with base environment and platform sender registers`() {
        class VersionCommand : Command<Environment, Sender> {
            override val structure = structure {
                command("version")() {
                    sender.log("Stick 0.4.0")
                }
            }
        }

        stick.withContext(server) { register(VersionCommand()) }
        assertEquals(1, stick.registered.size)

        stick.withContext(server) { register(VersionCommand().structure) }
        assertEquals(2, stick.registered.size)

        stick.registered.last().execute(server, zombachu, "/version")
        assertEquals(["Stick 0.4.0"], zombachu.logs)

        stick.registered.last().execute(server, console, "/version")
        assertEquals(["Stick 0.4.0"], console.logs)
    }

    @Test
    fun `version - command registers inline`() {
        stick.withContext(server) {
            register {
                command("version")() {
                    sender.log("Stick 0.4.0")
                }
            }
        }

        stick.registered.single().execute(server, zombachu, "/version")
        assertEquals(["Stick 0.4.0"], zombachu.logs)
    }

    @Test
    fun `me - command with narrower platform sender registers`() {
        class MeCommand : Command<Server, Player> {
            override val structure = structure {
                command("me")() {
                    sender.log("${sender.name}: Hi")
                }
            }
        }

        stick.withContext(server) { register(MeCommand()) }

        stick.registered.single().execute(server, zombachu, "/me")
        assertEquals(["zombachu: Hi"], zombachu.logs)
    }

    @Test
    fun `profile - command with custom sender registers`() {
        class ProfileCommand : Command<Server, Profile> {
            override val structure = structure {
                command("profile")() {
                    sender.sender.log("Profile for ${sender.sender.name}")
                }
            }
        }

        stick.withContext(server, noopFailureHandler(), { Profile(it) }, { SenderValidationResult.success() }) {
            register(ProfileCommand())
        }

        stick.registered.single().execute(server, zombachu, "/profile")
        assertEquals(["Profile for zombachu"], zombachu.logs)
    }

    @Test
    fun `adminprofile - command with narrower custom sender registers`() {
        class AdminProfileCommand : Command<Server, AdminProfile> {
            override val structure = structure {
                command("adminprofile")() {
                    sender.sender.log("Admin profile for ${sender.sender.name}")
                }
            }
        }

        stick.withContext(server, noopFailureHandler(), { AdminProfile(it) }, { SenderValidationResult.success() }) {
            register(AdminProfileCommand())
        }

        stick.registered.single().execute(server, zombachu, "/adminprofile")
        assertEquals(["Admin profile for zombachu"], zombachu.logs)
    }

    @Test
    fun `warps + ping - commands with narrower and base environment register in same context`() {
        class WarpFailureHandler : FailureHandler<WarpableServer, Sender> {
            var feedback: Feedback? = null
            var warps: Int = 0

            context(inv: Invocation<WarpableServer, Sender>)
            override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
                feedback = failure.feedback
                warps = inv.env.warps.names.size
            }
        }
        class WarpsCommand : Command<WarpableServer, Sender> {
            override val structure = structure {
                command("warps")(
                    optionally(
                        ifAbsent = default("overworld"),
                        parameter = literalParameter("overworld")
                    ),
                ) { world ->
                    sender.log("Warps in $world: ${env.warps.names.size}")
                }
            }
        }
        class PingCommand : Command<Environment, Sender> {
            override val structure = structure {
                command("ping")() {
                    sender.log("Pong!")
                }
            }
        }
        val handler = WarpFailureHandler()

        stick.withContext(server, handler) {
            register(WarpsCommand())
            register(PingCommand())
        }

        assertEquals(2, stick.registered.size)

        stick.registered.first().execute(server, zombachu, "/warps")
        assertEquals(["Warps in overworld: 1"], zombachu.logs)

        stick.registered.first().executeWithHandler(stick.handler!!, server, console, "/warps nether")
        assertEquals(Feedback.LiteralNotMatched(["overworld"], "nether"), handler.feedback)
        assertEquals(1, handler.warps)

        stick.registered.last().execute(server, console, "/ping")
        assertEquals(["Pong!"], console.logs)
    }

    @Test
    fun `selfban - command with custom sender uses custom sender handler`() {
        class ProfileFailureHandler : FailureHandler<Server, Profile> {
            var feedback: Feedback? = null
            var name: String? = null

            context(inv: Invocation<Server, Profile>)
            override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
                feedback = failure.feedback
                name = inv.sender.sender.name
            }
        }
        class SelfBanCommand : Command<Server, Profile> {
            override val structure = structure {
                command("selfban")(
                    intParameter("minutes", min = 1, max = 60)
                ) { minutes ->
                    sender.sender.log("Self banned for $minutes minutes")
                }
            }
        }
        val handler = ProfileFailureHandler()

        stick.withContext(server, handler, { Profile(it) }, { SenderValidationResult.success() }) {
            register(SelfBanCommand())
        }

        stick.registered.single().executeWithHandler(stick.handler!!, server, zombachu, "/selfban 99")
        assertEquals(Feedback.OutOfRange("1", "60", "99"), handler.feedback)
        assertEquals("zombachu", handler.name)
    }

    @Test
    fun `rules - a context accepts only the commands whose sender and environment it can supply`() {
        class RulesCommand : Command<Server, Sender> {
            override val structure = structure {
                command("rules")() {
                    sender.log("Be nice")
                }
            }
        }
        class UnrelatedEnvironment : Environment
        class WrongSenderCommand : Command<Server, Int> {
            override val structure = structure { command("wrongsender")() {} }
        }
        class WrongEnvironmentCommand : Command<UnrelatedEnvironment, Sender> {
            override val structure = structure { command("wrongenvironment")() {} }
        }

        stick.withContext(server) {
            register(RulesCommand())

            // Shouldn't compile
            // register(WrongSenderCommand())

            // Shouldn't compile
            // register(WrongEnvironmentCommand())
        }

        assertEquals(1, stick.registered.size)
    }

    @Test
    fun `KNOWN LIMITATION - vanish - command for narrower platform sender throws for wrong sender type`() {
        class VanishCommand : Command<Server, Player> {
            override val structure = structure {
                command("vanish")() {
                    sender.log("Toggled vanish state")
                }
            }
        }

        stick.withContext(server) { register(VanishCommand()) }

        assertFailsWith<ClassCastException> { stick.registered.single().execute(server, console, "/vanish") }
    }

    @Test
    fun `KNOWN LIMITATION - adminprofile - command for narrower custom sender throws for wrong sender type`() {
        class AdminProfileCommand : Command<Server, AdminProfile> {
            override val structure = structure {
                command("adminprofile")() {
                    sender.sender.log("Admin profile for ${sender.sender.name}")
                }
            }
        }

        stick.withContext(server, noopFailureHandler(), { Profile(it) }, { SenderValidationResult.success() }) {
            register(AdminProfileCommand())
        }

        assertFailsWith<ClassCastException> { stick.registered.single().execute(server, zombachu, "/audit") }
    }

    private class TestStick(env: Server) :
        Stick<Server, Sender>(Sender::class, lazyOf(env), lazy { noopFailureHandler() }) {

        val registered: MutableList<Structure<SynergyServer, Sender, *>> = mutableListOf()

        var handler: FailureHandler<SynergyServer, Sender>? = null

        context(env: E2, failureHandler: FailureHandler<E2, Sender>)
        override fun <E2 : Server> registerCommand(structure: Structure<E2, Sender, *>) {
            @Suppress("UNCHECKED_CAST")
            registered += structure as Structure<SynergyServer, Sender, *>
            @Suppress("UNCHECKED_CAST")
            handler = failureHandler as FailureHandler<SynergyServer, Sender>
        }
    }

    private open class Profile(val sender: Sender)

    private class AdminProfile(sender: Sender) : Profile(sender)
}
