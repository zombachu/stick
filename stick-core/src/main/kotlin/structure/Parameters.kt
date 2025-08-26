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

fun <E : Environment, O> SenderScope<E, O>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<E, O, BooleanParameter<E, O>> = {
    BooleanParameter(id, description)
}
fun <E : Environment, O> SenderScope<E, O>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<E, O, BooleanParameter<E, O>> = booleanParameter(id(name), description)

fun <E : Environment, O> SenderScope<E, O>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, ByteParameter<E, O>> = {
    ByteParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, ByteParameter<E, O>> = byteParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, ShortParameter<E, O>> = {
    ShortParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, ShortParameter<E, O>> = shortParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, IntParameter<E, O>> = {
    IntParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, IntParameter<E, O>> = intParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, LongParameter<E, O>> = {
    LongParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, LongParameter<E, O>> = longParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, FloatParameter<E, O>> = {
    FloatParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, FloatParameter<E, O>> = floatParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, DoubleParameter<E, O>> = {
    DoubleParameter(id, description, min, max)
}
fun <E : Environment, O> SenderScope<E, O>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<E, O, DoubleParameter<E, O>> = doubleParameter(id(name), min, max, description)

fun <E : Environment, O> SenderScope<E, O>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, LiteralParameter<E, O>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}
fun <E : Environment, O> SenderScope<E, O>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, O, LiteralParameter<E, O>> = literalParameter(name, aliases, description)

fun <E : Environment, O> SenderScope<E, O>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, O, StringParameter<E, O>> = {
    StringParameter(id, description)
}
fun <E : Environment, O> SenderScope<E, O>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<E, O, StringParameter<E, O>> = stringParameter(id(name), description)

fun <E : Environment, O> SenderScope<E, O>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, O, UnboundedStringParameter<E, O>> = {
    UnboundedStringParameter(id, description)
}
fun <E : Environment, O> SenderScope<E, O>.unboundedStringParameter(
    name: String,
    description: String = "",
): StructureElement<E, O, UnboundedStringParameter<E, O>> = unboundedStringParameter(id(name), description)

@JvmName("enumParameter")
inline fun <E : Environment, O, reified T : Enum<T>> SenderScope<E, O>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, O, EnumParameter<E, O, T>> = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}
@JvmName("enumParameterNamed")
inline fun <E : Environment, O, reified T : Enum<T>> SenderScope<E, O>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, O, EnumParameter<E, O, T>> = enumParameter(id(name), enum, description)

@JvmName("aliasableEnumParameter")
inline fun <E : Environment, O, reified T> SenderScope<E, O>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, O, EnumParameter<E, O, T>> where T : Enum<T>, T : Aliasable = {
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
inline fun <E : Environment, O, reified T> SenderScope<E, O>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, O, EnumParameter<E, O, T>> where T : Enum<T>, T : Aliasable =
    enumParameter(id(name), enum, description)

fun <E : Environment, O> SenderScope<E, O>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<E, O, UUIDParameter<E, O>> = {
    UUIDParameter(id, description)
}
fun <E : Environment, O> SenderScope<E, O>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<E, O, UUIDParameter<E, O>> = uuidParameter(id(name), description)

fun <E : Environment, O, T : Any> SenderScope<E, O>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<E, O, Parameter.Size1<E, O, T>>,
    description: String = "",
): StructureElement<E, O, ListParameter<E, O, T>> = {
    ListParameter(id, description, parameter(this))
}
fun <E : Environment, O, T : Any> SenderScope<E, O>.listParameter(
    name: String,
    parameter: StructureElement<E, O, Parameter.Size1<E, O, T>>,
    description: String = "",
): StructureElement<E, O, ListParameter<E, O, T>> = listParameter(id(name), parameter, description)

fun <E : Environment, O, T : Any> SenderScope<E, O>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<E, O, List<T>>,
    onEmpty: Invocation<E, O>.() -> ExecutionResult,
    description: String = "",
): StructureElement<E, O, ListElementParameter<E, O, T>> = {
    ListElementParameter(id, description, list, onEmpty)
}
inline fun <E : Environment, O, reified T : Any> SenderScope<E, O>.listElementParameter(
    name: String,
    noinline list: ContextualValue<E, O, List<T>>,
    noinline onEmpty: Invocation<E, O>.() -> ExecutionResult,
    description: String = "",
): StructureElement<E, O, ListElementParameter<E, O, T>> = listElementParameter(id(name), list, onEmpty, description)
