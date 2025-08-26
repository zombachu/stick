package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.stringParameter
import org.bukkit.command.CommandSender

class CustomCommand : Command<CustomBukkitContext, CommandSender> {
    override val structure = command("hi")(
        Invocation<CustomBukkitContext, CommandSender>::doSomething,
        translatedStringParameter(id("shouldntcompile")),
        scopedTranslatedStringParameter(id("yo")),
    )

    override fun createSenderContext(sender: Any): CustomBukkitContext {
        return CustomBukkitContext(sender)
    }
}

private fun Invocation<CustomBukkitContext, CommandSender>.doSomething(string: String, secondString: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}

class UncustomCommand : Command<BukkitContext, CommandSender> {
    override val structure = command("hi")(
        Invocation<BukkitContext, CommandSender>::doOtherThing,
        stringParameter(id("shouldntcompile")),
//        scopedTranslatedStringParameter(id("yo")),
    )

    override fun createSenderContext(sender: Any): CustomBukkitContext {
        return CustomBukkitContext(sender)
    }
}

private fun Invocation<BukkitContext, CommandSender>.doOtherThing(string: String): ExecutionResult {
    this.sender.sendMessage(string)
    return ExecutionResult.success()
}


class CustomBukkitContext(sender: Any) : BukkitContextImpl(sender) {
    fun translateMessage(string: String): String {
        TODO()
    }
}

fun <S : SenderContext, O : Any> SenderScope<S, O>.translatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, O, TranslatedStringParameter<O>> = {
    TranslatedStringParameter(id, description)
}

fun <O : Any> SenderScope<CustomBukkitContext, O>.scopedTranslatedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<CustomBukkitContext, O, TranslatedStringParameter<O>> = {
    TranslatedStringParameter(id, description)
}

class TranslatedStringParameter<O : Any>(
    id: TypedIdentifier<String>,
    description: String
) : StringParameter<CustomBukkitContext, O>(id, description) {

    context(senderContext: CustomBukkitContext, invocation: Invocation<CustomBukkitContext, O>)
    override fun parse(arg0: String): Result<out String> {
        val translated = senderContext.translateMessage(arg0)
        return super.parse(translated)
    }
}
