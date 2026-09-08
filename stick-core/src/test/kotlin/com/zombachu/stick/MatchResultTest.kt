package com.zombachu.stick

import kotlin.test.Test
import kotlin.test.assertSame

class MatchResultTest {

    @Test
    fun `unmatched defaults to TypeNotMatchedInternal`() {
        assertSame(ParsingResult.TypeNotMatchedInternal, MatchResult.unmatched().failure)
    }

    @Test
    fun `unmatched carries the failure parse would have given`() {
        val failure = ParsingResult.failLiteral(["give"], "take")
        assertSame(failure, MatchResult.unmatched(failure).failure)
    }
}
