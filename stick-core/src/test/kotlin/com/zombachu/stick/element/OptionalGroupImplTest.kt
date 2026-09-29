package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.Invocation
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.expectNoMatch
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OptionalGroupImplTest {

    @Test
    fun `empty args with presence not allowed fails with InvalidSyntax`() {
        val optional = optionalGroup(presenceAllowed = false)
        val result = withExecution { optional.parse([]) }
        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `empty args returns presence default`() {
        val optional = optionalGroup(presenceAllowed = true)
        val result = withExecution { optional.parse([]) }
        assertEquals(GroupResult.ResultA("presence"), result.expectSuccessValue())
    }

    @Test
    fun `non-empty args delegates to group`() {
        val optional = optionalGroup(presenceAllowed = true)
        val result = withExecution("orange") { optional.parse(["orange"]) }
        assertEquals(GroupResult.ResultB("orange"), result.expectSuccessValue())
    }

    @Test
    fun `args matching nothing fails with InvalidSyntax NoMatch`() {
        val optional = optionalGroup(presenceAllowed = true)
        val result = withExecution("asdf") { optional.parse(["asdf"]) }
        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `match on empty args claims nothing`() {
        val optional = optionalGroup(presenceAllowed = true)
        assertEquals(MatchResult.matchedAtLeast(0), withInvocation { optional.match([]) })
    }

    @Test
    fun `match on non-empty args delegates to group`() {
        val optional = optionalGroup(presenceAllowed = true)
        assertEquals(MatchResult.matchedExactly(1), withInvocation { optional.match(["apple"]) })
    }

    @Test
    fun `size allows group to be absent`() {
        val optional = optionalGroup(presenceAllowed = true)
        assertEquals(0, optional.size.min)
        assertEquals(1, assertIs<Size.Bounded>(optional.size).max)
    }

    @Test
    fun `getSyntax shows as optional when optional for sender`() {
        val optional = optionalGroup(presenceAllowed = true)
        assertEquals("[apple|orange]", withInvocation { optional.getSyntax() })
    }

    @Test
    fun `getSyntax shows as required when required for sender`() {
        val optional = optionalGroup(presenceAllowed = false)
        assertEquals("<apple|orange>", withInvocation { optional.getSyntax() })
    }

    private fun optionalGroup(
        presenceAllowed: Boolean,
    ): OptionalGroupImpl<TestEnv, Unit, GroupResult2<String, String>, Position.Optional> =
        OptionalGroupImpl(
            group =
                Group2Impl<TestEnv, Unit, String, String, Position.Leading>(
                    "",
                    "",
                    LiteralParameter("apple", [], ""),
                    LiteralParameter("orange", [], ""),
                ),
            default = { success(GroupResult.ResultA("presence")) },
            defaultRequirement = Requirement { validate(presenceAllowed) },
        )

    context(_: Invocation<*, *>)
    private fun validate(allowed: Boolean): CommandResult<Unit> =
        if (allowed) success() else failSender()
}
