@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Environment
import com.zombachu.stick.Result
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <E : Environment, S> SenderScope<E, S>.requirement(
    validate: (validationContext: ValidationContext<E, S>) -> Result<Unit>
): Requirement<E, S> {
    return Requirement(validate)
}

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <E : Environment, S> SenderScope<E, S>.requirement(
    failureResult: Result<Unit> = SenderValidationResult.failSender(),
    validate: (validationContext: ValidationContext<E, S>) -> Boolean,
): Requirement<E, S> {
    return Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult
        }
    }
}
