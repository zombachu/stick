package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class ListParameterTest {

    private val parameter =
        ListParameter("", "", StringParameter<TestEnv, Unit>("", ""))

    @Test
    fun `splits on commas`() {
        val result = withInvocation { parameter.parse("a,b,c") }
        assertEquals(["a", "b", "c"], result.expectSuccessValue())
    }

    @Test
    fun `empty string produces single empty element`() {
        val result = withInvocation { parameter.parse("") }
        assertEquals([""], result.expectSuccessValue())
    }

    @Test
    fun `trailing comma produces empty element`() {
        val result = withInvocation { parameter.parse("a,b,") }
        assertEquals(["a", "b", ""], result.expectSuccessValue())
    }

    @Test
    fun `single element produces single-item list`() {
        val result = withInvocation { parameter.parse("a") }
        assertEquals(["a"], result.expectSuccessValue())
    }

    @Test
    fun `first failure short-circuits`() {
        var calls = 0
        val counting =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String): CommandResult<String> {
                    calls++
                    return if (arg0 == "bad") ParsingResult.failType("item", arg0) else ParsingResult.success(arg0)
                }
            }
        val listParameter = ListParameter("", "", counting)

        val result = withInvocation { listParameter.parse("a,bad,c") }

        assertEquals(Feedback.TypeNotMatched("item", "bad"), result.expectFailure().feedback)
        assertEquals(2, calls)
    }
}
