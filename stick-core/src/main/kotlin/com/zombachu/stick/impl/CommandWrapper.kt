package com.zombachu.stick.impl

import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.isSuccess

interface CommandWrapper<E : Environment, S> {
    val env: E
    val parsingFailureHandler: ParsingFailureHandler<E, S>
    val structure: Structure<E, S>

    fun execute(sender: S, fullArgs: List<String>) {
        val inv = Invocation(sender, env, fullArgs.first(), fullArgs.subList(1, fullArgs.size), structure)
        context(env, inv) {
            val result = structure.parse(fullArgs)
            if (!result.isSuccess()) {
                parsingFailureHandler.onFailure(inv, result)
            }
        }
    }
}
