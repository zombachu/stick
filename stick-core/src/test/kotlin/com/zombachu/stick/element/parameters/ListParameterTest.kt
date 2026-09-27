package com.zombachu.stick.element.parameters

import com.zombachu.stick.CommandResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import kotlin.test.Test
import kotlin.test.assertEquals

class ListParameterTest {

    private val parameter =
        ListParameter("", "", StringParameter<TestEnv, Unit>("", ""))

    @Test
    fun `splits on commas`() {
        val result = withExecution { parameter.parse(["a,b,c"]) }
        assertEquals(["a", "b", "c"], result.expectSuccessValue())
    }

    @Test
    fun `empty string produces single empty element`() {
        val result = withExecution { parameter.parse([""]) }
        assertEquals([""], result.expectSuccessValue())
    }

    @Test
    fun `trailing comma produces empty element`() {
        val result = withExecution { parameter.parse(["a,b,"]) }
        assertEquals(["a", "b", ""], result.expectSuccessValue())
    }

    @Test
    fun `single element produces single-item list`() {
        val result = withExecution { parameter.parse(["a"]) }
        assertEquals(["a"], result.expectSuccessValue())
    }

    @Test
    fun `first failure short-circuits`() {
        var calls = 0
        val counting =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    calls++
                    return if (arg0 == "bad") failType("item", arg0) else success(arg0)
                }
            }
        val listParameter = ListParameter("", "", counting)

        val result = withExecution { listParameter.parse(["a,bad,c"]) }

        assertEquals(Reason.TypeNotMatched("item", "bad"), result.expectReason())
        assertEquals(2, calls)
    }
}
