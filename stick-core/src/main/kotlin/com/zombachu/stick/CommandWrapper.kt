package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler

interface CommandWrapper<E : Environment, S> {
    val env: E
    val failureHandler: FailureHandler<E, S>
    val structure: Structure<E, S, *>

    @Suppress("TooGenericExceptionCaught")
    fun execute(sender: S, fullArgs: List<String>) {
        val inv = Invocation(sender, env, fullArgs.first(), fullArgs, structure)
        context(env, inv) {
            val result =
                try {
                    val validationResult = structure.validateSender()
                    if (validationResult.isSuccess()) structure.parse(fullArgs) else validationResult
                } catch (e: Exception) {
                    ParsingResult.failUnknown(e)
                }
            // Ignore InternalFailures
            if (result is CommandResult.Failure<*>) {
                failureHandler.onFailure(result)
            }
        }
    }

    fun suggest(sender: S, label: String, args: List<String>): List<String> {
        if (args.isEmpty()) return []

        val preceding = [label] + args.dropLast(1).filter { it.isNotEmpty() }
        val partial = args.last()
        val validationContext = ValidationContext(env, sender)
        context(validationContext) {
            return try {
                if (structure.validateSender().isSuccess()) {
                    structure
                        .suggest(preceding, partial)
                        .filter { it.isSuggestedFor(partial) }
                        .map { it.applyTo(partial) }
                        .distinct()
                } else {
                    []
                }
            } catch (_: Exception) {
                []
            }
        }
    }
}

private fun Suggestion.isSuggestedFor(partial: String): Boolean {
    // Allows for completions for things like minecraft:<material> to complete <material>
    val unmatched = partial.drop(index)
    // Don't suggest aliases when the user hasn't typed anything, to avoid clutter
    if (isAlias && unmatched.isEmpty()) return false
    return value.length > unmatched.length && value.startsWith(unmatched, ignoreCase = true)
}

private fun Suggestion.applyTo(partial: String): String = if (index == 0) value else partial.take(index) + value
