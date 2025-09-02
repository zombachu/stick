@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Requirement
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <E : Environment, S> BuilderScope<E, S>.requirement(
    validate: (validationContext: ValidationContext<E, S>) -> CommandResult<Unit>
): Requirement<E, S> {
    return Requirement(validate)
}

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <E : Environment, S> BuilderScope<E, S>.requirement(
    failureResult: () -> CommandResult.Failure<Unit> = SenderValidationResult::failSender,
    validate: (validationContext: ValidationContext<E, S>) -> Boolean,
): Requirement<E, S> {
    return Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult()
        }
    }
}
