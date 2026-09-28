package com.zombachu.stick.element

import com.zombachu.stick.Arguments0
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectNoMatch
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failSender
import com.zombachu.stick.failSenderType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class StructureImplTest {

    @Test
    fun `matches name case-insensitively`() {
        val structure = structure(name = "cMd")

        val result = withExecution("CMD") { structure.parse(["CMD"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `matches alias case-insensitively`() {
        val structure = structure(name = "cmd", aliases = ["c"])

        val result = withExecution("C") { structure.parse(["C"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `mismatch fails with InvalidSyntax NoMatch`() {
        val structure = structure(name = "cmd")

        val result = withExecution("other") { structure.parse(["other"]) }

        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `match covers only label`() {
        val structure = structure(name = "cmd", aliases = ["c"])

        val result = withInvocation { structure.match(["C", "arg"]) }

        assertEquals(MatchResult.matchedExactly(1), result)
    }

    @Test
    fun `match unmatched returns fails with InvalidSyntax NoMatch`() {
        val structure = structure(name = "cmd")

        val result = withInvocation { structure.match(["other"]) }

        assertIs<Reason.InvalidSyntax>(result.expectUnmatched().expectNoMatch().reason)
    }

    @Test
    fun `match on empty args is partial`() {
        val structure = structure(name = "cmd")

        val result = withInvocation { structure.match([]) }

        assertEquals(MatchResult.partial(), result)
    }

    @Test
    fun `failing requirement short-circuits executing signature`() {
        var executed = false
        val structure =
            structure(
                name = "cmd",
                requirement = Requirement { failSender() },
                onExecute = { executed = true },
            )

        val result = withExecution("cmd") { structure.parse(["cmd"]) }

        assertSame(Reason.InvalidSender, result.expectReason())
        assertFalse(executed)
    }

    @Test
    fun `getSyntax returns name when signature has no syntax`() {
        val structure = structure(name = "cmd")
        assertEquals("cmd", withInvocation { structure.getSyntax() })
    }

    @Test
    fun `getSyntax returns signature syntax`() {
        val parameter = StringParameter<TestEnv, Unit>("arg", "")
        val structure =
            StructureImpl("cmd", [], "", Requirement<TestEnv, Unit> { success() }) {
                Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [it, parameter])
            }

        assertEquals("cmd <arg>", withInvocation { structure.getSyntax() })
    }

    @Test
    fun `SenderMappedStructure delegates parse to base`() {
        val base = structure(name = "cmd")
        val mapped = SenderMappedStructure(base, { _: Int -> })

        val result = withExecutionSender(1, "cmd") { mapped.parse(["cmd"]) }

        assertTrue(result.isSuccess())
    }

    @Test
    fun `GatedStructure validateSender includes base requirement`() {
        val base = structure(name = "cmd", requirement = Requirement { failSender() })
        val requirement = Requirement<TestEnv, Int> { success() }
        val gated = GatedStructure(SenderMappedStructure(base, { _: Int -> }), requirement)

        val result = withInvocation(1) { gated.validateSender() }

        assertFalse(result.isSuccess())
    }

    @Test
    fun `GatedStructure validateSender skips transform when requirement fails`() {
        val base = structure(name = "cmd")
        val requirement = Requirement<TestEnv, Int> { failSenderType(String::class) }
        val gated = GatedStructure(SenderMappedStructure(base, { _: Int -> error("transform ran") }), requirement)

        val result = withInvocation(1) { gated.validateSender() }

        assertFalse(result.isSuccess())
    }

    private fun structure(
        name: String,
        aliases: Set<String> = [],
        requirement: Requirement<TestEnv, Unit> = Requirement { success() },
        onExecute: () -> Unit = {},
    ): StructureImpl<TestEnv, Unit, Arguments0> =
        StructureImpl(name, aliases, "", requirement) { Signature0({ onExecute() }, LeadingParameterRole.Label, [it]) }
}
