package com.zombachu.stick.element.parameters

import com.zombachu.stick.MatchResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.withExecution
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class NumberParameterTest {

    @Test
    fun `byte accepts boundaries and rejects invalid input`() {
        val parameter = ByteParameter<TestEnv, Unit>("", "", 0, 10)

        assertEquals(0, parse(parameter, "0"))
        assertEquals(10, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0", "10", "11"), failure(parameter, "11"))
        assertEquals(Reason.TypeNotMatched("byte", "x"), failure(parameter, "x"))
    }

    @Test
    fun `short accepts boundaries and rejects invalid input`() {
        val parameter = ShortParameter<TestEnv, Unit>("", "", 0, 10)

        assertEquals(0, parse(parameter, "0"))
        assertEquals(10, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0", "10", "-1"), failure(parameter, "-1"))
        assertEquals(Reason.TypeNotMatched("short", "x"), failure(parameter, "x"))
    }

    @Test
    fun `int accepts boundaries and rejects invalid input`() {
        val parameter = IntParameter<TestEnv, Unit>("", "", 0, 10)

        assertEquals(0, parse(parameter, "0"))
        assertEquals(10, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0", "10", "11"), failure(parameter, "11"))
        assertEquals(Reason.TypeNotMatched("integer", "x"), failure(parameter, "x"))
    }

    @Test
    fun `long accepts boundaries and rejects invalid input`() {
        val parameter = LongParameter<TestEnv, Unit>("", "", 0L, 10L)

        assertEquals(0L, parse(parameter, "0"))
        assertEquals(10L, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0", "10", "11"), failure(parameter, "11"))
        assertEquals(Reason.TypeNotMatched("long", "x"), failure(parameter, "x"))
    }

    @Test
    fun `float accepts boundaries and rejects invalid input`() {
        val parameter = FloatParameter<TestEnv, Unit>("", "", 0f, 10f)

        assertEquals(0f, parse(parameter, "0"))
        assertEquals(10f, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0.0", "10.0", "11.0"), failure(parameter, "11.0"))
        assertEquals(Reason.TypeNotMatched("float", "x"), failure(parameter, "x"))
    }

    @Test
    fun `double accepts boundaries and rejects invalid input`() {
        val parameter = DoubleParameter<TestEnv, Unit>("", "", 0.0, 10.0)

        assertEquals(0.0, parse(parameter, "0"))
        assertEquals(10.0, parse(parameter, "10"))

        assertEquals(Reason.OutOfRange("0.0", "10.0", "11.0"), failure(parameter, "11.0"))
        assertEquals(Reason.TypeNotMatched("double", "x"), failure(parameter, "x"))
    }

    @Test
    fun `matching considers type not range`() {
        val parameter = IntParameter<TestEnv, Unit>("", "", 0, 10)

        assertEquals(MatchResult.matchedExactly(1), match(parameter, "11"))
        assertEquals(Reason.TypeNotMatched("integer", "x"), matchFailure(parameter, "x"))
    }

    private fun <T> match(parameter: Parameter.Size1<TestEnv, Unit, T>, arg: String): MatchResult =
        withValidationContext { parameter.match([arg]) }

    private fun <T> matchFailure(parameter: Parameter.Size1<TestEnv, Unit, T>, arg: String): Reason? =
        withValidationContext { parameter.match([arg]) }.expectUnmatched().expectReason()

    private fun <T> parse(parameter: Parameter.Size1<TestEnv, Unit, T>, arg: String): T =
        withExecution { parameter.parse([arg]) }.expectSuccessValue()

    private fun <T> failure(parameter: Parameter.Size1<TestEnv, Unit, T>, arg: String): Reason? =
        withExecution { parameter.parse([arg]) }.expectReason()
}
