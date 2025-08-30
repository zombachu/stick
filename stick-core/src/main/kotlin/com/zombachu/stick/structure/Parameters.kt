package com.zombachu.stick.structure

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.parameters.BooleanParameter
import com.zombachu.stick.element.parameters.ByteParameter
import com.zombachu.stick.element.parameters.DoubleParameter
import com.zombachu.stick.element.parameters.EnumEntry
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
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase
import java.util.*
import kotlin.enums.enumEntries
import kotlin.reflect.KClass

fun <E : Environment, S> BuilderScope<E, S>.booleanParameter(
    id: TypedIdentifier<Boolean>,
    description: String = "",
): StructureElement<E, S, BooleanParameter<E, S>> = {
    BooleanParameter(id, description)
}
fun <E : Environment, S> BuilderScope<E, S>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, BooleanParameter<E, S>> = booleanParameter(id(name), description)

fun <E : Environment, S> BuilderScope<E, S>.byteParameter(
    id: TypedIdentifier<Byte>,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ByteParameter<E, S>> = {
    ByteParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ByteParameter<E, S>> = byteParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.shortParameter(
    id: TypedIdentifier<Short>,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ShortParameter<E, S>> = {
    ShortParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ShortParameter<E, S>> = shortParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.intParameter(
    id: TypedIdentifier<Int>,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, IntParameter<E, S>> = {
    IntParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, IntParameter<E, S>> = intParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.longParameter(
    id: TypedIdentifier<Long>,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, LongParameter<E, S>> = {
    LongParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, LongParameter<E, S>> = longParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.floatParameter(
    id: TypedIdentifier<Float>,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, FloatParameter<E, S>> = {
    FloatParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, FloatParameter<E, S>> = floatParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.doubleParameter(
    id: TypedIdentifier<Double>,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, DoubleParameter<E, S>> = {
    DoubleParameter(id, description, min, max)
}
fun <E : Environment, S> BuilderScope<E, S>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, DoubleParameter<E, S>> = doubleParameter(id(name), min, max, description)

fun <E : Environment, S> BuilderScope<E, S>.literalParameter(
    id: TypedIdentifier<String>,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, LiteralParameter<E, S>> = {
    LiteralParameter(id, aliases.lowercase(), description)
}
fun <E : Environment, S> BuilderScope<E, S>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, LiteralParameter<E, S>> = literalParameter(name, aliases, description)

fun <E : Environment, S> BuilderScope<E, S>.stringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, S, StringParameter<E, S>> = {
    StringParameter(id, description)
}
fun <E : Environment, S> BuilderScope<E, S>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, StringParameter<E, S>> = stringParameter(id(name), description)

fun <E : Environment, S> BuilderScope<E, S>.unboundedStringParameter(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElement<E, S, UnboundedStringParameter<E, S>> = {
    UnboundedStringParameter(id, description)
}
fun <E : Environment, S> BuilderScope<E, S>.unboundedStringParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, UnboundedStringParameter<E, S>> = unboundedStringParameter(id(name), description)

@JvmName("enumParameter")
inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> = {
    EnumParameter(
        id,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}
@JvmName("enumParameterNamed")
inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> = enumParameter(id(name), enum, description)

@JvmName("aliasableEnumParameter")
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.enumParameter(
    id: TypedIdentifier<T>,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> where T : Enum<T>, T : Aliasable = {
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
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> where T : Enum<T>, T : Aliasable =
    enumParameter(id(name), enum, description)


@JvmName("customEnumParameter")
inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    id: TypedIdentifier<T>,
    entries: List<EnumEntry<T>>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> = {
    EnumParameter(
        id,
        description,
        entries.associate { Pair(it.label.lowercase(), it.enumValue) },
        entries
            .flatMap { e -> e.aliases.lowercase()
                .map { n -> Pair(n, e.enumValue) } }
            .toMap(),
    )
}
@JvmName("customEnumParameterNamed")
inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    name: String,
    entries: List<EnumEntry<T>>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>>  =
    enumParameter(id(name), entries, description)

fun <E : Environment, S> BuilderScope<E, S>.uuidParameter(
    id: TypedIdentifier<UUID>,
    description: String = "",
): StructureElement<E, S, UUIDParameter<E, S>> = {
    UUIDParameter(id, description)
}
fun <E : Environment, S> BuilderScope<E, S>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, UUIDParameter<E, S>> = uuidParameter(id(name), description)

fun <E : Environment, S, T : Any> BuilderScope<E, S>.listParameter(
    id: TypedIdentifier<List<T>>,
    parameter: StructureElement<E, S, Parameter.Size1<E, S, T>>,
    description: String = "",
): StructureElement<E, S, ListParameter<E, S, T>> = {
    ListParameter(id, description, parameter(this))
}
fun <E : Environment, S, T : Any> BuilderScope<E, S>.listParameter(
    name: String,
    parameter: StructureElement<E, S, Parameter.Size1<E, S, T>>,
    description: String = "",
): StructureElement<E, S, ListParameter<E, S, T>> = listParameter(id(name), parameter, description)

fun <E : Environment, S, T : Any> BuilderScope<E, S>.listElementParameter(
    id: TypedIdentifier<T>,
    list: ContextualValue<E, S, List<T>>,
    onEmpty: Invocation<E, S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<E, S, ListElementParameter<E, S, T>> = {
    ListElementParameter(id, description, list, onEmpty)
}
inline fun <E : Environment, S, reified T : Any> BuilderScope<E, S>.listElementParameter(
    name: String,
    noinline list: ContextualValue<E, S, List<T>>,
    noinline onEmpty: Invocation<E, S>.() -> ExecutionResult,
    description: String = "",
): StructureElement<E, S, ListElementParameter<E, S, T>> = listElementParameter(id(name), list, onEmpty, description)
