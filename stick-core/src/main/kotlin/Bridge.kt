package com.zombachu.stick

import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.ParsingFailureHandler
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.structure.requireIs

interface Bridge<S> {
    val parsingFailureHandler: ParsingFailureHandler<S>

    fun registerStructure(structure: Structure<S>)
}

inline fun <reified S : Any, reified S2 : S> Bridge<S>.registerCommand(command: Command<S2>) {
    val emptyContext = StructureScope.empty<S>()
    val structureElement: StructureElement<S, Structure<S>> =
        if (S2::class == S::class) {
            (command as Command<S>).structure
        } else {
            emptyContext.requireIs(S2::class) { command.structure }
        }

    val structure = structureElement(emptyContext)
    registerStructure(structure)
}
