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
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test

class CommandRunnerTest {

    private val handler = mockk<FailureHandler<TestEnv, Unit>>(relaxed = true)

    @Test
    fun `successful parse does not invoke failure handler`() {
        val structure = structure("cmd") { Signature0({}, LeadingParameterRole.Label, [it]) }

        CommandRunner(TestEnv, handler, structure, EmptyCoroutineContext, EmptyCoroutineContext)
            .execute(Unit, "cmd", [])

        verify { handler wasNot Called }
    }

    @Test
    fun `unmatched label reports InvalidSyntax`() {
        val structure = structure("cmd") { Signature0({}, LeadingParameterRole.Label, [it]) }

        CommandRunner(TestEnv, handler, structure, EmptyCoroutineContext, EmptyCoroutineContext)
            .execute(Unit, "other", [])

        verify { context(any<Execution<TestEnv, Unit>>()) { handler.onFailure(Reason.InvalidSyntax, any()) } }
    }

    @Test
    fun `handled failure is swallowed, not reported`() {
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = handled()
            }
        val structure =
            structure("cmd") { Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [it, parameter]) }

        CommandRunner(TestEnv, handler, structure, EmptyCoroutineContext, EmptyCoroutineContext)
            .execute(Unit, "cmd", ["x"])

        verify { handler wasNot Called }
    }

    @Test
    fun `missing args invokes failure handler with reason`() {
        val parameter = StringParameter<TestEnv, Unit>("", "")
        val structure =
            structure("cmd") { Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [it, parameter]) }

        CommandRunner(TestEnv, handler, structure, EmptyCoroutineContext, EmptyCoroutineContext)
            .execute(Unit, "cmd", [])

        verify { context(any<Execution<TestEnv, Unit>>()) { handler.onFailure(Reason.InvalidSyntax, any()) } }
    }

    private fun <T_ : Arguments> structure(
        label: String,
        signature: (Parameter<TestEnv, Unit, *, *>) -> Signature<TestEnv, Unit, T_>,
    ): StructureImpl<TestEnv, Unit, T_> =
        StructureImpl(label, [], "", signature)
}
