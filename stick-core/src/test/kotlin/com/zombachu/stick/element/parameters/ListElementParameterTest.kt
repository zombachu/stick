package com.zombachu.stick.element.parameters

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ListElementParameterTest {

    private val threeItems: ContextualValue<TestEnv, Unit, List<String>> = { ParsingResult.success(["a", "b", "c"]) }
    private val noItems: ContextualValue<TestEnv, Unit, List<String>> = { ParsingResult.success([]) }

    @Test
    fun `zero-indexed lookup resolves element`() {
        val parameter = listElementParameter("", "", threeItems, oneIndexed = false, onEmpty = null)

        val first = withInvocation { parameter.parse(["0"]) }.expectSuccessValue()
        val last = withInvocation { parameter.parse(["2"]) }.expectSuccessValue()

        assertEquals("a", first.result)
        assertEquals(0, first.index)
        assertEquals("c", last.result)
    }

    @Test
    fun `zero-indexed lookup rejects out-of-range index`() {
        val parameter = listElementParameter("", "", threeItems, oneIndexed = false, onEmpty = null)

        assertEquals(Feedback.OutOfRange("0", "2", "3"), failure(parameter, "3"))
        assertEquals(Feedback.OutOfRange("0", "2", "-1"), failure(parameter, "-1"))
    }

    @Test
    fun `one-indexed lookup shifts range to zero-based index`() {
        val parameter = listElementParameter("", "", threeItems, oneIndexed = true, onEmpty = null)

        val first = withInvocation { parameter.parse(["1"]) }.expectSuccessValue()

        assertEquals("a", first.result)
        assertEquals(0, first.index)
        assertEquals(Feedback.OutOfRange("1", "3", "0"), failure(parameter, "0"))
    }

    @Test
    fun `non-numeric index fails with TypeNotMatched`() {
        val parameter = listElementParameter("", "", threeItems, oneIndexed = false, onEmpty = null)
        assertEquals(Feedback.TypeNotMatched("index", "x"), failure(parameter, "x"))
    }

    @Test
    fun `empty list with onEmpty fails with HandledError`() {
        var onEmptyCalled = false
        val parameter =
            listElementParameter(
                "item",
                "",
                noItems,
                oneIndexed = false,
                onEmpty = { onEmptyCalled = true },
            )

        val result = withInvocation { parameter.parse(["0"]) }

        assertTrue(onEmptyCalled)
        assertSame(ParsingResult.HandledError, result)
    }

    @Test
    fun `empty list without onEmpty fails with OutOfRange`() {
        val parameter = listElementParameter("", "", noItems, oneIndexed = false, onEmpty = null)
        assertEquals(Feedback.OutOfRange("0", "-1", "0"), failure(parameter, "0"))
    }

    @Test
    fun `ContextualValue failure propagates`() {
        val failingList: ContextualValue<TestEnv, Unit, List<String>> = { ParsingResult.failUnknown() }
        val parameter = listElementParameter("", "", failingList, oneIndexed = false, onEmpty = null)
        assertEquals(Feedback.Unknown(), failure(parameter, "0"))
    }

    @Test
    fun `matching uses index but not list`() {
        var listReached = false
        val watched: ContextualValue<TestEnv, Unit, List<String>> = {
            listReached = true
            ParsingResult.success(["a", "b", "c"])
        }
        val parameter = listElementParameter("", "", watched, oneIndexed = false, onEmpty = null)

        val result = withValidationContext { parameter.match(["x"]) }

        assertEquals(Feedback.TypeNotMatched("index", "x"), result.expectUnmatched().expectFailure().feedback)
        assertFalse(listReached)
    }

    private fun failure(
        parameter: Parameter<TestEnv, Unit, ListElementResult<String>, Position.Leading>,
        arg: String,
    ): Feedback =
        withInvocation { parameter.parse([arg]) }.expectFailure().feedback
}
