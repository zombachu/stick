package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requirement
import kotlin.reflect.KClass

abstract class Bridge<S : SenderContext>(
    val platformSenderClass: KClass<S>,
    val parsingFailureHandler: ParsingFailureHandler<S>,
) {
    protected abstract fun registerStructure(structure: Structure<S>)

    inline fun <reified S2 : S> register(command: Command<S2>) {
        @Suppress("UNCHECKED_CAST")
        internalRegister(
            S2::class,
            command,
            { it is S2 },
            { it as S2 }
        )
    }

    @PublishedApi
    internal fun <S2 : S> internalRegister(
        commandSenderClass: KClass<S2>,
        command: Command<S2>,
        isSenderRequiredType: (S) -> Boolean,
        castSender: (S) -> S2,
    ) {
        val emptyContext = StructureScope.empty<S>()
        val structureElement: StructureElement<S, Structure<S>> =
            if (commandSenderClass == platformSenderClass) {
                @Suppress("UNCHECKED_CAST")
                (command as Command<S>).structure
            } else {
                with(emptyContext) {
                    requireAs(
                        castSender,
                        requirement(SenderValidationResult.failSenderType(), isSenderRequiredType),
                    ) {
                        command.structure
                    }
                }
            }

        val structure = structureElement(emptyContext)
        registerStructure(structure)
    }
}
