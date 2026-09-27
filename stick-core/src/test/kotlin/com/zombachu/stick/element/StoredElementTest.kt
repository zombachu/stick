package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.dsl.id
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failType
import com.zombachu.stick.isSuccess
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class StoredElementTest {

    @Test
    fun `StoredParameter stores parsed value`() {
        val base = StringParameter<TestEnv, Unit>("", "")
        val identifier = id<String>("name")
        val stored = StoredParameter(base, identifier)

        val ex = testExecution("bob")
        val result = context(ex) { stored.parse(["bob"]) }

        assertEquals("bob", result.expectSuccessValue())
        assertEquals("bob", ex.get(identifier))
    }

    @Test
    fun `StoredParameter delegates match to base`() {
        val stored = StoredParameter(LiteralParameter<TestEnv, Unit>("give", [], ""), id<String>("literal"))
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { stored.match(["give"]) })
    }

    @Test
    fun `StoredParameter delegates suggest to base`() {
        val stored = StoredParameter(LiteralParameter<TestEnv, Unit>("give", [], ""), id<String>("literal"))
        assertEquals(["give"], withValidationContext { stored.suggest([], "") }.map { it.value })
    }

    @Test
    fun `StoredParameter stores nothing on failure`() {
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = failType("", arg0)
            }
        val identifier = id<String>("bad")
        val stored = StoredParameter(parameter, identifier)

        val ex = testExecution("x")
        val result = context(ex) { stored.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertNull(ex.get(identifier))
    }

    @Test
    fun `StoredHelper stores contextual value`() {
        val base = HelperImpl<TestEnv, Unit, String>({ success("computed") })
        val identifier = id<String>("computed")
        val stored = StoredHelper(base, identifier)

        val ex = testExecution()
        val result = context(ex) { stored.parse([]) }

        assertEquals("computed", result.expectSuccessValue())
        assertEquals("computed", ex.get(identifier))
    }
}
