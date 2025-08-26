package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<E : Environment, O : Any>(
    val platformSenderClass: KClass<O>,
    val parsingFailureHandler: ParsingFailureHandler<E, O>,
) {
    protected abstract fun registerCommand(structure: Structure<E, O>, createEnvironment: (O) -> E)

    inline fun <E2 : E, reified O2 : O> register(command: Command<E2, O2>) {
        @Suppress("UNCHECKED_CAST")
        internalRegister(
            O2::class,
            command,
            { it is O2 },
            { it as O2 }
        )
    }

    @PublishedApi
    internal fun <E2 : E, O2 : O> internalRegister(
        commandSenderClass: KClass<O2>,
        command: Command<E2, O2>,
        isSenderRequiredType: (O) -> Boolean,
        castSender: (O) -> O2,
    ) {
        val emptyContext = StructureScope.empty<E, O>()
        val structureElement: StructureElement<E, O, Structure<E, O>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<E, O>).structure
            } else {
                with(emptyContext) {
                    // TODO: Handle safer
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult.failSenderType()) { isSenderRequiredType(it.sender as O) },
                    ) {
                        command.structure as StructureElement<E, O2, Structure<E, O2>>
                    }
                }
            }

        val structure = structureElement(emptyContext)
        registerCommand(structure, command::createEnvironment)
    }
}
