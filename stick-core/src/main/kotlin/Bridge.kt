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
    val parsingFailureHandler: ParsingFailureHandler<E, S>,
) {
    protected abstract fun registerCommand(structure: Structure<E, S>, createEnvironment: (S) -> E)

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
    internal fun <E2 : E, S2 : S> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<E2, S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val emptyContext = StructureScope.empty<E, S>()
        val structureElement: StructureElement<E, S, Structure<E, S>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<E, S>).structure
            } else {
                with(emptyContext) {
                    // TODO: Handle safer
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult.failSenderType()) { isSenderRequiredType(it.sender as S) },
                    ) {
                        command.structure as StructureElement<E, S2, Structure<E, S2>>
                    }
                }
            }

        val structure = structureElement(emptyContext)
        registerCommand(structure, command::createEnvironment)
    }
}
