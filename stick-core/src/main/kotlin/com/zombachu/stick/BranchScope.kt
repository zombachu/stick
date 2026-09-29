package com.zombachu.stick

import com.zombachu.stick.element.Branch
import com.zombachu.stick.element.BranchImpl
import com.zombachu.stick.element.GatedBranch
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Signature

class BranchScope<E : Environment, S, A>
internal constructor(
    internal val leadingParameter: Parameter<E, S, A, Position.Leading>,
    private val requirement: Requirement<E, S>?,
) {

    internal fun <T_ : Arguments> build(signature: Signature<E, S, T_>): Branch<E, S, T_> {
        val branch = BranchImpl(signature)
        return if (requirement == null) branch else GatedBranch(branch, requirement)
    }
}
