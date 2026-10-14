package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.verifyResolves
import com.zombachu.stick.withExecution
import io.mockk.spyk
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
        val item = spyk(IntParameter<TestEnv, Unit>("", "", Int.MIN_VALUE, Int.MAX_VALUE))
        val listParameter = ListParameter("", "", item)

        val result = withExecution { listParameter.parse(["1,bad,3"]) }

        assertEquals(Reason.TypeNotMatched("integer", "bad"), result.expectReason())
        verifyResolves(item, exactly = 2)
    }
}
