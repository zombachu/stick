package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.impl.CommandWrapper
import com.zombachu.stick.isSuccess
import kotlin.collections.addAll

class VelocityCommandWrapper<E : VelocityEnvironment>(
    override val env: E,
    override val failureHandler: FailureHandler<E, CommandSource>,
    override val structure: Structure<E, CommandSource, *>,
) : RawCommand, CommandWrapper<E, CommandSource> {

    override fun execute(invocation: RawCommand.Invocation) {
        val args = invocation.arguments().split(' ').filter { it.isNotEmpty() }
        val fullArgs = buildList(args.size + 1) {
            add(invocation.alias())
            addAll(args)
        }
        execute(invocation.source(), fullArgs)
    }

    override fun hasPermission(invocation: RawCommand.Invocation): Boolean {
        val validationContext = ValidationContext(env, invocation.source())
        context(validationContext) {
            return structure.validateSender().isSuccess()
        }
    }

    //    override fun suggest(invocation: RawCommand.Invocation?): List<String?>? {
    //        return super.suggest(invocation)
    //    }
    //
    //    override fun suggestAsync(invocation: RawCommand.Invocation?): CompletableFuture<List<String?>?>? {
    //        return super.suggestAsync(invocation)
    //    }
}
