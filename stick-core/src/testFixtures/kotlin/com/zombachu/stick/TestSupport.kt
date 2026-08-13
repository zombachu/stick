package com.zombachu.stick

import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.InvalidSenderDefault
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.ValidSenderDefault
import com.zombachu.stick.element.ValidatedDefaultImpl
import com.zombachu.stick.element.ValueFlagImpl
import com.zombachu.stick.feedback.CustomFeedback
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import kotlin.test.fail

object TestEnv : Environment

private fun <E : Environment, S> emptyStructure(): Structure<E, S, *> =
    StructureImpl("", [], "", Requirement { SenderValidationResult.success() }, Signature0({}, []))

internal fun testInvocation(
    vararg args: String = [],
): InvocationImpl<TestEnv, Unit> {
    return testInvocationSender(Unit, *args)
}

internal fun <S> testInvocationSender(
    sender: S,
    vararg args: String = [],
): InvocationImpl<TestEnv, S> {
    return Invocation(sender, TestEnv, "", args.asList(), emptyStructure()) as InvocationImpl<TestEnv, S>
}

internal inline fun <T> withValidationContext(
    block: context(ValidationContext<TestEnv, Unit>) () -> T
): T {
    withValidationContext(Unit) {
        return block()
    }
}

internal inline fun <S, T> withValidationContext(
    sender: S,
    block: context(ValidationContext<TestEnv, S>) () -> T
): T {
    val ctx = ValidationContext(TestEnv, sender)
    context(ctx) {
        return block()
    }
}

internal inline fun <T> withInvocation(
    vararg args: String = [],
    block: context(InvocationImpl<TestEnv, Unit>) () -> T,
): T {
    withInvocationSender(Unit, *args) {
        return block()
    }
}

internal inline fun <S, T> withInvocationSender(
    sender: S,
    vararg args: String = [],
    block: context(InvocationImpl<TestEnv, S>) () -> T,
): T {
    val inv = testInvocationSender(sender, *args)
    context(inv) {
        return block()
    }
}

fun <E : Environment, S> noopFailureHandler(): FailureHandler<E, S> =
    object : FailureHandler<E, S> {
        context(inv: Invocation<E, S>)
        override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {}
    }

fun customFailure(message: String): CommandResult.Failure<CustomFeedback> =
    object : ParsingResult.CustomError<CustomFeedback> {
        override val feedback = CustomFeedback { message }
    }

fun <E : Environment, S, T> validSenderDefault(
    value: T,
    validate: context(ValidationContext<E, S>) () -> CommandResult<Unit> = { SenderValidationResult.success() },
): ValidSenderDefault<E, S, T> = ValidatedDefaultImpl({ ParsingResult.success(value) }, validate)

fun <E : Environment, S, T> invalidSenderDefault(
    value: T,
    validate: context(ValidationContext<E, S>) () -> CommandResult<Unit> = { SenderValidationResult.success() },
): InvalidSenderDefault<E, S, T> = ValidatedDefaultImpl({ ParsingResult.success(value) }, validate)

internal fun <E : Environment, S, T> presenceValueFlag(
    name: String,
    default: T,
    presentValue: T,
): ValueFlagImpl<E, S, T> =
    ValueFlagImpl(name, { ParsingResult.success(default) }, presenceFlagParameter(name, presentValue))

internal fun <E : Environment, S, T> presenceFlagParameter(
    name: String,
    presentValue: T,
): FlagParameter.PresenceFlagParameter<E, S, T> =
    FlagParameter.PresenceFlagParameter(name, { ParsingResult.success(presentValue) }, [], "")

fun <T> CommandResult<T>.expectSuccessValue(): T {
    val success = this as? CommandResult.Success<T> ?: fail("Expected success but was $this")
    return success.value
}

fun <T> CommandResult<T>.expectFailure(): CommandResult.Failure<*> {
    return this as? CommandResult.Failure<*> ?: fail("Expected failure but was $this")
}

fun structureTest(block: StructureScope<TestEnv, Unit>.() -> Unit) {
    val scope = StructureScope.empty<TestEnv, Unit>()
    with(scope) {
        block()
    }
}

@JvmName("structureTestSender")
fun <T> structureTest(block: StructureScope<TestEnv, T>.() -> Unit) {
    val scope = StructureScope.empty<TestEnv, T>()
    with(scope) {
        block()
    }
}

@JvmName("structureTestEnvironment")
fun <E : Environment, S> structureTest(block: StructureScope<E, S>.() -> Unit) {
    val scope = StructureScope.empty<E, S>()
    with(scope) {
        block()
    }
}
