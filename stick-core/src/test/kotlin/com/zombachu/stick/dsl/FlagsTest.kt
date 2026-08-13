package com.zombachu.stick.dsl

import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class FlagsTest {

    private val intParameter = IntParameter<TestEnv, Unit>("n", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `flag defaults to false`() = structureTest {
        val basicFlag = flag("loud")
        assertEquals(false, basicFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `flag parses to true`() = structureTest {
        val basicFlag = flag("loud")
        assertEquals(true, withInvocation { basicFlag.parse(["-loud"]) }.expectSuccessValue())
    }

    @Test
    fun `typed flag defaults to absent value`() = structureTest {
        val typedFlag = flag("boost", { ParsingResult.success(0) }, { ParsingResult.success(10) })
        assertEquals(0, typedFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `typed flag parses to given value`() = structureTest {
        val typedFlag = flag("boost", { ParsingResult.success(0) }, { ParsingResult.success(10) })
        assertEquals(10, withInvocation { typedFlag.parse(["-boost"]) }.expectSuccessValue())
    }

    @Test
    fun `valueFlag matches name`() = structureTest {
        val valueFlag = valueFlag("n", 0, intParameter("amount"))

        assertEquals(5, withInvocation { valueFlag.parse(["-n", "5"]) }.expectSuccessValue())
        assertEquals("[-n <amount>]", withValidationContext { valueFlag.getSyntax() })
    }

    @Test
    fun `flag matches a mixed-case name`() = structureTest {
        val basicFlag = flag("Loud")
        assertEquals(true, withInvocation { basicFlag.parse(["-loud"]) }.expectSuccessValue())
    }

    @Test
    fun `valueFlag defaults to given value`() = structureTest {
        val valueFlag = valueFlag("n", 0, intParameter)
        assertEquals(0, valueFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `nullableValueFlag defaults to null`() = structureTest {
        val nullableValueFlag = nullableValueFlag("n", intParameter)
        assertNull(nullableValueFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `valueFlag parses with parameter`() = structureTest {
        val valueFlag = valueFlag("n", { ParsingResult.success(0) }, intParameter)
        assertEquals(5, withInvocation { valueFlag.parse(["-n", "5"]) }.expectSuccessValue())
    }

    @Test
    fun `nullableValueFlag parses with parameter`() = structureTest {
        val nullableValueFlag = nullableValueFlag("n", intParameter)
        assertEquals(5, withInvocation { nullableValueFlag.parse(["-n", "5"]) }.expectSuccessValue())
    }

    @Test
    fun `enumFlag defaults to given value`() = structureTest {
        val enumFlag = enumFlag(Color.RED, enumParameter("", Color::class))
        assertEquals(Color.RED, enumFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `nullableEnumFlag defaults to null`() = structureTest {
        val nullableEnumFlag = nullableEnumFlag(enumParameter("", Color::class))
        assertNull(nullableEnumFlag.default(testInvocation()).expectSuccessValue())
    }

    @Test
    fun `enumFlag parses with parameter`() = structureTest {
        val enumFlag = enumFlag(Color.RED, enumParameter("", Color::class))
        assertEquals(Color.GREEN, withInvocation { enumFlag.parse(["-green"]) }.expectSuccessValue())
    }

    @Test
    fun `nullableEnumFlag parses with parameter`() = structureTest {
        val nullableEnumFlag = nullableEnumFlag(enumParameter("", Color::class))
        assertEquals(Color.GREEN, withInvocation { nullableEnumFlag.parse(["-green"]) }.expectSuccessValue())
    }

    @Test
    fun `hybridFlag defaults to absent`() = structureTest {
        val hybridFlag = hybridFlag("boost", intParameter)
        val result = withInvocation { hybridFlag.default(testInvocation()) }.expectSuccessValue()
        assertIs<HybridFlagResult.Absent<Int>>(result)
    }

    @Test
    fun `hybridFlag parses with parameter`() = structureTest {
        val hybridFlag = hybridFlag("boost", intParameter)

        val valueResult = withInvocation { hybridFlag.parse(["-boost", "5"]) }.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(valueResult)
        assertEquals(5, valueResult.value)

        val present = withInvocation { hybridFlag.parse(["-boost"]) }.expectSuccessValue()
        assertIs<HybridFlagResult.Present<Int>>(present)
    }

    private enum class Color {
        RED,
        GREEN,
    }
}
