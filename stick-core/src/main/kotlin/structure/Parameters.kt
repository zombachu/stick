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

fun <O, S : SenderContext> SenderScope<O, S>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<O, S, BooleanParameter<O, S>> = {
    BooleanParameter(id, description)
}
fun <O, S : SenderContext> SenderScope<O, S>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<O, S, BooleanParameter<O, S>> = booleanParameter(id(name), description)

fun <O, S : SenderContext> SenderScope<O, S>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, ByteParameter<O, S>> = {
    ByteParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, ByteParameter<O, S>> = byteParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, ShortParameter<O, S>> = {
    ShortParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, ShortParameter<O, S>> = shortParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, IntParameter<O, S>> = {
    IntParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, IntParameter<O, S>> = intParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, LongParameter<O, S>> = {
    LongParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, LongParameter<O, S>> = longParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, FloatParameter<O, S>> = {
    FloatParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, FloatParameter<O, S>> = floatParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, DoubleParameter<O, S>> = {
    DoubleParameter(id, description, min, max)
}
fun <O, S : SenderContext> SenderScope<O, S>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<O, S, DoubleParameter<O, S>> = doubleParameter(id(name), min, max, description)

fun <O, S : SenderContext> SenderScope<O, S>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, LiteralParameter<O, S>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}
fun <O, S : SenderContext> SenderScope<O, S>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<O, S, LiteralParameter<O, S>> = literalParameter(name, aliases, description)

fun <O, S : SenderContext> SenderScope<O, S>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<O, S, StringParameter<O, S>> = {
    StringParameter(id, description)
}
fun <O, S : SenderContext> SenderScope<O, S>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<O, S, StringParameter<O, S>> = stringParameter(id(name), description)

fun <O, S : SenderContext> SenderScope<O, S>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<O, S, UnboundedStringParameter<O, S>> = {
    UnboundedStringParameter(id, description)
}
fun <O, S : SenderContext> SenderScope<O, S>.unboundedStringParameter(
    name: String,
    description: String = "",
): StructureElement<O, S, UnboundedStringParameter<O, S>> = unboundedStringParameter(id(name), description)

@JvmName("enumParameter")
inline fun <O, S : SenderContext, reified T : Enum<T>> SenderScope<O, S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<O, S, EnumParameter<O, S, T>> = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}
@JvmName("enumParameterNamed")
inline fun <O, S : SenderContext, reified T : Enum<T>> SenderScope<O, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<O, S, EnumParameter<O, S, T>> = enumParameter(id(name), enum, description)

@JvmName("aliasableEnumParameter")
inline fun <O, S : SenderContext, reified T> SenderScope<O, S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<O, S, EnumParameter<O, S, T>> where T : Enum<T>, T : Aliasable = {
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
inline fun <O, S : SenderContext, reified T> SenderScope<O, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<O, S, EnumParameter<O, S, T>> where T : Enum<T>, T : Aliasable =
    enumParameter(id(name), enum, description)

fun <O, S : SenderContext> SenderScope<O, S>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<O, S, UUIDParameter<O, S>> = {
    UUIDParameter(id, description)
}
fun <O, S : SenderContext> SenderScope<O, S>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<O, S, UUIDParameter<O, S>> = uuidParameter(id(name), description)

fun <O, S : SenderContext, T : Any> SenderScope<O, S>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<O, S, Parameter.Size1<O, S, T>>,
    description: String = "",
): StructureElement<O, S, ListParameter<O, S, T>> = {
    ListParameter(id, description, parameter(this))
}
fun <O, S : SenderContext, T : Any> SenderScope<O, S>.listParameter(
    name: String,
    parameter: StructureElement<O, S, Parameter.Size1<O, S, T>>,
    description: String = "",
): StructureElement<O, S, ListParameter<O, S, T>> = listParameter(id(name), parameter, description)

fun <O, S : SenderContext, T : Any> SenderScope<O, S>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<O, S, List<T>>,
    onEmpty: ExecutionContext<O, S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<O, S, ListElementParameter<O, S, T>> = {
    ListElementParameter(id, description, list, onEmpty)
}
inline fun <O, S : SenderContext, reified T : Any> SenderScope<O, S>.listElementParameter(
    name: String,
    noinline list: ContextualValue<O, S, List<T>>,
    noinline onEmpty: ExecutionContext<O, S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<O, S, ListElementParameter<O, S, T>> = listElementParameter(id(name), list, onEmpty, description)
