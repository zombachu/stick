package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<E : Environment, S : Any>(
    val platformSenderClass: KClass<S>,
) {
    context(env: E2, parsingFailureHandler: ParsingFailureHandler<E2, S>)
    protected abstract fun <E2 : E> registerCommand(structure: Structure<E2, S>)

    context(env: E2, parsingFailureHandler: ParsingFailureHandler<out E2, S>)
    inline fun <E2 : E, reified S2 : S> register(command: Command<E2, S2>) {
        @Suppress("UNCHECKED_CAST")
        internalRegister(
            S2::class,
            command,
            { it is S2 },
            { it as S2 }
        )
    }

    @PublishedApi
    context(env: E2, parsingFailureHandler: ParsingFailureHandler<out E2, S>)
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
                        // TODO: Handle safer
                        command.structure as StructureElement<E2, S2, Structure<E2, S2>>
                    }
                }
            }

        val structure: Structure<E2, S> = structureElement(emptyContext)
        // TODO: Handle safer
        with(parsingFailureHandler as ParsingFailureHandler<E2, S>) {
            registerCommand(structure)
        }
    }
}
