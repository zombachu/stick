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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

@PublishedApi
internal interface Registrar<E : Environment, S : Any> {
    context(env: E2, failureHandler: FailureHandler<E2, S>)
    fun <E2 : E, S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    )
}

class BridgeScope<E : Environment, S : Any>
@PublishedApi internal constructor(
    @PublishedApi internal val bridge: Registrar<E, S>
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

abstract class Bridge<in E : Environment, S : Any>(
    val platformSenderClass: KClass<S>,
) : Registrar<@UnsafeVariance E, S> {

    @OptIn(ExperimentalContracts::class)
    inline fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<@UnsafeVariance E, S>.() -> Unit,
    ) {
        contract {
            callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        }

        with(BridgeScope(this)) {
            context(env, failureHandler) {
                block()
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    inline fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        noinline transform: (S) -> S2,
        noinline validate: (validationContext: ValidationContext<E2, S>) -> Result<Unit>,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<E2, S2>.() -> Unit,
    ) {
        contract {
            callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        }

        val newBridge = TransformedBridge(transform, Requirement(validate), this)
        with(BridgeScope(newBridge)) {
            context(env, failureHandler) {
                block()
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    inline fun <E2 : E, S2 : Any> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S2>,
        noinline transform: (S) -> S2,
        failureResult: Result<Unit>,
        noinline validate: (validationContext: ValidationContext<E2, S>) -> Boolean,
        block: context(E2, FailureHandler<E2, S2>) BridgeScope<E2, S2>.() -> Unit,
    ) {
        contract {
            callsInPlace(block, InvocationKind.AT_MOST_ONCE)
        }

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

@PublishedApi
internal class TransformedBridge<E : Environment, S0 : Any, S : Any>(
    val transform: (S0) -> S,
    val requirement: Requirement<E, S0>,
    val bridge: Bridge<E, S0>,
) : Registrar<E, S>, SenderValidator<E, S0> {

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    override fun <E2 : E, S2 : Any> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val newFailureHandler = TransformedFailureHandler(failureHandler, transform)
        context(newFailureHandler) {
            bridge.internalRegister(
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
    override fun validateSender(): Result<Unit> {
        return requirement.validateSender()
    }
}
