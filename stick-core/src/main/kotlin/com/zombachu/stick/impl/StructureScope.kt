package com.zombachu.stick.impl

import com.zombachu.stick.Environment
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.structure.id

typealias StructureElement<E, S, T> = StructureScope<E, S>.() -> T

class StructureScope<E : Environment, S>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureScope<*, *>?,
    internal val requirement: Requirement<E, S>,
): BuilderScope<E, S> {

    private val root: StructureScope<*, *> = parent?.root ?: this

    internal fun <E2 : Environment, S2> forSender(): StructureScope<E2, S2> {
        return StructureScope(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // They must have already passed the previous requirement so should be safe to set to true
            requirement = Requirement { SenderValidationResult.success() }
        )
    }

    internal fun build(signature: Signature<E, S>): StructureImpl<E, S> {
        return StructureImpl(
            id(this.name),
            this.aliases,
            this.description,
            this.requirement,
            signature
        )
    }

    companion object {
        fun <E : Environment, S> empty(): StructureScope<E, S> = StructureScope(
            name = "",
            aliases = setOf(),
            description = "",
            parent = null,
            requirement = Requirement { SenderValidationResult.success() }
        )
    }
}
