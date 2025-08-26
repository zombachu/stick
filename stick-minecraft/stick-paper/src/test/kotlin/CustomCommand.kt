package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import org.bukkit.command.CommandSender

class CustomCommand : Command<CustomBukkitEnvironment, CommandSender> {
    override val structure = command("hi")(
        Invocation<CustomBukkitEnvironment, CommandSender>::doSomething,
        translatedStringParameter(id("shouldntcompile")),
        scopedTranslatedStringParameter(id("yo")),
    )

    override fun createEnvironment(sender: Any): CustomBukkitEnvironment {
        return CustomBukkitEnvironment(sender)
    }
}

private fun Invocation<CustomBukkitEnvironment, CommandSender>.doSomething(string: String, secondString: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}

class UncustomCommand : Command<BukkitEnvironment, CommandSender> {
    override val structure = command("hi")(
        Invocation<BukkitEnvironment, CommandSender>::doOtherThing,
        stringParameter(id("shouldntcompile")),
//        scopedTranslatedStringParameter(id("yo")),
    )

    override fun createEnvironment(sender: Any): CustomBukkitEnvironment {
        return CustomBukkitEnvironment(sender)
    }
}

private fun Invocation<BukkitEnvironment, CommandSender>.doOtherThing(string: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}


class CustomBukkitEnvironment(sender: Any) : BukkitEnvironmentImpl(sender) {
    fun translateMessage(string: String): String {
        TODO()
    }
}

fun <E : Environment, O : Any> SenderScope<E, O>.translatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, O, TranslatedStringParameter<O>> = {
    TranslatedStringParameter(id, description)
}

fun <O : Any> SenderScope<CustomBukkitEnvironment, O>.scopedTranslatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<CustomBukkitEnvironment, O, TranslatedStringParameter<O>> = {
    TranslatedStringParameter(id, description)
}

class TranslatedStringParameter<O : Any>(
    id: TypedIdentifier<String>,
    description: String
) : StringParameter<CustomBukkitEnvironment, O>(id, description) {

    context(env: CustomBukkitEnvironment, inv: Invocation<CustomBukkitEnvironment, O>)
    override fun parse(arg0: String): Result<out String> {
        val translated = env.translateMessage(arg0)
        return super.parse(translated)
    }
}
