package com.zombachu.stick

import com.zombachu.stick.failure.Reason
import kotlin.test.Test
import kotlin.test.assertSame

class MatchResultTest {

    @Test
    fun `unmatched defaults to InvalidSyntax`() {
        val result = withValidationContext { MatchResult.unmatched() }
        assertSame(Reason.InvalidSyntax, result.failure.expectNoMatch().reason)
    }

    @Test
    fun `unmatched carries the failure parse would have given`() {
        val failure = withValidationContext { failLiteral(["give"], "take") }
        assertSame(failure, MatchResult.unmatched(failure).failure)
    }
}
