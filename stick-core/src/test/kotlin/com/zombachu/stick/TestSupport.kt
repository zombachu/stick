package com.zombachu.stick

import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.impl.InvocationImpl
import com.zombachu.stick.impl.Requirement
import kotlin.test.fail

object TestEnv : Environment

private fun <E : Environment, S> emptyStructure(): Structure<E, S, *> =
    StructureImpl("", [], "", Requirement { SenderValidationResult.success() }, Signature0({}, []))

internal fun <S> testInvocation(
    sender: S,
    args: List<String> = [],
): InvocationImpl<TestEnv, S> {
    return Invocation(sender, TestEnv, "test", args, emptyStructure()) as InvocationImpl<TestEnv, S>
}

internal inline fun <S, T> withValidationContext(sender: S, block: context(ValidationContext<TestEnv, S>) () -> T): T {
    val ctx = ValidationContext(TestEnv, sender)
    context(ctx) {
        return block()
    }
}

internal inline fun <S, T> withInvocation(
    sender: S,
    args: List<String> = emptyList(),
    block: context(InvocationImpl<TestEnv, S>) () -> T,
): T {
    val inv = testInvocation(sender, args)
    context(inv) {
        return block()
    }
}

fun <T> CommandResult<T>.expectSuccessValue(): T {
    val success = this as? CommandResult.Success<T> ?: fail("Expected success but was $this")
    return success.value
}

fun <T> CommandResult<T>.expectFailure(): CommandResult.Failure<*> {
    return this as? CommandResult.Failure<*> ?: fail("Expected failure but was $this")
}
