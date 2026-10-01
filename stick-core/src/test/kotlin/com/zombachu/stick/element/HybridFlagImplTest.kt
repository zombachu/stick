package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.consuming
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.expectNoMatch
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failSenderType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withExecution
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HybridFlagImplTest {

    private val parameter = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)
    private val flag = HybridFlagImpl("boost", parameter, [])

    @Test
    fun `parse resolves parameter once`() {
        var resolves = 0
        val counting =
            object : Parameter.Size1<TestEnv, Unit, String>("name", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    resolves++
                    return success(arg0)
                }
            }
        val countingFlag = HybridFlagImpl("rank", counting, [])

        val ex = testExecution("-rank", "guest")
        val result = ex.processElement(countingFlag)

        assertEquals("guest", assertIs<HybridFlagResult.Value<String>>(result.expectSuccessValue()).value)
        assertEquals(2, ex.consumedArgs)
        assertEquals(1, resolves)
    }

    @Test
    fun `parse consumes what variable-width parameter took`() {
        val varying =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "v", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> = success(args[0]).consuming(1)
            }
        val varyingFlag = HybridFlagImpl("boost", varying, [])

        val ex = testExecution("-boost", "a", "b")
        val result = ex.processElement(varyingFlag)

        assertEquals("a", assertIs<HybridFlagResult.Value<String>>(result.expectSuccessValue()).value)
        assertEquals(2, ex.consumedArgs)
    }

    @Test
    fun `matches with no trailing value returns Present`() {
        val result = withExecution { flag.parse(["-boost"]) }
        assertIs<HybridFlagResult.Present<Int>>(result.expectSuccessValue())
    }

    @Test
    fun `matches with trailing value parses value`() {
        val result = withExecution { flag.parse(["-boost", "5"]) }

        val value = result.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(value)
        assertEquals(5, value.value)
    }

    @Test
    fun `match with no trailing value claims label`() {
        assertEquals(MatchResult.matchedAtLeast(1), withInvocation { flag.match(["-boost"]) })
    }

    @Test
    fun `match with trailing value claims label and value`() {
        assertEquals(MatchResult.matchedExactly(2), withInvocation { flag.match(["-boost", "5"]) })
    }

    @Test
    fun `match unmatched fails with InvalidSyntax NoMatch`() {
        val result = withInvocation { flag.match(["-other"]) }
        assertIs<Reason.InvalidSyntax>(result.expectUnmatched().expectNoMatch().reason)
    }

    @Test
    fun `parameter failure fails with TypeNotMatched`() {
        val result = withExecution { flag.parse(["-boost", "not-a-number"]) }
        assertEquals(Reason.TypeNotMatched("integer", "not-a-number"), result.expectReason())
    }

    @Test
    fun `mismatch fails with InvalidSyntax NoMatch`() {
        val result = withExecution { flag.parse(["-other"]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `default value is Absent`() {
        val defaultResult = withExecution { flag.parse([]) }
        assertIs<HybridFlagResult.Absent<Int>>(defaultResult.expectSuccessValue())
    }

    @Test
    fun `getSyntax nests flag parameter syntax`() {
        val syntax = withInvocation { flag.getSyntax() }
        assertEquals("[-boost [amount]]", syntax)
    }

    @Test
    fun `GatedHybridFlag delegates parse to flag parameter`() {
        val gated = GatedHybridFlag(SenderMappedHybridFlag(flag, { _: Int -> }), Requirement { success() }, null)

        val result = withExecutionSender(1) { gated.parse(["-boost", "5"]) }

        val value = result.expectSuccessValue()
        assertIs<HybridFlagResult.Value<Int>>(value)
        assertEquals(5, value.value)
    }

    @Test
    fun `GatedHybridFlag default for accessible flag returns Absent`() {
        val gated =
            GatedHybridFlag(
                SenderMappedHybridFlag(flag, { _: Int -> }),
                Requirement { success() },
                { success(HybridFlagResult.Present()) },
            )
        val result = withExecutionSender(1) { gated.parse([]) }
        assertIs<HybridFlagResult.Absent<Int>>(result.expectSuccessValue())
    }

    @Test
    fun `GatedHybridFlag default for inaccessible flag returns denied default`() {
        val gated =
            GatedHybridFlag(
                SenderMappedHybridFlag(flag, { _: Int -> }),
                Requirement { failSenderType(String::class) },
                { success(HybridFlagResult.Present()) },
            )
        val result = withExecutionSender(1) { gated.parse([]) }
        assertIs<HybridFlagResult.Present<Int>>(result.expectSuccessValue())
    }

    @Test
    fun `GatedHybridFlag default for inaccessible flag without denied default returns Absent`() {
        val gated =
            GatedHybridFlag(
                SenderMappedHybridFlag(flag, { _: Int -> }),
                Requirement { failSenderType(String::class) },
                null,
            )
        val result = withExecutionSender(1) { gated.parse([]) }
        assertIs<HybridFlagResult.Absent<Int>>(result.expectSuccessValue())
    }
}
