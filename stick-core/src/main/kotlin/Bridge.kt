package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<O : Any, S : SenderContext<O>>(
    val platformSenderClass: KClass<O>,
    val parsingFailureHandler: ParsingFailureHandler<O, S>,
) {
    protected abstract fun registerStructure(structure: Structure<O, S>)

    inline fun <reified O2 : O, S2 : SenderContext<O2>> register(command: Command<O2, S2>) {
        @Suppress("UNCHECKED_CAST")
        internalRegister(
            O2::class,
            command,
            { it is O2 },
            { it as O2 }
        )
    }

    @PublishedApi
    internal fun <O2 : O, S2 : SenderContext<O2>> internalRegister(
        commandSenderClass: KClass<O2>,
        command: Command<O2, S2>,
        isSenderRequiredType: (O) -> Boolean,
        castSender: (O) -> O2,
    ) {
        val emptyContext = StructureScope.empty<O, S>()
        val structureElement: StructureElement<O, S, Structure<O, S>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<O, S>).structure
            } else {
                with(emptyContext) {
                    requireAs<O, S, O2, S2>(
                        castSender,
                        requirement(SenderValidationResult.failSenderType()) { isSenderRequiredType(it.sender) },
                    ) {
                        command.structure
                    }
                }
            }

        val structure = structureElement(emptyContext)
        registerStructure(structure)
    }
}
