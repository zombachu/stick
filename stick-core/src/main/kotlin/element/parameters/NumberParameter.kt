package com.zombachu.stick.element.parameters

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter

open class NumberParameter<S : SenderContext, O, T>(
    id: TypedIdentifier<T>,
    description: String,
    val toOrNull: String.() -> T?,
    val min: T,
    val max: T,
    val errorType: String,
) : Parameter.Size1<S, O, T>(id, description) where T : Number, T : Comparable<T> {

    context(senderContext: S, executionContext: ExecutionContext<S, O>)
    override fun parse(arg0: String): Result<out T> {
        val number = arg0.toOrNull() ?: return ParsingResult.failType(errorType, arg0)

        // If the given number is not in the valid range then give the sender an error
        if (number !in min..max) {
            return ParsingResult.failRange(min.toString(), max.toString(), arg0)
        }

        return ParsingResult.success(number)
    }
}

open class ByteParameter<S : SenderContext, O>(
    id: TypedIdentifier<Byte>,
    description: String,
    min: Byte,
    max: Byte,
) : NumberParameter<S, O, Byte>(id, description, String::toByteOrNull, min, max, "byte")

open class ShortParameter<S : SenderContext, O>(
    id: TypedIdentifier<Short>,
    description: String,
    min: Short,
    max: Short,
) : NumberParameter<S, O, Short>(id, description, String::toShortOrNull, min, max, "short")

open class IntParameter<S : SenderContext, O>(
    id: TypedIdentifier<Int>,
    description: String,
    min: Int,
    max: Int,
) : NumberParameter<S, O, Int>(id, description, String::toIntOrNull, min, max, "integer")

open class LongParameter<S : SenderContext, O>(
    id: TypedIdentifier<Long>,
    description: String,
    min: Long,
    max: Long,
) : NumberParameter<S, O, Long>(id, description, String::toLongOrNull, min, max, "long")

open class FloatParameter<S : SenderContext, O>(
    id: TypedIdentifier<Float>,
    description: String,
    min: Float,
    max: Float,
) : NumberParameter<S, O, Float>(id, description, String::toFloatOrNull, min, max, "float")

open class DoubleParameter<S : SenderContext, O>(
    id: TypedIdentifier<Double>,
    description: String,
    min: Double,
    max: Double,
) : NumberParameter<S, O, Double>(id, description, String::toDoubleOrNull, min, max, "double")
