package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ParameterTest {

    private val ranged =
        object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(args: List<String>): ConsumingResult<String> =
                when {
                    args.isEmpty() -> ParsingResult.failSize()
                    args[0] == "wide" -> ParsingResult.success("wide").consuming(2)
                    args[0] == "narrow" -> ParsingResult.success("narrow").consuming(1)
                    else -> ParsingResult.failType("thing", args[0])
                }
        }

    @Test
    fun `match claims width result reports`() {
        assertEquals(MatchResult.matched(2), withValidationContext { ranged.match(["wide", "x"]) })
        assertEquals(MatchResult.matched(1), withValidationContext { ranged.match(["narrow"]) })
    }

    @Test
    fun `match with too few arguments returns partial`() {
        assertEquals(MatchResult.partial(0), withValidationContext { ranged.match([]) })
    }

    @Test
    fun `match with failure returns error`() {
        val result = withValidationContext { ranged.match(["other"]) }
        assertEquals(Feedback.TypeNotMatched("thing", "other"), result.expectUnmatched().expectFailure().feedback)
    }

    @Test
    fun `parse ignores memo produced by another parameter`() {
        var resolves = 0
        val other = countingParameter {}
        val parameter = countingParameter { resolves++ }

        val inv = testInvocation("a")
        inv.currentMatch = withValidationContext { other.match(["a"]) } as MatchResult.Matched
        val result = context(inv) { parameter.parse(["a"]) }

        assertEquals("a", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    private fun countingParameter(onResolve: () -> Unit) =
        object : Parameter.Size1<TestEnv, Unit, String>("", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> {
                onResolve()
                return ParsingResult.success(arg0)
            }
        }
}
