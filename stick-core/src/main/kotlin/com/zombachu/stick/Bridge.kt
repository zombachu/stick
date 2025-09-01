package com.zombachu.stick

import com.zombachu.stick.element.SenderValidator
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.TransformedFailureHandler
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<in E : Environment, S : Any>(
    val platformSenderClass: KClass<S>,
) : CommandRegistrar<@UnsafeVariance E, S> {

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<@UnsafeVariance E, S>.() -> Unit,
    ) {
        with(BridgeScope(this)) {
            context(env, failureHandler) {
                block()
            }
        }
    }

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        transform: (S) -> S2,
        validate: (validationContext: ValidationContext<E2, S>) -> CommandResult<Unit>,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<E2, S2>.() -> Unit,
    ) {
        val transformedBridge = TransformedBridge(this, transform, Requirement(validate))
        with(BridgeScope(transformedBridge)) {
            context(env, failureHandler) {
                block()
            }
        }
    }

    fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        transform: (S) -> S2,
        failureResult: CommandResult<Unit>,
        validate: (validationContext: ValidationContext<E2, S>) -> Boolean,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<E2, S2>.() -> Unit,
    ) {
        withContext(
            env,
            failureHandler,
            transform,
            { if (validate(it)) SenderValidationResult.success() else failureResult },
            block
        )
    }

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    override fun <E2 : E, S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val emptyContext: StructureScope<E2, S> = StructureScope.empty()
        val structureElement: StructureElement<E2, S, Structure<E2, S>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<E2, S>).structure
            } else {
                with(emptyContext) {
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult.failSenderType()) { isSenderRequiredType(it.sender) },
                    ) {
                        command.structure
                    }
                }
            }

        val structure: Structure<E2, S> = structureElement(emptyContext)
        registerCommand(structure)
    }

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    protected abstract fun <E2 : E> registerCommand(structure: Structure<E2, S>)
}

class BridgeScope<E : Environment, S : Any>
@PublishedApi internal constructor(
    @PublishedApi internal val bridge: CommandRegistrar<E, S>
) {

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    inline fun <E2 : E, reified S2 : S> register(command: Command<E2, S2>) {
        bridge.internalRegister(
            S2::class,
            command,
            { it is S2 },
            { it as S2 }
        )
    }

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    inline fun <E2 : E, reified S2 : S> register(
        block: CommandScope<E2, S2>.() -> StructureElement<E2, S2, Structure<E2, S2>>
    ) {
        val commandScope = object : CommandScope<E2, S2> { }
        val structure = block(commandScope)
        val command = object : Command<E2, S2> {
            override val structure: StructureElement<E2, S2, Structure<E2, S2>> = structure
        }
        register(command)
    }
}

@PublishedApi
internal interface CommandRegistrar<E : Environment, S : Any> {
    context(env: E2, failureHandler: FailureHandler<E2, S>)
    fun <E2 : E, S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    )
}

@PublishedApi
internal class TransformedBridge<E : Environment, S0 : Any, S : Any>(
    val base: Bridge<E, S0>,
    val transform: (S0) -> S,
    val requirement: Requirement<E, S0>,
) : CommandRegistrar<E, S>, SenderValidator<E, S0> {

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    override fun <E2 : E, S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val transformedFailureHandler = TransformedFailureHandler(failureHandler, transform)
        context(transformedFailureHandler) {
            base.internalRegister(
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
