@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.dsl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidator
import com.zombachu.stick.StructureScope
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.failSender
import com.zombachu.stick.success
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <E : Environment, S> StructureScope<E, S>.requirement(
    validate: ValidationContext<E, S>.() -> CommandResult<Unit>
): Requirement<E, S> = Requirement(validate)

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <E : Environment, S> StructureScope<E, S>.requirement(
    failureResult: ValidationContext<E, S>.() -> CommandResult.Failure = { failSender() },
    validate: ValidationContext<E, S>.() -> Boolean,
): Requirement<E, S> = Requirement {
    if (validate()) {
        success()
    } else {
        failureResult()
    }
}

fun <E : Environment, S> StructureScope<E, S>.requirement(from: SenderValidator<E, S>): Requirement<E, S> =
    Requirement {
        from.validateSender()
    }
