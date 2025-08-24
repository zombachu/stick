@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.structure

import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.SenderScope
import kotlin.experimental.ExperimentalTypeInference

@OverloadResolutionByLambdaReturnType
@JvmName("requirement")
fun <S : SenderContext, O> SenderScope<S, O>.requirement(validate: (senderContext: S) -> Result<Unit>): Requirement<S, O> {
    return Requirement(validate)
}

@OverloadResolutionByLambdaReturnType
@JvmName("requirementBoolean")
fun <S : SenderContext, O> SenderScope<S, O>.requirement(
    failureResult: Result<Unit> = SenderValidationResult.failSender(),
    validate: (senderContext: S) -> Boolean
): Requirement<S, O> {
    return Requirement {
        if (validate(it)) {
            SenderValidationResult.success()
        } else {
            failureResult
        }
    }
}
