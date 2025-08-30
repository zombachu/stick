package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.impl.CommandScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

class BridgeScope<E : Environment, S : Any>(@PublishedApi internal val bridge: Bridge<E, S>) {

    context(env: E2, failureHandler: FailureHandler<E2, S>)
    inline fun <E2 : E, reified S2 : S> register(command: Command<E2, S2>) {
        @Suppress("UNCHECKED_CAST")
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

abstract class Bridge<E : Environment, S : Any>(
    val platformSenderClass: KClass<S>,
) {
    @OptIn(ExperimentalContracts::class)
    fun <E2 : E> withContext(
        env: E2,
        failureHandler: FailureHandler<E2, S>,
        block: context(E2, FailureHandler<E2, S>) BridgeScope<E, S>.() -> Unit,
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

    @PublishedApi
    context(env: E2, failureHandler: FailureHandler<E2, S>)
    internal fun <E2 : E, S2 : S> internalRegister(
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
