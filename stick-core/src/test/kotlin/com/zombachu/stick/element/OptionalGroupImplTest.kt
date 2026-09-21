package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.MatchResult
import com.zombachu.stick.Position
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.TypeNotMatchedInternal
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.expectUnmatched
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.validSenderDefault
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class OptionalGroupImplTest {

    @Test
    fun `empty args with group not allowed returns requirement default`() {
        val optional = optionalGroup(groupAllowed = false, presenceAllowed = true)
        val result = withInvocation { optional.parse([]) }
        assertEquals(GroupResult.ResultA("requirement"), result.expectSuccessValue())
    }

    @Test
    fun `empty args with presence not allowed fails with InvalidSyntax`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = false)
        val result = withInvocation { optional.parse([]) }
        assertIs<Feedback.InvalidSyntax>(result.expectFailure().feedback)
    }

    @Test
    fun `empty args returns presence default`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        val result = withInvocation { optional.parse([]) }
        assertEquals(GroupResult.ResultA("presence"), result.expectSuccessValue())
    }

    @Test
    fun `non-empty args with group not allowed fails with InvalidSender`() {
        val optional = optionalGroup(groupAllowed = false, presenceAllowed = true)
        val result = withInvocation("orange") { optional.parse(["orange"]) }
        assertSame(Feedback.InvalidSender, result.expectFailure().feedback)
    }

    @Test
    fun `non-empty args delegates to group`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        val result = withInvocation("orange") { optional.parse(["orange"]) }
        assertEquals(GroupResult.ResultB("orange"), result.expectSuccessValue())
    }

    @Test
    fun `args matching nothing fails with TypeNotMatchedInternal`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        val result = withInvocation("asdf") { optional.parse(["asdf"]) }
        assertSame(TypeNotMatchedInternal, result)
    }

    @Test
    fun `match on empty args claims nothing`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        assertEquals(MatchResult.matchedAtLeast(0), withValidationContext { optional.match([]) })
    }

    @Test
    fun `match on non-empty args delegates to group`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        assertEquals(MatchResult.matchedExactly(1), withValidationContext { optional.match(["apple"]) })
    }

    @Test
    fun `match on non-empty args with group not allowed fails with InvalidSender`() {
        val optional = optionalGroup(groupAllowed = false, presenceAllowed = true)
        val result = withValidationContext { optional.match(["apple"]) }
        assertSame(Feedback.InvalidSender, result.expectUnmatched().expectFailure().feedback)
    }

    @Test
    fun `size allows group to be absent`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        assertEquals(0, optional.size.min)
        assertEquals(1, assertIs<Size.Bounded>(optional.size).max)
    }

    @Test
    fun `getSyntax shows as optional when optional for sender`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = true)
        assertEquals("[apple|orange]", withValidationContext { optional.getSyntax() })
    }

    @Test
    fun `getSyntax shows as required when required for sender`() {
        val optional = optionalGroup(groupAllowed = true, presenceAllowed = false)
        assertEquals("<apple|orange>", withValidationContext { optional.getSyntax() })
    }

    @Test
    fun `getSyntax returns empty when group not allowed`() {
        val optional = optionalGroup(groupAllowed = false, presenceAllowed = true)
        assertEquals("", withValidationContext { optional.getSyntax() })
    }

    private fun optionalGroup(
        groupAllowed: Boolean,
        presenceAllowed: Boolean,
    ): OptionalGroupImpl<TestEnv, Unit, GroupResult2<String, String>, Position.Optional> =
        OptionalGroupImpl(
            requirementDefault = invalidSenderDefault(GroupResult.ResultA("requirement")) { validate(groupAllowed) },
            presenceDefault = validSenderDefault(GroupResult.ResultA("presence")) { validate(presenceAllowed) },
            group =
                Group2Impl<TestEnv, Unit, String, String, Position.Leading>(
                    "",
                    "",
                    LiteralParameter("apple", [], ""),
                    LiteralParameter("orange", [], ""),
                ),
        )

    private fun validate(allowed: Boolean): CommandResult<Unit> =
        if (allowed) SenderValidationResult.success() else SenderValidationResult.failSender()
}
