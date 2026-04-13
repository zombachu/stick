package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.Parameter

open class NumberParameter<E : Environment, S, T>(
    name: String,
    description: String,
    val toOrNull: String.() -> T?,
    val min: T,
    val max: T,
    val errorType: String,
) : Parameter.Size1<E, S, T>(name, description) where T : Number, T : Comparable<T> {

    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<T> {
        val number = arg0.toOrNull() ?: return ParsingResult.failType(errorType, arg0)

        // If the given number is not in the valid range then give the sender an error
        if (number !in min..max) {
            return ParsingResult.failRange(min.toString(), max.toString(), arg0)
        }

        return ParsingResult.success(number)
    }
}

open class ByteParameter<E : Environment, S>(name: String, description: String, min: Byte, max: Byte) :
    NumberParameter<E, S, Byte>(name, description, String::toByteOrNull, min, max, "byte")

open class ShortParameter<E : Environment, S>(name: String, description: String, min: Short, max: Short) :
    NumberParameter<E, S, Short>(name, description, String::toShortOrNull, min, max, "short")

open class IntParameter<E : Environment, S>(name: String, description: String, min: Int, max: Int) :
    NumberParameter<E, S, Int>(name, description, String::toIntOrNull, min, max, "integer")

open class LongParameter<E : Environment, S>(name: String, description: String, min: Long, max: Long) :
    NumberParameter<E, S, Long>(name, description, String::toLongOrNull, min, max, "long")

open class FloatParameter<E : Environment, S>(name: String, description: String, min: Float, max: Float) :
    NumberParameter<E, S, Float>(name, description, String::toFloatOrNull, min, max, "float")

open class DoubleParameter<E : Environment, S>(name: String, description: String, min: Double, max: Double) :
    NumberParameter<E, S, Double>(name, description, String::toDoubleOrNull, min, max, "double")
