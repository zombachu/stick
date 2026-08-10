package com.zombachu.stick.impl

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Signature
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.StructureImpl
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.feedback.FailureHandler
import com.zombachu.stick.feedback.Feedback
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CommandWrapperTest {

    @Test
    fun `successful parse does not invoke failure handler`() {
        val structure = structure("cmd", Signature0({}, []))
        val handler = RecordingFailureHandler()

        wrapper(structure, handler).execute(Unit, ["cmd"])

        assertEquals(0, handler.calls)
    }

    @Test
    fun `InternalFailure is swallowed, not reported`() {
        val structure = structure("cmd", Signature0({}, []))
        val handler = RecordingFailureHandler()

        wrapper(structure, handler).execute(Unit, ["other"])

        assertEquals(0, handler.calls)
    }

    @Test
    fun `missing args invokes failure handler with feedback`() {
        val parameter = StringParameter<TestEnv, Unit>("", "")
        val structure = structure("cmd", Signature1<TestEnv, Unit, String>({}, [parameter]))
        val handler = RecordingFailureHandler()

        wrapper(structure, handler).execute(Unit, ["cmd"])

        assertEquals(1, handler.calls)
        assertIs<Feedback.InvalidSyntax>(handler.lastFeedback)
    }

    private fun <T_ : Arguments> structure(label: String, signature: Signature<TestEnv, Unit, T_>): StructureImpl<TestEnv, Unit, T_> =
        StructureImpl(label, [], "", Requirement { SenderValidationResult.success() }, signature)

    private fun <T_ : Arguments> wrapper(
        structureImpl: StructureImpl<TestEnv, Unit, T_>,
        handler: RecordingFailureHandler,
    ): CommandWrapper<TestEnv, Unit> =
        object : CommandWrapper<TestEnv, Unit> {
            override val env: TestEnv = TestEnv
            override val failureHandler: FailureHandler<TestEnv, Unit> = handler
            override val structure = structureImpl
        }

    private class RecordingFailureHandler : FailureHandler<TestEnv, Unit> {
        var calls = 0
        var lastFeedback: Feedback? = null

        context(inv: Invocation<TestEnv, Unit>)
        override fun <F : Feedback> onFailure(failure: CommandResult.Failure<F>) {
            calls++
            lastFeedback = failure.feedback
        }
    }
}
