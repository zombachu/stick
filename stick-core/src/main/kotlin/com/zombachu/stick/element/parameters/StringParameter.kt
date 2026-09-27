package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.success

open class StringParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Size1<E, S, String>(name, description) {

    override val type: GroupableType = GroupableType.Passthrough

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<String> {
        return success(arg0)
    }
}
