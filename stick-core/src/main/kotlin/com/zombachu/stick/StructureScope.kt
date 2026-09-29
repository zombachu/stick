package com.zombachu.stick

import com.zombachu.stick.element.GatedStructure
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.StructureImpl

class StructureScope<E : Environment, S>(
    val name: String,
    val aliases: Set<String>,
    val description: String,
    val parent: StructureScope<*, *>?,
    internal val requirement: Requirement<E, S>?,
) : BuilderScope<E, S> {

    private val root: StructureScope<*, *> = parent?.root ?: this

    internal fun <E2 : Environment, S2> forSender(): StructureScope<E2, S2> {
        return StructureScope(
            this.name,
            this.aliases,
            this.description,
            this.parent,
            // They must have already passed the previous requirement so should be safe to drop
            requirement = null,
        )
    }

    internal fun <T_ : Arguments> build(
        signature: (Parameter<E, S, *, *>) -> Signature<E, S, T_>
    ): Structure<E, S, T_> {
        val structure = StructureImpl(this.name, this.aliases, this.description, signature)
        return if (requirement == null) structure else GatedStructure(structure, requirement)
    }

    companion object {
        fun <E : Environment, S> empty(): StructureScope<E, S> =
            StructureScope(
                name = "",
                aliases = [],
                description = "",
                parent = null,
                requirement = null,
            )
    }
}
