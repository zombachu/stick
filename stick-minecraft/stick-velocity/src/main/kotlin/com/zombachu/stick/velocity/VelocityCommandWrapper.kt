package com.zombachu.stick.velocity

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.RawCommand
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.CommandWrapper

class VelocityCommandWrapper<E : VelocityEnvironment>(
    override val env: E,
    override val parsingFailureHandler: ParsingFailureHandler<E, CommandSource>,
    override val structure: Structure<E, CommandSource>,
) : RawCommand, CommandWrapper<E, CommandSource> {

    override fun execute(invocation: RawCommand.Invocation) {
        execute(invocation.source(), listOf(invocation.alias(), *invocation.arguments().split(' ').toTypedArray()))
    }
}
