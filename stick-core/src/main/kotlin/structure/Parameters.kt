package com.zombachu.stick.structure

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.parameters.BooleanParameter
import com.zombachu.stick.element.parameters.ByteParameter
import com.zombachu.stick.element.parameters.DoubleParameter
import com.zombachu.stick.element.parameters.EnumParameter
import com.zombachu.stick.element.parameters.FloatParameter
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.ListElementParameter
import com.zombachu.stick.element.parameters.ListParameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.LongParameter
import com.zombachu.stick.element.parameters.ShortParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.UUIDParameter
import com.zombachu.stick.element.parameters.UnboundedStringParameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase
import java.util.*
import kotlin.enums.enumEntries
import kotlin.reflect.KClass

fun <S : SenderContext> SenderScope<S>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<S, BooleanParameter<S>> = {
    BooleanParameter(id, description)
}
fun <S : SenderContext> SenderScope<S>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<S, BooleanParameter<S>> = booleanParameter(id(name), description)

fun <S : SenderContext> SenderScope<S>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<S, ByteParameter<S>> = {
    ByteParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<S, ByteParameter<S>> = byteParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<S, ShortParameter<S>> = {
    ShortParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<S, ShortParameter<S>> = shortParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<S, IntParameter<S>> = {
    IntParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<S, IntParameter<S>> = intParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<S, LongParameter<S>> = {
    LongParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<S, LongParameter<S>> = longParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<S, FloatParameter<S>> = {
    FloatParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<S, FloatParameter<S>> = floatParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<S, DoubleParameter<S>> = {
    DoubleParameter(id, description, min, max)
}
fun <S : SenderContext> SenderScope<S>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<S, DoubleParameter<S>> = doubleParameter(id(name), min, max, description)

fun <S : SenderContext> SenderScope<S>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, LiteralParameter<S>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}
fun <S : SenderContext> SenderScope<S>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, LiteralParameter<S>> = literalParameter(name, aliases, description)

fun <S : SenderContext> SenderScope<S>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, StringParameter<S>> = {
    StringParameter(id, description)
}
fun <S : SenderContext> SenderScope<S>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<S, StringParameter<S>> = stringParameter(id(name), description)

fun <S : SenderContext> SenderScope<S>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, UnboundedStringParameter<S>> = {
    UnboundedStringParameter(id, description)
}
fun <S : SenderContext> SenderScope<S>.unboundedStringParameter(
    name: String,
    description: String = "",
): StructureElement<S, UnboundedStringParameter<S>> = unboundedStringParameter(id(name), description)

@JvmName("enumParameter")
inline fun <S : SenderContext, reified T : Enum<T>> SenderScope<S>.enumParameter(
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
@JvmName("enumParameterNamed")
inline fun <S : SenderContext, reified T : Enum<T>> SenderScope<S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, EnumParameter<S, T>> = enumParameter(id(name), enum, description)

@JvmName("aliasableEnumParameter")
inline fun <S : SenderContext, reified T> SenderScope<S>.enumParameter(
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
@JvmName("aliasableEnumParameterNamed")
inline fun <S : SenderContext, reified T> SenderScope<S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, EnumParameter<S, T>> where T : Enum<T>, T : Aliasable =
    enumParameter(id(name), enum, description)

fun <S : SenderContext> SenderScope<S>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<S, UUIDParameter<S>> = {
    UUIDParameter(id, description)
}
fun <S : SenderContext> SenderScope<S>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<S, UUIDParameter<S>> = uuidParameter(id(name), description)

fun <S : SenderContext, T : Any> SenderScope<S>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<S, Parameter.Size1<S, T>>,
    description: String = "",
): StructureElement<S, ListParameter<S, T>> = {
    ListParameter(id, description, parameter(this))
}
fun <S : SenderContext, T : Any> SenderScope<S>.listParameter(
    name: String,
    parameter: StructureElement<S, Parameter.Size1<S, T>>,
    description: String = "",
): StructureElement<S, ListParameter<S, T>> = listParameter(id(name), parameter, description)

fun <S : SenderContext, T : Any> SenderScope<S>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<S, List<T>>,
    onEmpty: ExecutionContext<S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<S, ListElementParameter<S, T>> = {
    ListElementParameter(id, description, list, onEmpty)
}
inline fun <S : SenderContext, reified T : Any> SenderScope<S>.listElementParameter(
    name: String,
    noinline list: ContextualValue<S, List<T>>,
    noinline onEmpty: ExecutionContext<S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<S, ListElementParameter<S, T>> = listElementParameter(id(name), list, onEmpty, description)
