package com.zombachu.stick.element.parameters

import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Size
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.success

open class TextParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Unbounded<E, S, String>(Size.atLeast(1), name, description) {

    override val type: GroupableType = GroupableType.Passthrough

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(args: List<String>): ConsumingResult<String> {
        return success(args.joinToString(" ")).consuming(args.size)
    }
}
