@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureScope
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <E : Environment, S> StructureScope<E, S>.requirement(
    validate: (validationContext: ValidationContext<E, S>) -> CommandResult<Unit>
): Requirement<E, S> =
    Requirement(validate)

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <E : Environment, S> StructureScope<E, S>.requirement(
    failureResult: () -> CommandResult.Failure<*> = SenderValidationResult::failSender,
    validate: (validationContext: ValidationContext<E, S>) -> Boolean,
): Requirement<E, S> =
    Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult()
        }
    }

fun <E : Environment, S> StructureScope<E, S>.requirement(
    from: SenderValidator<E, S>
): Requirement<E, S> =
    Requirement { context(it) { from.validateSender() } }
