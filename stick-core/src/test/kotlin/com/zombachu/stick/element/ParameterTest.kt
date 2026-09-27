package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.failType
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ParameterTest {

    private val ranged =
        object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(args: List<String>): ConsumingResult<String> =
                when {
                    args[0] == "wide" -> success("wide").consuming(2)
                    args[0] == "narrow" -> success("narrow").consuming(1)
                    args[0] == "exact" -> success("exact").consuming(1, canConsumeMore = false)
                    else -> failType("thing", args[0])
                }
        }

    @Test
    fun `match reports consumed`() {
        assertEquals(MatchResult.matchedExactly(2), withValidationContext { ranged.match(["wide", "x"]) })
        assertEquals(MatchResult.matchedAtLeast(1), withValidationContext { ranged.match(["narrow"]) })
    }

    @Test
    fun `match allows early termination`() {
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { ranged.match(["exact"]) })
    }

    @Test
    fun `match with too few arguments returns partial`() {
        assertEquals(MatchResult.partial(), withValidationContext { ranged.match([]) })
    }

    @Test
    fun `match with failure returns error`() {
        val result = withValidationContext { ranged.match(["other"]) }
        assertEquals(Reason.TypeNotMatched("thing", "other"), result.expectUnmatched().expectReason())
    }

    @Test
    fun `fixed arity match returns partial for incomplete args`() {
        val parameter =
            object : Parameter.Size2<TestEnv, Unit, String>("", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String, arg1: String): CommandResult<String> = success(arg0)
            }

        assertEquals(MatchResult.partial(), withValidationContext { parameter.match(["a"]) })
    }

    @Test
    fun `match trims arguments to max`() {
        val parameter =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> =
                    success("").consuming(args.size)
            }

        assertEquals(MatchResult.matchedExactly(2), withValidationContext { parameter.match(["a", "b", "c"]) })
    }

    @Test
    fun `unbounded match with too few arguments returns partial`() {
        val parameter =
            object : Parameter.Unbounded<TestEnv, Unit, String>(Size.atLeast(2), "", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> =
                    success("").consuming(args.size)
            }

        assertEquals(MatchResult.partial(), withValidationContext { parameter.match(["a"]) })
    }

    @Test
    fun `parse ignores memo produced by another parameter`() {
        var resolves = 0
        val other = countingParameter {}
        val parameter = countingParameter { resolves++ }

        val ex = testExecution("a")
        ex.currentMatch = withValidationContext { other.match(["a"]) } as MatchResult.Matched
        val result = context(ex) { parameter.parse(["a"]) }

        assertEquals("a", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    private fun countingParameter(onResolve: () -> Unit) =
        object : Parameter.Size1<TestEnv, Unit, String>("", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> {
                onResolve()
                return success(arg0)
            }
        }
}
