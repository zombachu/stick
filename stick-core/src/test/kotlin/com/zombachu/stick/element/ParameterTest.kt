package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.MatchResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ParameterTest {

    private val ranged =
        object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(1, 2), "", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(args: List<String>): CommandResult<String> =
                when {
                    args.isEmpty() -> ParsingResult.failSize()
                    args[0] == "wide" -> ParsingResult.success("wide", 2)
                    args[0] == "narrow" -> ParsingResult.success("narrow", 1)
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
}
