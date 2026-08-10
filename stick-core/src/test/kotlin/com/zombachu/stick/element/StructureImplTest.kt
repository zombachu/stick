package com.zombachu.stick.element

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.Arguments0
import com.zombachu.stick.impl.Requirement
import com.zombachu.stick.isSuccess
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class StructureImplTest {

    @Test
    fun `matches name case-insensitively`() {
        val structure = structure(name = "cmd")

        val result = withInvocation("CMD") { structure.parse(["CMD"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `matches alias case-insensitively`() {
        val structure = structure(name = "cmd", aliases = ["c"])

        val result = withInvocation("C") { structure.parse(["C"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `mismatch fails with TypeNotMatchedInternal`() {
        val structure = structure(name = "cmd")

        val result = withInvocation("other") { structure.parse(["other"]) }

        assertIs<ParsingResult.TypeNotMatchedInternal>(result)
    }

    @Test
    fun `failing requirement short-circuits executing signature`() {
        var executed = false
        val structure =
            structure(
                name = "cmd",
                requirement = Requirement { SenderValidationResult.failSender() },
                onExecute = { executed = true },
            )

        val result = withInvocation("cmd") { structure.parse(["cmd"]) }

        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
        assertFalse(executed)
    }

    @Test
    fun `getSyntax returns name when signature has no syntax`() {
        val structure = structure(name = "cmd")
        assertEquals("cmd", withValidationContext { structure.getSyntax() })
    }

    @Test
    fun `getSyntax returns signature syntax`() {
        val parameter = StringParameter<TestEnv, Unit>("arg", "")
        val signature = Signature1<TestEnv, Unit, String>({}, [parameter])
        val structure =
            StructureImpl(
                "cmd",
                [],
                "",
                Requirement { SenderValidationResult.success() },
                signature,
            )

        assertEquals("cmd <arg>", withValidationContext { structure.getSyntax() })
    }

    @Test
    fun `TransformedStructure delegates parse to base`() {
        val base = structure(name = "cmd")
        val requirement = Requirement<TestEnv, Int> { SenderValidationResult.success() }
        val transformed = TransformedStructure(base, { _: Int -> }, requirement)

        val result = withInvocationSender(1, "cmd") { transformed.parse(["cmd"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `TransformedStructure validateSender uses outer requirement, not base`() {
        val base = structure(name = "cmd", requirement = Requirement { SenderValidationResult.failSender() })
        val requirement = Requirement<TestEnv, Int> { SenderValidationResult.success() }
        val transformed = TransformedStructure(base, { _: Int -> }, requirement)

        val result = withValidationContext(1) { transformed.validateSender() }

        assertTrue(result.isSuccess())
    }

    private fun structure(
        name: String,
        aliases: Set<String> = [],
        requirement: Requirement<TestEnv, Unit> = Requirement { SenderValidationResult.success() },
        onExecute: () -> Unit = {},
    ): StructureImpl<TestEnv, Unit, Arguments0> =
        StructureImpl(name, aliases, "", requirement, Signature0({ onExecute() }, []))
}
