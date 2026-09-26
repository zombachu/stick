package com.zombachu.stick.element.parameters

import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.withInvocation
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UUIDParameterTest {

    private val parameter = UUIDParameter<TestEnv, Unit>("", "")

    @Test
    fun `parses valid UUID`() {
        val uuid = UUID.randomUUID()
        val result = withInvocation { parameter.parse([uuid.toString()]) }
        assertEquals(uuid, result.expectSuccessValue())
    }

    @Test
    fun `rejects malformed UUID`() {
        val result = withInvocation { parameter.parse(["not-a-uuid"]) }
        assertEquals(Reason.TypeNotMatched("UUID", "not-a-uuid"), result.expectReason())
    }
}
