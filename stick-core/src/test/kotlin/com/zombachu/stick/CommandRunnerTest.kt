package com.zombachu.stick

import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.failure.FailureHandler
import com.zombachu.stick.failure.Reason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommandRunnerTest {

    @Test
    fun `successful parse does not invoke failure handler`() {
        val structure = structure("cmd") { Signature0({}, LeadingParameterRole.Label, [it]) }
        val handler = RecordingFailureHandler()

        CommandRunner(TestEnv, handler, structure).execute(Unit, "cmd", [])

        assertEquals(0, handler.calls)
    }

    @Test
    fun `unmatched label reports InvalidSyntax`() {
        val structure = structure("cmd") { Signature0({}, LeadingParameterRole.Label, [it]) }
        val handler = RecordingFailureHandler()

        CommandRunner(TestEnv, handler, structure).execute(Unit, "other", [])

        assertEquals(1, handler.calls)
        assertIs<Reason.InvalidSyntax>(handler.lastReason)
    }

    @Test
    fun `handled failure is swallowed, not reported`() {
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = handled()
            }
        val structure =
            structure("cmd") { Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [it, parameter]) }
        val handler = RecordingFailureHandler()

        CommandRunner(TestEnv, handler, structure).execute(Unit, "cmd", ["x"])

        assertEquals(0, handler.calls)
    }

    @Test
    fun `missing args invokes failure handler with reason`() {
        val parameter = StringParameter<TestEnv, Unit>("", "")
        val structure =
            structure("cmd") { Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [it, parameter]) }
        val handler = RecordingFailureHandler()

        CommandRunner(TestEnv, handler, structure).execute(Unit, "cmd", [])

        assertEquals(1, handler.calls)
        assertIs<Reason.InvalidSyntax>(handler.lastReason)
    }

    private fun <T_ : Arguments> structure(
        label: String,
        signature: (Parameter<TestEnv, Unit, *, *>) -> Signature<TestEnv, Unit, T_>,
    ): StructureImpl<TestEnv, Unit, T_> =
        StructureImpl(label, [], "", Requirement { success() }, signature)

    private class RecordingFailureHandler : FailureHandler<TestEnv, Unit> {
        var calls = 0
        var lastReason: Reason? = null

        context(inv: Invocation<TestEnv, Unit>)
        override fun onFailure(reason: Reason) {
            calls++
            lastReason = reason
        }
    }
}
