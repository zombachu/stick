package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<S : SenderContext, O : Any>(
    val platformSenderClass: KClass<O>,
    val parsingFailureHandler: ParsingFailureHandler<S, O>,
) {
    protected abstract fun registerCommand(structure: Structure<S, O>, createSenderContext: (O) -> S)

    inline fun <S2 : S, reified O2 : O> register(command: Command<S2, O2>) {
        @Suppress("UNCHECKED_CAST")
        internalRegister(
            O2::class,
            command,
            { it is O2 },
            { it as O2 }
        )
    }

    @PublishedApi
    internal fun <S2 : S, O2 : O> internalRegister(
        commandSenderClass: KClass<O2>,
        command: Command<S2, O2>,
        isSenderRequiredType: (O) -> Boolean,
        castSender: (O) -> O2,
    ) {
        val emptyContext = StructureScope.empty<S, O>()
        val structureElement: StructureElement<S, O, Structure<S, O>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<S, O>).structure
            } else {
                with(emptyContext) {
                    // TODO: Handle safer
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult.failSenderType()) { isSenderRequiredType(it.sender as O) },
                    ) {
                        command.structure as StructureElement<S, O2, Structure<S, O2>>
                    }
                }
            }

        val structure = structureElement(emptyContext)
        registerCommand(structure, command::createSenderContext)
    }
}
