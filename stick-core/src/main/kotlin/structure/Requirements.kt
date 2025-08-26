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
fun <S : Environment, O> SenderScope<S, O>.requirement(validate: (env: S) -> Result<Unit>): Requirement<S, O> {
    return Requirement(validate)
}

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <S : Environment, O> SenderScope<S, O>.requirement(
    failureResult: Result<Unit> = SenderValidationResult.failSender(),
    validate: (env: S) -> Boolean
): Requirement<S, O> {
    return Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult
        }
    }
}
