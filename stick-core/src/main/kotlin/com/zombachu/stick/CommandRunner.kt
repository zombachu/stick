package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.parse
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.Reason

class CommandRunner<E : Environment, S>(
    private val env: E,
    private val failureHandler: FailureHandler<E, S>,
    private val structure: Structure<E, S, *>,
) {

    @Suppress("TooGenericExceptionCaught")
    fun execute(sender: S, label: String, args: List<String>) {
        val fullArgs = [label] + args
        val inv = Invocation(sender, env, label, fullArgs, structure)
        context(env, inv) {
            val result =
                try {
                    val validationResult = structure.validateSender()
                    if (validationResult.isSuccess()) structure.parse(fullArgs) else validationResult
                } catch (e: Exception) {
                    fail(Reason.Unknown(e))
                }
            val reason =
                when (result) {
                    is CommandResult.Failure.NoMatch -> result.reason
                    is CommandResult.Failure.Error -> result.reason
                    else -> return
                }
            failureHandler.onFailure(reason)
        }
    }

    fun canUse(sender: S): Boolean {
        val validationContext = ValidationContext(env, sender)
        context(validationContext) {
            return structure.validateSender().isSuccess()
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
