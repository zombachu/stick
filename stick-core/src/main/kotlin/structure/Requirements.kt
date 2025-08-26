@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Result
import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <E : Environment, O> SenderScope<E, O>.requirement(validate: (env: E) -> Result<Unit>): Requirement<E, O> {
    return Requirement(validate)
}

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <E : Environment, O> SenderScope<E, O>.requirement(
    failureResult: Result<Unit> = SenderValidationResult.failSender(),
    validate: (env: E) -> Boolean
): Requirement<E, O> {
    return Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult
        }
    }
}
