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
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.fail
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.validSenderDefault
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs

class MappedElementTest {

    @Test
    fun `chained transforms run in order`() {
        val length =
            MappedParameter<TestEnv, Unit, String, Int, Position.Leading>(StringParameter("", "")) {
                success(it.length)
            }
        val mapped = MappedParameter<TestEnv, Unit, Int, Int, Position.Leading>(length) { success(it * 2) }

        val result = withExecution { mapped.parse(["hello"]) }

        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `MappedParameter delegates match to base`() {
        val mapped =
            MappedParameter<TestEnv, Unit, String, String, Position.Leading>(LiteralParameter("give", [], "")) {
                success(it)
            }

        assertEquals(MatchResult.matchedExactly(1), withInvocation { mapped.match(["give"]) })
    }

    @Test
    fun `MappedParameter delegates suggest to base`() {
        val mapped =
            MappedParameter<TestEnv, Unit, String, String, Position.Leading>(LiteralParameter("give", [], "")) {
                success(it)
            }

        assertEquals(["give"], withInvocation { mapped.suggest([], "") }.map { it.value })
    }

    @Test
    fun `MappedParameter delegates getGroupedSyntax to base`() {
        val base =
            object : Parameter.Size1<TestEnv, Unit, String>("", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = success(arg0)

                context(inv: Invocation<TestEnv, Unit>)
                override fun getGroupedSyntax(): String = "grouped"
            }
        val mapped = MappedParameter<TestEnv, Unit, String, String, Position.Leading>(base) { success(it) }

        assertEquals("grouped", withInvocation { mapped.getGroupedSyntax() })
    }

    @Test
    fun `MappedParameter short-circuits on failing transform`() {
        var laterCalled = false
        val failing =
            MappedParameter<TestEnv, Unit, String, Int, Position.Leading>(StringParameter("", "")) {
                fail(Reason.Unknown())
            }
        val mapped =
            MappedParameter<TestEnv, Unit, Int, Int, Position.Leading>(failing) {
                laterCalled = true
                success(it)
            }

        val result = withExecution { mapped.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(laterCalled)
    }

    @Test
    fun `MappedParameter short-circuits before transform if base element fails`() {
        var transformCalled = false
        val failingBase =
            object : Parameter.Size1<TestEnv, Unit, String>("bad", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.matchedExactly(1)

                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> = failType("bad", arg0)
            }
        val mapped =
            MappedParameter<TestEnv, Unit, String, String, Position.Leading>(failingBase) {
                transformCalled = true
                success(it)
            }

        val result = withExecution { mapped.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertFalse(transformCalled)
    }

    @Test
    fun `MappedParameter type reports base element type`() {
        val mapped =
            MappedParameter<TestEnv, Unit, String, String, Position.Leading>(LiteralParameter("", [], "")) {
                success(it)
            }
        assertEquals(GroupableType.Literal, mapped.type)
    }

    @Test
    fun `MappedParameter consumed size of fixed-size base returns base size`() {
        val mapped =
            MappedParameter<TestEnv, Unit, String, Int, Position.Leading>(StringParameter("", "")) {
                success(it.length)
            }

        val result = withExecution { mapped.parse(["hi"]) }

        assertIs<ConsumingResult.Success<Int>>(result)
        assertEquals(1, result.consumed)
    }

    @Test
    fun `MappedParameter consumed size of non-fixed base returns number of args consumed`() {
        val mapped =
            MappedParameter<TestEnv, Unit, String, String, Position.Last>(TextParameter("", "")) {
                success(it.uppercase())
            }

        val result = withExecution { mapped.parse(["a", "b", "c"]) }

        assertIs<ConsumingResult.Success<String>>(result)
        assertEquals(3, result.consumed)
        assertEquals("A B C", result.expectSuccessValue())
    }

    @Test
    fun `MappedValueFlag delegates suggest to base`() {
        val flagParameter =
            FlagParameter.ParameterFlagParameter("f", LiteralParameter<TestEnv, Unit>("give", [], ""), [])
        val base = ValueFlagImpl("f", { success("") }, flagParameter)
        val mapped = MappedValueFlag<TestEnv, Unit, String, String>(base) { success(it) }

        assertEquals(["give"], withInvocation { mapped.suggest(["-f"], "") }.map { it.value })
    }

    @Test
    fun `MappedValueFlag default runs transform on default value`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val mapped = MappedValueFlag<TestEnv, Unit, Int, Int>(base) { success(it * 10) }

        val result = mapped.default(testExecution())

        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `MappedValueFlag default short-circuits if transform fails`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val mapped = MappedValueFlag<TestEnv, Unit, Int, Int>(base) { fail(Reason.Unknown()) }

        val result = mapped.default(testExecution())

        assertFalse(result.isSuccess())
    }

    @Test
    fun `MappedValueFlag default short-circuits if base fails`() {
        val base = presenceValueFlag<TestEnv, Unit, Int>("", 5, 1)
        val failing = MappedValueFlag<TestEnv, Unit, Int, Int>(base) { fail(Reason.Unknown()) }
        val mapped = MappedValueFlag<TestEnv, Unit, Int, Int>(failing) { success(it) }

        val result = mapped.default(testExecution())

        assertFalse(result.isSuccess())
    }

    @Test
    fun `MappedValueFlag delegates match to base`() {
        val base = presenceValueFlag<TestEnv, Unit, Boolean>("silent", false, true)
        val mapped = MappedValueFlag<TestEnv, Unit, Boolean, Boolean>(base) { success(it) }

        val result = withInvocation { mapped.match(["-silent"]) }

        assertEquals(MatchResult.matchedExactly(1), result)
    }

    @Test
    fun `MappedOptionalParameter delegates suggest to base`() {
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                validSenderDefault(""),
                LiteralParameter("give", [], ""),
            )
        val mapped = MappedOptionalParameter<TestEnv, Unit, String, String, Position.Optional>(optional) { success(it) }

        assertEquals(["give"], withInvocation { mapped.suggest([], "") }.map { it.value })
    }

    @Test
    fun `MappedHelper runs transform on contextual value`() {
        val base = HelperImpl<TestEnv, Unit, Int>({ success(5) })
        val mapped = MappedHelper<TestEnv, Unit, Int, Int>(base) { success(it * 10) }

        val result = withExecution { mapped.parse([]) }

        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `MappedHelper short-circuits before transform if base fails`() {
        var transformCalled = false
        val base = HelperImpl<TestEnv, Unit, Int>({ fail(Reason.Unknown()) })
        val mapped =
            MappedHelper<TestEnv, Unit, Int, Int>(base) {
                transformCalled = true
                success(it)
            }

        val result = withExecution { mapped.parse([]) }

        assertFalse(result.isSuccess())
        assertFalse(transformCalled)
    }
}
