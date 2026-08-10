package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.ElementType
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class StringParameterTest {

    private val parameter = StringParameter<TestEnv, Unit>("", "")

    @Test
    fun `raw argument passes through`() {
        assertEquals("Anything", withInvocation { parameter.parse("Anything") }.expectSuccessValue())
        assertEquals("", withInvocation { parameter.parse("") }.expectSuccessValue())
    }

    @Test
    fun `type is Passthrough`() {
        assertEquals(ElementType.Passthrough, parameter.type)
    }
}
