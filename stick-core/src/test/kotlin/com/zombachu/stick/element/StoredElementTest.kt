package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.dsl.id
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StoredElementTest {

    @Test
    fun `StoredFixedSizeParameter stores parsed value`() {
        val base = StringParameter<TestEnv, Unit>("", "")
        val identifier = id<String>("name")
        val stored = StoredFixedSizeParameter(base, identifier)

        val inv = testInvocation("bob")
        val result = context(inv) { stored.parse(["bob"]) }

        assertEquals("bob", result.expectSuccessValue())
        assertEquals("bob", inv.get(identifier))
    }

    @Test
    fun `StoredFixedSizeParameter stores nothing on failure`() {
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String): CommandResult<String> = ParsingResult.failType("", arg0)
            }
        val identifier = id<String>("bad")
        val stored = StoredFixedSizeParameter(parameter, identifier)

        val inv = testInvocation("x")
        val result = context(inv) { stored.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertNull(inv.get(identifier))
    }

    @Test
    fun `StoredValueFlag stores parsed value`() {
        val base = presenceValueFlag<TestEnv, Unit, Boolean>("loud", false, true)
        val identifier = id<Boolean>("loud")
        val stored = StoredValueFlag(base, identifier)

        val inv = testInvocation("-loud")
        val result = context(inv) { stored.parse(["-loud"]) }

        assertEquals(true, result.expectSuccessValue())
        assertEquals(true, inv.get(identifier))
    }

    @Test
    fun `KNOWN LIMITATION - StoredValueFlag ignores base requirement`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val validated =
            TransformedValueFlag(
                base,
                { it: Int -> it.toString() },
                invalidSenderDefault(false) { SenderValidationResult.failSenderType() },
            )
        val stored = StoredValueFlag(validated, id<Boolean>("silent"))

        assertFalse(withValidationContext(1) { validated.validateSender() }.isSuccess())
        assertTrue(withValidationContext(1) { stored.validateSender() }.isSuccess())
    }

    @Test
    fun `StoredHelper stores contextual value`() {
        val base = HelperImpl<TestEnv, Unit, String>({ ParsingResult.success("computed") })
        val identifier = id<String>("computed")
        val stored = StoredHelper(base, identifier)

        val inv = testInvocation()
        val result = context(inv) { stored.parse([]) }

        assertEquals("computed", result.expectSuccessValue())
        assertEquals("computed", inv.get(identifier))
    }
}
