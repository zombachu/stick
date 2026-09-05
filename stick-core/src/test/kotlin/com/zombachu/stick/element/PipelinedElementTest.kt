package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class PipelinedElementTest {

    @Test
    fun `operations chain in order`() {
        val lengthOp: PipelineOperation<TestEnv, Unit, String, Int> = { s -> ParsingResult.success(s.length) }
        val doubleOp: PipelineOperation<TestEnv, Unit, Int, Int> = { n -> ParsingResult.success(n * 2) }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(
                StringParameter("", ""),
                [lengthOp, doubleOp],
            )

        val result = withInvocation { pipelined.parse(["hello"]) }

        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `short-circuits on failing operation`() {
        var laterCalled = false
        val failingOp: PipelineOperation<TestEnv, Unit, String, Int> = { ParsingResult.failUnknown() }
        val laterOp: PipelineOperation<TestEnv, Unit, Int, Int> = {
            laterCalled = true
            ParsingResult.success(it)
        }
        val pipelined =
            PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(
                StringParameter("", ""),
                [failingOp, laterOp],
            )

        val result = withInvocation { pipelined.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(laterCalled)
    }

    @Test
    fun `short-circuits before operations if base element fails`() {
        var opCalled = false
        val failingBase =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(arg0: String): CommandResult<String> = ParsingResult.failType("bad", arg0)
            }
        val op: PipelineOperation<TestEnv, Unit, String, String> = {
            opCalled = true
            ParsingResult.success(it)
        }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, String, Position.Leading>(failingBase, [op])

        val result = withInvocation { pipelined.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(opCalled)
    }

    @Test
    fun `consumed size of fixed-size base returns base size`() {
        val op: PipelineOperation<TestEnv, Unit, String, Int> = { ParsingResult.success(it.length) }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, Int, Position.Leading>(StringParameter("", ""), [op])

        val result = withInvocation { pipelined.parse(["hi"]) }

        assertIs<CommandResult.Success<Int>>(result)
        assertEquals(1, result.consumed.size)
    }

    @Test
    fun `consumed size of non-fixed base returns number of args consumed`() {
        val op: PipelineOperation<TestEnv, Unit, String, String> = { ParsingResult.success(it.uppercase()) }
        val pipelined = PipelinedParameter<TestEnv, Unit, String, String, Position.Last>(TextParameter("", ""), [op])

        val result = withInvocation { pipelined.parse(["a", "b", "c"]) }

        assertIs<CommandResult.Success<String>>(result)
        assertEquals(3, result.consumed.size)
        assertEquals("A B C", result.expectSuccessValue())
    }

    @Test
    fun `PipelinedValueFlag default runs pipeline on default value`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val op: PipelineOperation<TestEnv, Unit, Int, Int> = { ParsingResult.success(it * 10) }
        val pipelined = PipelinedValueFlag<TestEnv, Unit, Int, Int>(base, [op])

        val result = pipelined.default(testInvocation())

        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `PipelinedValueFlag default short-circuits if operation fails`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val op: PipelineOperation<TestEnv, Unit, Int, Int> = { ParsingResult.failUnknown() }
        val pipelined = PipelinedValueFlag<TestEnv, Unit, Int, Int>(base, [op])

        val result = pipelined.default(testInvocation())

        assertFalse(result.isSuccess())
    }
}
