package com.zombachu.stick.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.impl.Parameter

open class NumberParameter<S, T>(
    id: TypedIdentifier<T>,
    description: String,
    val toOrNull: String.() -> T?,
    val min: T,
    val max: T,
) : Parameter.Size1<S, T>(id, description) where T : Number, T : Comparable<T> {

    override fun parse(context: ExecutionContext<S>, arg0: String): Result<T> {
        val number = arg0.toOrNull() ?: return ParsingResult.failType()

        // If the given number is not in the valid range then give the sender an error
        if (number !in min..max) {
            return ParsingResult.failArgument()
        }

        return ParsingResult.success(number)
    }
}

open class ByteParameter<S>(
    id: TypedIdentifier<Byte>,
    description: String,
    min: Byte,
    max: Byte,
) : NumberParameter<S, Byte>(id, description, String::toByteOrNull, min, max)

open class ShortParameter<S>(
    id: TypedIdentifier<Short>,
    description: String,
    min: Short,
    max: Short,
) : NumberParameter<S, Short>(id, description, String::toShortOrNull, min, max)

open class IntParameter<S>(
    id: TypedIdentifier<Int>,
    description: String,
    min: Int,
    max: Int,
) : NumberParameter<S, Int>(id, description, String::toIntOrNull, min, max)

open class LongParameter<S>(
    id: TypedIdentifier<Long>,
    description: String,
    min: Long,
    max: Long,
) : NumberParameter<S, Long>(id, description, String::toLongOrNull, min, max)

open class FloatParameter<S>(
    id: TypedIdentifier<Float>,
    description: String,
    min: Float,
    max: Float,
) : NumberParameter<S, Float>(id, description, String::toFloatOrNull, min, max)

open class DoubleParameter<S>(
    id: TypedIdentifier<Double>,
    description: String,
    min: Double,
    max: Double,
) : NumberParameter<S, Double>(id, description, String::toDoubleOrNull, min, max)
