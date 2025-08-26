package com.zombachu.stick.impl

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.structure.id

typealias StructureElement<E, O, T> = StructureScope<E, O>.() -> T

class StructureScope<E : Environment, O>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureScope<*, *>?,
    internal val requirement: Requirement<E, O>,
): SenderScope<E, O> {

    private val root: StructureScope<*, *> = parent?.root ?: this

    internal fun <E2 : Environment, O2> forSender(): StructureScope<E2, O2> {
        return StructureScope(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // To get to this point they must have already passed the previous requirement so should be safe to set to true
            requirement = Requirement { SenderValidationResult.success() }
        )
    }

    internal fun build(signature: Signature<E, O>): StructureImpl<E, O> {
        return StructureImpl(
            id(this.name),
            this.aliases,
            this.description,
            this.requirement,
            signature
        )
    }

    companion object {
        fun <E : Environment, O> empty(): StructureScope<E, O> = StructureScope(
            name = "",
            aliases = setOf(),
            description = "",
            parent = null,
            requirement = Requirement { SenderValidationResult.success() }
        )
    }
}
