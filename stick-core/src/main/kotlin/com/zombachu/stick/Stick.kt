package com.zombachu.stick

import com.zombachu.stick.dsl.requireAs
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.TransformedFailureHandler
import kotlin.reflect.KClass

abstract class Stick<E : Environment, S : Any>(
    val platformSenderClass: KClass<S>,
    private val defaultEnvironment: Lazy<E>,
    private val defaultFailureHandler: Lazy<FailureHandler<E, S>>,
) : CommandRegistrar<E, S> {

    fun <E2 : E> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S> = defaultFailureHandler.value,
        block: context(E2, FailureHandler<E2, S>) StickScope<E2, S>.() -> Unit,
    ) {
        val transformedStick: TransformedStick<E, E2, S, S> =
            TransformedStick(this, { it }, Requirement { SenderValidationResult.success() })
        with(StickScope(transformedStick)) { context(env, failureHandler) { block() } }
    }

    fun withContext(
        failureHandler: FailureHandler<E, S> = defaultFailureHandler.value,
        block: context(E, FailureHandler<E, S>) StickScope<E, S>.() -> Unit,
    ) = withContext(defaultEnvironment.value, failureHandler, block)

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        transform: (S) -> S2,
        validate: (validationContext: ValidationContext<E2, S>) -> CommandResult<Unit>,
        block: context(E2, FailureHandler<E2, S2>) StickScope<E2, S2>.() -> Unit,
    ) {
        val transformedStick: TransformedStick<E, E2, S, S2> = TransformedStick(this, transform, Requirement(validate))
        with(StickScope(transformedStick)) { context(env, failureHandler) { block() } }
    }

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        transform: (S) -> S2,
        failureResult: CommandResult<Unit>,
        validate: (validationContext: ValidationContext<E2, S>) -> Boolean,
        block: context(E2, FailureHandler<E2, S2>) StickScope<E2, S2>.() -> Unit,
    ) {
        withContext(
            env,
            failureHandler,
            transform,
            { if (validate(it)) SenderValidationResult.success() else failureResult },
            block,
        )
    }

    context(env: E, failureHandler: FailureHandler<E, S>)
    override fun <S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val emptyContext: StructureScope<E, S> = StructureScope.empty()
        val structure: Structure<E, S, *> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST") (command as Command<E, S>).structure
            } else {
                with(emptyContext) {
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult::failSenderType) { isSenderRequiredType(it.sender) },
                    ) {
                        command.structure
                    }
                }
            }
        registerCommand(structure)
    }

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    protected abstract fun <E2 : E> registerCommand(structure: Structure<E2, S, *>)
}

class StickScope<E : Environment, S : Any>
@PublishedApi
internal constructor(@PublishedApi internal val stick: CommandRegistrar<E, S>) {

    context(env: E, failureHandler: FailureHandler<E, S>)
    inline fun <reified S2 : S> register(command: Command<E, S2>) {
        stick.internalRegister(S2::class, command, { it is S2 }, { it as S2 })
    }

    context(env: E, failureHandler: FailureHandler<E, S>)
    inline fun <reified S2 : S> register(structure: Structure<E, S2, *>) {
        val command =
            object : Command<E, S2> {
                override val structure: Structure<E, S2, *> = structure
            }
        register(command)
    }

    context(env: E, failureHandler: FailureHandler<E, S>)
    inline fun <reified S2 : S> register(noinline structure: StructureScope<E, S2>.() -> Structure<E, S2, *>) {
        val emptyContext = StructureScope.empty<E, S2>()
        register(structure(emptyContext))
    }
}

@PublishedApi
internal interface CommandRegistrar<E : Environment, S : Any> {
    context(env: E, failureHandler: FailureHandler<E, S>)
    fun <S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    )
}

@PublishedApi
internal class TransformedStick<E0 : Environment, E : E0, S0 : Any, S : Any>(
    val base: Stick<E0, S0>,
    val transform: (S0) -> S,
    val requirement: Requirement<E, S0>,
) : CommandRegistrar<E, S>, SenderValidator<E, S0> {

    context(env: E, failureHandler: FailureHandler<E, S>)
    override fun <S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val transformedFailureHandler: FailureHandler<E, S0> = TransformedFailureHandler(failureHandler, transform)
        context(transformedFailureHandler) {
            @Suppress("UNCHECKED_CAST")
            (base as Stick<E, S0>).internalRegister(
                commandSenderClass,
                command,
                {
                    val validationContext: ValidationContext<E, S0> = ValidationContext(env, it)
                    context(validationContext) {
                        requirement.validateSender().isSuccess() && isSenderRequiredType(transform(it))
                    }
                },
                { castSender(transform(it)) },
            )
        }
    }

    context(validationContext: ValidationContext<E, S0>)
    override fun validateSender(): CommandResult<Unit> {
        return requirement.validateSender()
    }
}
