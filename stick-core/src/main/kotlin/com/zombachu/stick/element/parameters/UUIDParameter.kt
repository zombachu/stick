package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.failType
import com.zombachu.stick.success
import java.util.*

open class UUIDParameter<E : Environment, S>(name: String, description: String) :
    Parameter.Size1<E, S, UUID>(name, description) {

    context(validationContext: ValidationContext<E, S>)
    override fun resolve(arg0: String): CommandResult<UUID> {
        try {
            val uuid = UUID.fromString(arg0)
            return success(uuid)
        } catch (_: IllegalArgumentException) {
            return failType("UUID", arg0)
        }
    }
}
