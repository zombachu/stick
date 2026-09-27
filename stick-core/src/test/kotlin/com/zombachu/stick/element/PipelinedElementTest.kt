package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.fail
import com.zombachu.stick.failSenderType
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class PipelinedElementTest {

    @Test
    fun `operations chain in order`() {
        val lengthOp: PipelineOperation<TestEnv, Unit, String, Int> = { s -> success(s.length) }
        val doubleOp: PipelineOperation<TestEnv, Unit, Int, Int> = { n -> success(n * 2) }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(
                StringParameter("", ""),
                [lengthOp, doubleOp],
            )

        val result = withExecution { pipelined.parse(["hello"]) }

        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `PipelinedParameter delegates match to base`() {
        val op: PipelineOperation<TestEnv, Unit, String, String> = { success(it) }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, String, Position.Leading>(
                LiteralParameter("give", [], ""),
                [op],
            )

        assertEquals(MatchResult.matchedExactly(1), withInvocation { pipelined.match(["give"]) })
    }

    @Test
    fun `PipelinedParameter delegates suggest to base`() {
        val op: PipelineOperation<TestEnv, Unit, String, String> = { success(it) }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, String, Position.Leading>(LiteralParameter("give", [], ""), [op])

        assertEquals(["give"], withInvocation { pipelined.suggest([], "") }.map { it.value })
    }

    @Test
    fun `short-circuits on failing operation`() {
        var laterCalled = false
        val failingOp: PipelineOperation<TestEnv, Unit, String, Int> = { fail(Reason.Unknown()) }
        val laterOp: PipelineOperation<TestEnv, Unit, Int, Int> = {
            laterCalled = true
            success(it)
        }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(
                StringParameter("", ""),
                [failingOp, laterOp],
            )

        val result = withExecution { pipelined.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(laterCalled)
    }

    @Test
    fun `short-circuits before operations if base element fails`() {
        var opCalled = false
        val failingBase =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = failType("bad", arg0)
            }
        val op: PipelineOperation<TestEnv, Unit, String, String> = {
            opCalled = true
            success(it)
        }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, String, Position.Leading>(failingBase, [op])

        val result = withExecution { pipelined.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(opCalled)
    }

    @Test
    fun `type reports base element type`() {
        val op: PipelineOperation<TestEnv, Unit, String, String> = { success(it) }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, String, Position.Leading>(LiteralParameter("", [], ""), [op])
        assertEquals(GroupableType.Literal, pipelined.type)
    }

    @Test
    fun `consumed size of fixed-size base returns base size`() {
        val op: PipelineOperation<TestEnv, Unit, String, Int> = { success(it.length) }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(StringParameter("", ""), [op])

        val result = withExecution { pipelined.parse(["hi"]) }

        assertIs<ConsumingResult.Success<Int>>(result)
        assertEquals(1, result.consumed)
    }

    @Test
    fun `consumed size of non-fixed base returns number of args consumed`() {
        val op: PipelineOperation<TestEnv, Unit, String, String> = { success(it.uppercase()) }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, String, Position.Last>(TextParameter("", ""), [op])

        val result = withExecution { pipelined.parse(["a", "b", "c"]) }

        assertIs<ConsumingResult.Success<String>>(result)
        assertEquals(3, result.consumed)
        assertEquals("A B C", result.expectSuccessValue())
    }

    @Test
    fun `PipelinedValueFlag default runs pipeline on default value`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val op: PipelineOperation<TestEnv, Unit, Int, Int> = { success(it * 10) }
        val pipelined = PipelinedValueFlag<TestEnv, Unit, Int, Int>(base, [op])

        val result = pipelined.default(testExecution())

        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `PipelinedValueFlag default short-circuits if operation fails`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val op: PipelineOperation<TestEnv, Unit, Int, Int> = { fail(Reason.Unknown()) }
        val pipelined = PipelinedValueFlag<TestEnv, Unit, Int, Int>(base, [op])

        val result = pipelined.default(testExecution())

        assertFalse(result.isSuccess())
    }

    @Test
    fun `PipelinedValueFlag default short-circuits if base fails`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val failing: PipelineOperation<TestEnv, Unit, Int, Int> = { fail(Reason.Unknown()) }
        val passing: PipelineOperation<TestEnv, Unit, Int, Int> = { success(it) }
        val pipelined =
            PipelinedValueFlag<TestEnv, Unit, Int, Int>(
                PipelinedValueFlag<TestEnv, Unit, Int, Int>(base, [failing]),
                [passing],
            )

        val result = pipelined.default(testExecution())

        assertFalse(result.isSuccess())
    }

    @Test
    fun `PipelinedValueFlag delegates validateSender to base`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("silent", false, true)
        val invalidDefault =
            invalidSenderDefault<TestEnv, Int, Boolean>(false) { failSenderType(String::class) }
        val validated = TransformedValueFlag(base, { it: Int -> it.toString() }, invalidDefault)
        val pipelined = PipelinedValueFlag<TestEnv, Int, Boolean, Boolean>(validated, [])

        val result = withInvocation(1) { pipelined.validateSender() }

        assertEquals(Reason.InvalidSenderType(String::class), result.expectReason())
    }
}
