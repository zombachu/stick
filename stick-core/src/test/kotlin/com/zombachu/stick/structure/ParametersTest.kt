package com.zombachu.stick.structure

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.EnumEntry
import com.zombachu.stick.structureTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ParametersTest {

    @Test
    fun `integer parameters default to type bounds`() = structureTest {
        assertEquals(Byte.MIN_VALUE, byteParameter("").min)
        assertEquals(Byte.MAX_VALUE, byteParameter("").max)
        assertEquals(Short.MIN_VALUE, shortParameter("").min)
        assertEquals(Short.MAX_VALUE, shortParameter("").max)
        assertEquals(Int.MIN_VALUE, intParameter("").min)
        assertEquals(Int.MAX_VALUE, intParameter("").max)
        assertEquals(Long.MIN_VALUE, longParameter("").min)
        assertEquals(Long.MAX_VALUE, longParameter("").max)
    }

    @Test
    fun `float and double default to type bounds`() = structureTest {
        assertEquals(-Float.MAX_VALUE, floatParameter("").min)
        assertEquals(Float.MAX_VALUE, floatParameter("").max)
        assertEquals(-Double.MAX_VALUE, doubleParameter("").min)
        assertEquals(Double.MAX_VALUE, doubleParameter("").max)
    }

    @Test
    fun `literalParameter lowercases aliases`() = structureTest {
        assertEquals(["bar", "baz"], literalParameter("Foo", ["BAR", "Baz"]).aliases)
    }

    @Test
    fun `enumParameter by KClass lowercases enum names`() = structureTest {
        val parameter = enumParameter("", Color::class)
        assertEquals(mapOf("red" to Color.RED, "green" to Color.GREEN), parameter.primaryValues)
        assertEquals(emptyMap(), parameter.aliasedValues)
    }

    @Test
    fun `enumParameter derives aliases from Aliasable enums`() = structureTest {
        val parameter = enumParameter("", AliasedColor::class)
        assertEquals(mapOf("red" to AliasedColor.RED, "green" to AliasedColor.GREEN), parameter.primaryValues)
        assertEquals(mapOf("r" to AliasedColor.RED, "g" to AliasedColor.GREEN), parameter.aliasedValues)
    }

    @Test
    fun `enumParameter from EnumEntry list derives values`() = structureTest {
        val entries = [EnumEntry(Color.RED, "red", ["r"]), EnumEntry(Color.GREEN, "green")]
        val parameter = enumParameter("", entries)
        assertEquals(mapOf("red" to Color.RED, "green" to Color.GREEN), parameter.primaryValues)
        assertEquals(mapOf("r" to Color.RED), parameter.aliasedValues)
    }

    @Test
    fun `parameters set names`() = structureTest {
        assertEquals("s", stringParameter("s").name)
        assertEquals("t", textParameter("t").name)
        assertEquals("u", uuidParameter("u").name)
    }

    @Test
    fun `listParameter and listElementParameter wire inner parameter`() = structureTest {
        val inner = stringParameter("")
        assertEquals(inner, listParameter("", inner).parameter)

        val source: ContextualValue<TestEnv, Unit, List<String>> = { ParsingResult.success(["a"]) }
        assertEquals("item", listElementParameter("item", source).name)
    }

    private enum class Color {
        RED,
        GREEN,
    }

    private enum class AliasedColor(override val label: String, override val aliases: Set<String>) : Aliasable {
        RED("red", ["r"]),
        GREEN("green", ["g"]),
    }
}
