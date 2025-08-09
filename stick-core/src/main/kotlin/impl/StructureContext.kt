package com.zombachu.stick.impl

import com.zombachu.stick.id

typealias StructureElement<S, T> = StructureContext<S>.() -> T

class StructureContext<S>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureContext<*>?,
    internal val requirement: Requirement<S>,
): SenderScope<S> {

    val root: StructureContext<*> = parent?.root ?: this

    fun <S2> forSender(): StructureContext<S2> {
        return StructureContext(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // To get to this point they must have already passed the previous requirement so should be safe to set to true
            requirement = { true }
        )
    }

    internal fun build(signature: Signature<S>): StructureImpl<S> {
        return StructureImpl(
            id(this.name),
            this.aliases,
            this.description,
            { this.requirement(it) },
            signature
        )
    }

    companion object {
        fun <S> empty(): StructureContext<S> = StructureContext(
            name = "",
            aliases = setOf(),
            description = "",
            parent = null,
            requirement = { true }
        )
    }
}
