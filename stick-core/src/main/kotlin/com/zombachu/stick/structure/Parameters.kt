package com.zombachu.stick.structure

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
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
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.element.parameters.UUIDParameter
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.lowercase
import kotlin.enums.enumEntries
import kotlin.reflect.KClass

fun <E : Environment, S> BuilderScope<E, S>.booleanParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, BooleanParameter<E, S>> = {
    BooleanParameter(name, description)
}

fun <E : Environment, S> BuilderScope<E, S>.byteParameter(
    name: String,
    min: Byte = Byte.MIN_VALUE,
    max: Byte = Byte.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ByteParameter<E, S>> = {
    ByteParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.shortParameter(
    name: String,
    min: Short = Short.MIN_VALUE,
    max: Short = Short.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, ShortParameter<E, S>> = {
    ShortParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.intParameter(
    name: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, IntParameter<E, S>> = {
    IntParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.longParameter(
    name: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, LongParameter<E, S>> = {
    LongParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.floatParameter(
    name: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, FloatParameter<E, S>> = {
    FloatParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.doubleParameter(
    name: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    description: String = "",
): StructureElement<E, S, DoubleParameter<E, S>> = {
    DoubleParameter(name, description, min, max)
}

fun <E : Environment, S> BuilderScope<E, S>.literalParameter(
    name: String,
    aliases: Set<String> = setOf(),
    description: String = "",
): StructureElement<E, S, LiteralParameter<E, S>> = {
    LiteralParameter(name, aliases.lowercase(), description)
}

fun <E : Environment, S> BuilderScope<E, S>.stringParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, StringParameter<E, S>> = {
    StringParameter(name, description)
}

fun <E : Environment, S> BuilderScope<E, S>.textParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, TextParameter<E, S>> = {
    TextParameter(name, description)
}

inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> = {
    EnumParameter(
        name,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        mapOf(),
    )
}

@JvmName("aliasableEnumParameter")
inline fun <E : Environment, S, reified T> BuilderScope<E, S>.enumParameter(
    name: String,
    enum: KClass<T>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> where T : Enum<T>, T : Aliasable = {
    EnumParameter(
        name,
        description,
        enumEntries<T>().associateBy { it.name.lowercase() },
        enumEntries<T>()
            .flatMap { e -> e.aliases.lowercase()
                .map { n -> Pair(n, e) } }
            .toMap()
    )
}

inline fun <E : Environment, S, reified T : Enum<T>> BuilderScope<E, S>.enumParameter(
    name: String,
    entries: List<EnumEntry<T>>,
    description: String = "",
): StructureElement<E, S, EnumParameter<E, S, T>> = {
    EnumParameter(
        name,
        description,
        entries.associate { Pair(it.label.lowercase(), it.enumValue) },
        entries
            .flatMap { e -> e.aliases.lowercase()
                .map { n -> Pair(n, e.enumValue) } }
            .toMap(),
    )
}

fun <E : Environment, S> BuilderScope<E, S>.uuidParameter(
    name: String,
    description: String = "",
): StructureElement<E, S, UUIDParameter<E, S>> = {
    UUIDParameter(name, description)
}

fun <E : Environment, S, T> BuilderScope<E, S>.listParameter(
    name: String,
    parameter: StructureElement<E, S, Parameter.Size1<E, S, T>>,
    description: String = "",
): StructureElement<E, S, ListParameter<E, S, T>> = {
    ListParameter(name, description, parameter(this))
}

fun <E : Environment, S, T> BuilderScope<E, S>.listElementParameter(
    name: String,
    list: ContextualValue<E, S, List<T>>,
    onEmpty: Invocation<E, S>.() -> Unit,
    description: String = "",
): StructureElement<E, S, ListElementParameter<E, S, T>> = {
    ListElementParameter(name, description, list, onEmpty)
}
