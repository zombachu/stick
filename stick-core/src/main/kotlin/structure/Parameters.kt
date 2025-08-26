package com.zombachu.stick.structure

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Environment
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

fun <S : Environment, O> SenderScope<S, O>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<S, O, BooleanParameter<S, O>> = {
    BooleanParameter(id, description)
}
fun <S : Environment, O> SenderScope<S, O>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<S, O, BooleanParameter<S, O>> = booleanParameter(id(name), description)

fun <S : Environment, O> SenderScope<S, O>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, ByteParameter<S, O>> = {
    ByteParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, ByteParameter<S, O>> = byteParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, ShortParameter<S, O>> = {
    ShortParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, ShortParameter<S, O>> = shortParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, IntParameter<S, O>> = {
    IntParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, IntParameter<S, O>> = intParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, LongParameter<S, O>> = {
    LongParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, LongParameter<S, O>> = longParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, FloatParameter<S, O>> = {
    FloatParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, FloatParameter<S, O>> = floatParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, DoubleParameter<S, O>> = {
    DoubleParameter(id, description, min, max)
}
fun <S : Environment, O> SenderScope<S, O>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<S, O, DoubleParameter<S, O>> = doubleParameter(id(name), min, max, description)

fun <S : Environment, O> SenderScope<S, O>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, LiteralParameter<S, O>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}
fun <S : Environment, O> SenderScope<S, O>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<S, O, LiteralParameter<S, O>> = literalParameter(name, aliases, description)

fun <S : Environment, O> SenderScope<S, O>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, O, StringParameter<S, O>> = {
    StringParameter(id, description)
}
fun <S : Environment, O> SenderScope<S, O>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<S, O, StringParameter<S, O>> = stringParameter(id(name), description)

fun <S : Environment, O> SenderScope<S, O>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<S, O, UnboundedStringParameter<S, O>> = {
    UnboundedStringParameter(id, description)
}
fun <S : Environment, O> SenderScope<S, O>.unboundedStringParameter(
    name: String,
    description: String = "",
): StructureElement<S, O, UnboundedStringParameter<S, O>> = unboundedStringParameter(id(name), description)

@JvmName("enumParameter")
inline fun <S : Environment, O, reified T : Enum<T>> SenderScope<S, O>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, O, EnumParameter<S, O, T>> = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}
@JvmName("enumParameterNamed")
inline fun <S : Environment, O, reified T : Enum<T>> SenderScope<S, O>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, O, EnumParameter<S, O, T>> = enumParameter(id(name), enum, description)

@JvmName("aliasableEnumParameter")
inline fun <S : Environment, O, reified T> SenderScope<S, O>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, O, EnumParameter<S, O, T>> where T : Enum<T>, T : Aliasable = {
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
inline fun <S : Environment, O, reified T> SenderScope<S, O>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<S, O, EnumParameter<S, O, T>> where T : Enum<T>, T : Aliasable =
    enumParameter(id(name), enum, description)

fun <S : Environment, O> SenderScope<S, O>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<S, O, UUIDParameter<S, O>> = {
    UUIDParameter(id, description)
}
fun <S : Environment, O> SenderScope<S, O>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<S, O, UUIDParameter<S, O>> = uuidParameter(id(name), description)

fun <S : Environment, O, T : Any> SenderScope<S, O>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<S, O, Parameter.Size1<S, O, T>>,
    description: String = "",
): StructureElement<S, O, ListParameter<S, O, T>> = {
    ListParameter(id, description, parameter(this))
}
fun <S : Environment, O, T : Any> SenderScope<S, O>.listParameter(
    name: String,
    parameter: StructureElement<S, O, Parameter.Size1<S, O, T>>,
    description: String = "",
): StructureElement<S, O, ListParameter<S, O, T>> = listParameter(id(name), parameter, description)

fun <S : Environment, O, T : Any> SenderScope<S, O>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<S, O, List<T>>,
    onEmpty: Invocation<S, O>.() -> ExecutionResult,
    description: String = "",
): StructureElement<S, O, ListElementParameter<S, O, T>> = {
    ListElementParameter(id, description, list, onEmpty)
}
inline fun <S : Environment, O, reified T : Any> SenderScope<S, O>.listElementParameter(
    name: String,
    noinline list: ContextualValue<S, O, List<T>>,
    noinline onEmpty: Invocation<S, O>.() -> ExecutionResult,
    description: String = "",
): StructureElement<S, O, ListElementParameter<S, O, T>> = listElementParameter(id(name), list, onEmpty, description)
