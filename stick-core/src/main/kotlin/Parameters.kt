package com.zombachu.stick

import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.parameters.BooleanParameter
import com.zombachu.stick.parameters.ByteParameter
import com.zombachu.stick.parameters.DoubleParameter
import com.zombachu.stick.parameters.EnumParameter
import com.zombachu.stick.parameters.FloatParameter
import com.zombachu.stick.parameters.IntParameter
import com.zombachu.stick.parameters.ListElementParameter
import com.zombachu.stick.parameters.ListParameter
import com.zombachu.stick.parameters.LiteralParameter
import com.zombachu.stick.parameters.LongParameter
import com.zombachu.stick.parameters.ShortParameter
import com.zombachu.stick.parameters.StringParameter
import com.zombachu.stick.parameters.UUIDParameter
import com.zombachu.stick.parameters.UnboundedStringParameter
import java.util.*
import kotlin.enums.enumEntries
import kotlin.reflect.KClass

fun <S> SenderScope<S>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<S, BooleanParameter<S>> = {
    BooleanParameter(id, description)
}

fun <S> SenderScope<S>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<S, ByteParameter<S>> = {
    ByteParameter(id, description, min, max)
}

fun <S> SenderScope<S>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<S, ShortParameter<S>> = {
    ShortParameter(id, description, min, max)
}

fun <S> SenderScope<S>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<S, IntParameter<S>> = {
    IntParameter(id, description, min, max)
}

fun <S> SenderScope<S>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<S, LongParameter<S>> = {
    LongParameter(id, description, min, max)
}

fun <S> SenderScope<S>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<S, FloatParameter<S>> = {
    FloatParameter(id, description, min, max)
}

fun <S> SenderScope<S>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<S, DoubleParameter<S>> = {
    DoubleParameter(id, description, min, max)
}

fun <S> SenderScope<S>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, LiteralParameter<S>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}

fun <S> SenderScope<S>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, StringParameter<S>> = {
    StringParameter(id, description)
}

fun <S> SenderScope<S>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, UnboundedStringParameter<S>> = {
    UnboundedStringParameter(id, description)
}

@JvmName("enumParameter")
inline fun <S, reified T : Enum<T>> SenderScope<S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, EnumParameter<S, T>> = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}

@JvmName("aliasableEnumParameter")
inline fun <S, reified T> SenderScope<S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, EnumParameter<S, T>> where T : Enum<T>, T : Aliasable = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        enumEntries<T>()
            .flatMap { e -> e.aliases.lowercase()
                .map { n -> Pair(n, e) } }
            .toMap()
    )
}

fun <S> SenderScope<S>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<S, UUIDParameter<S>> = {
    UUIDParameter(id, description)
}

fun <S, T : Any> SenderScope<S>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<S, Parameter.Size1<S, T>>,
    description: String = "",
): StructureElement<S, ListParameter<S, T>> = {
    ListParameter(id, description, parameter(this))
}

fun <S, T : Any> SenderScope<S>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<S, List<T>>,
    description: String = "",
): StructureElement<S, ListElementParameter<S, T>> = {
    ListElementParameter(id, description, list)
}
