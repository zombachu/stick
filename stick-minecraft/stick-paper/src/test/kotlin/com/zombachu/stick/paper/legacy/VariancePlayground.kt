package com.zombachu.stick.paper.legacy

import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Element
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.InvalidSenderDefault
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.impl.ValidSenderDefault
import com.zombachu.stick.paper.BasicBukkitEnvironment
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.default
import com.zombachu.stick.structure.group
import com.zombachu.stick.structure.invalidDefault
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.optionally
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender

// Scratch space for the variance of E across the element hierarchy. Element, Structure, Group, Flag,
// Command, FailureHandler and the ValidatedDefault family are all contravariant in E, so anything
// written against a base environment stays usable in a command over a richer one. The reverse must not
// compile — see the commented-out lines.
//
// TODO: fold these into the integration tests. Everything here is compile-only; nothing is asserted,
//  and the negative cases are only checked by being commented out.

open class AnimalEnvironment : BasicBukkitEnvironment(fakePlugin) {
    fun describe(name: String): String = "an animal called $name"
}

class DogEnvironment : AnimalEnvironment() {
    fun fetch(name: String): String = "$name fetches the ball"
}

private val dogScope = StructureScope.empty<DogEnvironment, CommandSender>()

// --- elements -------------------------------------------------------------------------------------

class AnimalParameter(name: String) : Parameter.Size1<AnimalEnvironment, CommandSender, String>(name, "") {

    context(inv: Invocation<AnimalEnvironment, CommandSender>)
    override fun parse(arg0: String): CommandResult<String> = ParsingResult.success(inv.env.describe(arg0))
}

class DogParameter(name: String) : Parameter.Size1<DogEnvironment, CommandSender, String>(name, "") {

    context(inv: Invocation<DogEnvironment, CommandSender>)
    override fun parse(arg0: String): CommandResult<String> = ParsingResult.success(inv.env.fetch(arg0))
}

private val animalElement: Element<DogEnvironment, CommandSender, String> = AnimalParameter("pet")
private val animalGroupable: Groupable<DogEnvironment, CommandSender, String> = AnimalParameter("pet")

// Must not compile: a DogEnvironment parameter cannot stand in for an AnimalEnvironment one.
// private val dogElement: Element<AnimalEnvironment, CommandSender, String> = DogParameter("pet")

// --- defaults -------------------------------------------------------------------------------------

private val animalScope = StructureScope.empty<AnimalEnvironment, CommandSender>()

private val absentDefault: ValidSenderDefault<DogEnvironment, CommandSender, String> =
    with(animalScope) { default("stray") }

private val deniedDefault: InvalidSenderDefault<DogEnvironment, CommandSender, String> =
    with(animalScope) { invalidDefault("stray") }

// --- structures, groups and flags -----------------------------------------------------------------

private val animalStructure: Structure<AnimalEnvironment, CommandSender, *> =
    with(animalScope) { command(name = "animal")(AnimalParameter("pet")) {} }

private val animalFlag: ValueFlag<AnimalEnvironment, CommandSender, String> =
    with(animalScope) { valueFlag(name = "kind", default = "stray", parameter = stringParameter("kind")) }

// A group terminates a signature, so it and a trailing optional need separate commands.
private val dogGroupStructure =
    with(dogScope) {
        command(name = "dog")(
            animalFlag,
            // an AnimalEnvironment subcommand composed into a DogEnvironment parent
            group(animalStructure, command(name = "bark")(DogParameter("pet")) {}),
        ) { _, _ ->
        }
    }

private val dogOptionalStructure =
    with(dogScope) {
        command(name = "dog")(
            animalFlag,
            optionally(ifInvalid = deniedDefault, ifAbsent = absentDefault, parameter = AnimalParameter("pet")),
        ) { _, _ ->
        }
    }

// --- commands and failure handlers ----------------------------------------------------------------

class AnimalCommand : Command<AnimalEnvironment, CommandSender> {
    override val structure = with(animalScope) { command(name = "animal")(AnimalParameter("pet")) {} }
}

private val dogCommand: Command<DogEnvironment, CommandSender> = AnimalCommand()

class AnimalFailureHandler : FailureHandler<AnimalEnvironment, CommandSender> {
    context(inv: Invocation<AnimalEnvironment, CommandSender>)
    override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
        inv.sender.sendMessage(inv.env.describe(failure.feedback.message))
    }
}

private val dogFailureHandler: FailureHandler<DogEnvironment, CommandSender> = AnimalFailureHandler()

// --- senders --------------------------------------------------------------------------------------

private val animalValidation: CommandResult<Unit> =
    with(animalScope) { SenderValidationResult.success() }
