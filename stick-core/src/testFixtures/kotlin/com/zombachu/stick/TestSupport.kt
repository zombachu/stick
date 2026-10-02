package com.zombachu.stick

import com.zombachu.stick.element.FlagParameter
import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.ValueFlagImpl
import com.zombachu.stick.failure.CustomReason
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.failure.Reason
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.fail

object TestEnv : Environment

private fun <E : Environment, S> emptyStructure(): Structure<E, S, *> =
    StructureImpl<E, S, Arguments0>("", [], "") {
        Signature0({}, LeadingParameterRole.Label, [it])
    }

internal fun testExecution(
    vararg args: String = [],
): ExecutionImpl<TestEnv, Unit> {
    return testExecutionSender(Unit, *args)
}

internal fun <S> testExecutionSender(
    sender: S,
    vararg args: String = [],
): ExecutionImpl<TestEnv, S> {
    return Execution(sender, TestEnv, "", args.asList(), emptyStructure()) as ExecutionImpl<TestEnv, S>
}

fun <E : Environment, S> testExecution(env: E, sender: S): Execution<E, S> =
    Execution(sender, env, "", [], emptyStructure())

fun <E : Environment, S> testInvocation(env: E, sender: S): Invocation<E, S> =
    Invocation(env, sender)

fun <T> runSync(block: suspend () -> T): T {
    var result: Result<T>? = null
    startUndispatched(StickCoroutineContext(EmptyCoroutineContext, EmptyCoroutineContext), block) { result = it }
    return (result ?: fail()).getOrThrow()
}

internal fun <T> withInvocation(block: suspend context(Invocation<TestEnv, Unit>) () -> T): T =
    withInvocation(Unit, block)

internal fun <S, T> withInvocation(sender: S, block: suspend context(Invocation<TestEnv, S>) () -> T): T {
    val ctx = Invocation(TestEnv, sender)
    return runSync { context(ctx) { block() } }
}

internal fun <T> withExecution(
    vararg args: String = [],
    block: suspend context(ExecutionImpl<TestEnv, Unit>) () -> T,
): T =
    withExecutionSender(Unit, *args, block = block)

internal fun <S, T> withExecutionSender(
    sender: S,
    vararg args: String = [],
    block: suspend context(ExecutionImpl<TestEnv, S>) () -> T,
): T {
    val ex = testExecutionSender(sender, *args)
    return runSync { context(ex) { block() } }
}

fun <E : Environment, S> noopFailureHandler(): FailureHandler<E, S> =
    object : FailureHandler<E, S> {
        context(ex: Execution<E, S>)
        override fun onFailure(reason: Reason, origin: FailureOrigin) {}
    }

fun failureOrigin(usage: String = "", elementName: String? = null): FailureOrigin = FailureOrigin(elementName) { usage }

data class MessageReason(val text: String) : CustomReason {
    override fun message(origin: FailureOrigin) = text
}

internal fun <E : Environment, S, T> presenceValueFlag(
    name: String,
    default: T,
    presentValue: T,
): ValueFlagImpl<E, S, T> =
    ValueFlagImpl(name, { success(default) }, presenceFlagParameter(name, presentValue))

internal fun <E : Environment, S, T> presenceFlagParameter(
    name: String,
    presentValue: T,
): FlagParameter.PresenceFlagParameter<E, S, T> =
    FlagParameter.PresenceFlagParameter(name, { success(presentValue) }, [], "")

fun <T> CommandResult<T>.expectSuccessValue(): T {
    val success = this as? CommandResult.Success<T> ?: fail("Expected success but was $this")
    return success.value
}

fun <T> CommandResult<T>.expectReason(): Reason =
    (this as? CommandResult.Failure.Unhandled)?.reason ?: fail("Expected NoMatch or Error but was $this")

fun <T> CommandResult<T>.expectNoMatch(): CommandResult.Failure.NoMatch {
    return this as? CommandResult.Failure.NoMatch ?: fail("Expected NoMatch but was $this")
}

fun <T> CommandResult<T>.expectError(): CommandResult.Failure.Error {
    return this as? CommandResult.Failure.Error ?: fail("Expected Error but was $this")
}

fun MatchResult.expectUnmatched(): CommandResult.Failure {
    val unmatched = this as? MatchResult.Unmatched ?: fail("Expected unmatched but was $this")
    return unmatched.failure
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
