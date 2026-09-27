package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withExecution
import kotlin.test.Test
import kotlin.test.assertEquals

class StringParameterTest {

    private val parameter = StringParameter<TestEnv, Unit>("", "")

    @Test
    fun `raw argument passes through`() {
        assertEquals("Anything", withExecution { parameter.parse(["Anything"]) }.expectSuccessValue())
        assertEquals("", withExecution { parameter.parse([""]) }.expectSuccessValue())
    }

    @Test
    fun `type is Passthrough`() {
        assertEquals(GroupableType.Passthrough, parameter.type)
    }
}
