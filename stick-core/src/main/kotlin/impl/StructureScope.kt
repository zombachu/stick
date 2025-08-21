package com.zombachu.stick.impl

import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.structure.id

typealias StructureElement<S, T> = StructureScope<S>.() -> T

class StructureScope<S>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureScope<*>?,
    internal val requirement: Requirement<S>,
): SenderScope<S> {

    private val root: StructureScope<*> = parent?.root ?: this

    internal fun <S2> forSender(): StructureScope<S2> {
        return StructureScope(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // To get to this point they must have already passed the previous requirement so should be safe to set to true
            requirement = Requirement { SenderValidationResult.success() }
        )
    }

    internal fun build(signature: Signature<S>): StructureImpl<S> {
        return StructureImpl(
            id(this.name),
            this.aliases,
            this.description,
            this.requirement,
            signature
        )
    }

    companion object {
        fun <S> empty(): StructureScope<S> = StructureScope(
            name = "",
            aliases = setOf(),
            description = "",
            parent = null,
            requirement = Requirement { SenderValidationResult.success() }
        )
    }
}
