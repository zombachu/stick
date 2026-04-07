package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.feedback.CustomFeedback
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.handle
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.paper.structure.permissionedValue
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.structure
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender

class CustomCommand : Command<CustomBukkitEnvironment, CommandSender> {
    override val structure by structure {
        command("hi")(
            translatedStringParameter("shouldntcompileifbukkitenv"),
            scopedTranslatedStringParameter("yo"),
            valueFlag(
                name = "wg",
                default = permissionedValue(
                    permission = "syn.secretwgs",
                    default = "secretworld",
                    fallback = "lobby"
                ),
                stringParameter("worldgroup"),
            ),
            Invocation<CustomBukkitEnvironment, CommandSender>::doSomething,
        )
    }
}

private fun Invocation<CustomBukkitEnvironment, CommandSender>.doSomething(string: String, secondString: String, flagValue: String) {
    this.sender.sendMessage(string)
}

class UncustomCommand : Command<BukkitEnvironment, CommandSender> {
    override val structure by structure {
        command("hi")(
            stringParameter("shouldntcompile"),
//        scopedTranslatedStringParameter(id("yo")),
            Invocation<BukkitEnvironment, CommandSender>::doOtherThing,
        )
    }
}

private fun Invocation<BukkitEnvironment, CommandSender>.doOtherThing(string: String) {
    this.sender.sendMessage(string)
}


class CustomBukkitEnvironment : BasicBukkitEnvironment(fakePlugin) {
    fun translateMessage(string: String): String {
        TODO()
    }
}

class CustomFailureHandler : FailureHandler<CustomBukkitEnvironment, CommandSender> {
    context(inv: Invocation<CustomBukkitEnvironment, CommandSender>)
    override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
        failure.handle {
            when (this) {
                is Feedback.OutOfRange -> { inv.sender.sendMessage("min: ${min} max: ${max}") }
                is Feedback.InvalidSyntax -> { inv.sender.sendMessage("correct syntax: ${usage} ") }
                Feedback.InvalidPermission,
                Feedback.InvalidSender,
                Feedback.InvalidSenderType,
                is Feedback.LiteralNotMatched,
                is Feedback.TypeNotMatched,
                Feedback.Unknown -> defaultProcess(failure)
                is CustomFeedback -> TODO()
            }
        }
    }

    context(inv: Invocation<CustomBukkitEnvironment, CommandSender>)
    fun defaultProcess(failure: CommandResult.Failure<*>) {
        val message = failure.feedback.message
        if (message.isEmpty()) { return }
        inv.sender.sendMessage(inv.env.translateMessage(message))
    }
}

fun <E : Environment, S : Any> StructureScope<E, S>.translatedStringParameter(
    name: String,
    description: String = "",
): TranslatedStringParameter<S> =
    TranslatedStringParameter(name, description)

fun <S : Any> StructureScope<CustomBukkitEnvironment, S>.scopedTranslatedStringParameter(
    name: String,
    description: String = "",
): TranslatedStringParameter<S> =
    TranslatedStringParameter(name, description)

class TranslatedStringParameter<S : Any>(
    name: String,
    description: String
) : StringParameter<CustomBukkitEnvironment, S>(name, description) {

    context(inv: Invocation<CustomBukkitEnvironment, S>)
    override fun parse(arg0: String): CommandResult<String> {
        val translated = inv.env.translateMessage(arg0)
        return super.parse(translated)
    }
}
