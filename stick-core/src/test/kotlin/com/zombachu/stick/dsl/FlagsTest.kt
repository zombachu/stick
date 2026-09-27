package com.zombachu.stick.dsl

import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parse
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class FlagsTest {

    private val intParameter = IntParameter<TestEnv, Unit>("n", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `flag defaults to false`() = structureTest {
        val basicFlag = flag("loud")
        assertEquals(false, basicFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `flag parses to true`() = structureTest {
        val basicFlag = flag("loud")
        assertEquals(true, withExecution { basicFlag.parse(["-loud"]) }.expectSuccessValue())
    }

    @Test
    fun `typed flag defaults to absent value`() = structureTest {
        val typedFlag = flag("boost", { success(0) }, { success(10) })
        assertEquals(0, typedFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `typed flag parses to given value`() = structureTest {
        val typedFlag = flag("boost", { success(0) }, { success(10) })
        assertEquals(10, withExecution { typedFlag.parse(["-boost"]) }.expectSuccessValue())
    }

    @Test
    fun `valueFlag matches name`() = structureTest {
        val valueFlag = valueFlag("n", 0, intParameter("amount"))

        assertEquals(5, withExecution { valueFlag.parse(["-n", "5"]) }.expectSuccessValue())
        assertEquals("[-n <amount>]", withInvocation { valueFlag.getSyntax() })
    }

    @Test
    fun `flag matches a mixed-case name`() = structureTest {
        val basicFlag = flag("Loud")
        assertEquals(true, withExecution { basicFlag.parse(["-loud"]) }.expectSuccessValue())
    }

    @Test
    fun `valueFlag defaults to given value`() = structureTest {
        val valueFlag = valueFlag("n", 0, intParameter)
        assertEquals(0, valueFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `nullableValueFlag defaults to null`() = structureTest {
        val nullableValueFlag = nullableValueFlag("n", intParameter)
        assertNull(nullableValueFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `valueFlag parses with parameter`() = structureTest {
        val valueFlag = valueFlag("n", { success(0) }, intParameter)
        assertEquals(5, withExecution { valueFlag.parse(["-n", "5"]) }.expectSuccessValue())
    }

    @Test
    fun `nullableValueFlag parses with parameter`() = structureTest {
        val nullableValueFlag = nullableValueFlag("n", intParameter)
        assertEquals(5, withExecution { nullableValueFlag.parse(["-n", "5"]) }.expectSuccessValue())
    }

    @Test
    fun `enumFlag defaults to given value`() = structureTest {
        val enumFlag = enumFlag(Color.RED, enumParameter("", Color::class))
        assertEquals(Color.RED, enumFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `nullableEnumFlag defaults to null`() = structureTest {
        val nullableEnumFlag = nullableEnumFlag(enumParameter("", Color::class))
        assertNull(nullableEnumFlag.default(testExecution()).expectSuccessValue())
    }

    @Test
    fun `enumFlag parses with parameter`() = structureTest {
        val enumFlag = enumFlag(Color.RED, enumParameter("", Color::class))
        assertEquals(Color.GREEN, withExecution { enumFlag.parse(["-green"]) }.expectSuccessValue())
    }

    @Test
    fun `nullableEnumFlag parses with parameter`() = structureTest {
        val nullableEnumFlag = nullableEnumFlag(enumParameter("", Color::class))
        assertEquals(Color.GREEN, withExecution { nullableEnumFlag.parse(["-green"]) }.expectSuccessValue())
    }

    @Test
    fun `hybridFlag defaults to absent`() = structureTest {
        val hybridFlag = hybridFlag("boost", intParameter)
        val result = withExecution { hybridFlag.default(testExecution()) }.expectSuccessValue()
        assertIs<HybridFlagResult.Absent<Int>>(result)
    }

    @Test
    fun `hybridFlag parses with parameter`() = structureTest {
        val hybridFlag = hybridFlag("boost", intParameter)

        val valueResult = withExecution { hybridFlag.parse(["-bOOst", "5"]) }.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(valueResult)
        assertEquals(5, valueResult.value)

        val present = withExecution { hybridFlag.parse(["-boost"]) }.expectSuccessValue()
        assertIs<HybridFlagResult.Present<Int>>(present)
    }

    private enum class Color {
        RED,
        GREEN,
    }
}
