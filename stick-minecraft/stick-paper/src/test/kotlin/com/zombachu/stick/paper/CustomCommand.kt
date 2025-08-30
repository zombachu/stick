package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.Environment
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.paper.structure.permissionedValue
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender

class CustomCommand : Command<CustomBukkitEnvironment, CommandSender> {
    override val structure =
        command("hi")(
            translatedStringParameter(id("shouldntcompileifbukkitenv")),
            scopedTranslatedStringParameter(id("yo")),
            valueFlag(
                id("wg"),
                default = permissionedValue(
                    permission = "syn.secretwgs",
                    default = "secretworld",
                    fallback = "lobby"
                ),
                stringParameter(id("worldgroup"))
            ),
            Invocation<CustomBukkitEnvironment, CommandSender>::doSomething,
        )
}

private fun Invocation<CustomBukkitEnvironment, CommandSender>.doSomething(string: String, secondString: String, flagValue: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}

class UncustomCommand : Command<BukkitEnvironment, CommandSender> {
    override val structure =
        command("hi")(
            stringParameter(id("shouldntcompile")),
//        scopedTranslatedStringParameter(id("yo")),
            Invocation<BukkitEnvironment, CommandSender>::doOtherThing,
        )
}

private fun Invocation<BukkitEnvironment, CommandSender>.doOtherThing(string: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}


class CustomBukkitEnvironment : BasicBukkitEnvironment(fakePlugin) {
    fun translateMessage(string: String): String {
        TODO()
    }
}

class CustomFailureHandler : FailureHandler<CustomBukkitEnvironment, CommandSender> {
    override fun onFailure(
        inv: Invocation<CustomBukkitEnvironment, CommandSender>,
        result: Result.Failure<*>,
    ) {
        val message = result.feedback().format()
        if (message.isEmpty()) {
            return
        }
        inv.sender.sendMessage(inv.env.translateMessage(message))
    }
}

fun <E : Environment, S : Any> SenderScope<E, S>.translatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, S, TranslatedStringParameter<S>> = {
    TranslatedStringParameter(id, description)
}

fun <S : Any> SenderScope<CustomBukkitEnvironment, S>.scopedTranslatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<CustomBukkitEnvironment, S, TranslatedStringParameter<S>> = {
    TranslatedStringParameter(id, description)
}

class TranslatedStringParameter<S : Any>(
    id: TypedIdentifier<String>,
    description: String
) : StringParameter<CustomBukkitEnvironment, S>(id, description) {

    context(inv: Invocation<CustomBukkitEnvironment, S>)
    override fun parse(arg0: String): Result<String> {
        val translated = inv.env.translateMessage(arg0)
        return super.parse(translated)
    }
}
