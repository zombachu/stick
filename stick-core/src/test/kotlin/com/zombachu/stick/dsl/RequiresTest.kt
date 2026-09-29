package com.zombachu.stick.dsl

import com.zombachu.stick.ContextualValue
import com.zombachu.stick.GroupResult
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.parse
import com.zombachu.stick.element.validateSender
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.testExecutionSender
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequiresTest {

    @Test
    fun `requireSender in group skips alternative for wrong sender`() = structureTest<BaseSender> {
        val gated = requireSender(Player::class) { stringParameter("") }
        val grouped = group(gated, stringParameter("fallback"))

        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        val playerResult = withExecutionSender(player, "bob") { grouped.parse(["bob"]) }
        val consoleResult = withExecutionSender(console, "bob") { grouped.parse(["bob"]) }

        assertIs<GroupResult.ResultA<String>>(playerResult.expectSuccessValue())
        assertIs<GroupResult.ResultB<String>>(consoleResult.expectSuccessValue())
    }

    @Test
    fun `require enforces given requirement`() = structureTest {
        val allowed = require(requirement { true }) { stringParameter("") }
        val denied = require(requirement { false }) { stringParameter("") }

        assertTrue(withInvocation { allowed.validateSender() }.isSuccess())
        assertSame(Reason.InvalidSender, withInvocation { denied.validateSender() }.expectReason())
    }

    @Test
    fun `requireSender on ValueFlag falls back to denied default`() = structureTest<BaseSender> {
        val gatedFlag = requireSender(Player::class, default = 999) { valueFlag("n", intParameter("n"), 0) }
        val signature =
            Signature1<TestEnv, BaseSender, Int>({}, LeadingParameterRole.Label, [literalParameter("cmd"), gatedFlag])

        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        val consoleResult = withExecutionSender(console, "cmd") { signature.execute() }
        val playerResult = withExecutionSender(player, "cmd", "-n", "5") { signature.execute() }

        assertEquals(999, consoleResult.expectSuccessValue().a)
        assertEquals(5, playerResult.expectSuccessValue().a)
    }

    @Test
    fun `require on ValueFlag gives denied sender the flag default`() = structureTest<String> {
        val flag = require(requirement { sender == "correct" }) { valueFlag("n", intParameter("n"), 7) }
        assertEquals(7, flag.default(testExecutionSender("incorrect")).expectSuccessValue())
    }

    @Test
    fun `require on ValueFlag takes contextual denied default`() = structureTest<String> {
        // KNOWN LIMITATION: a lambda literal passed as `default` doesn't resolve on Kotlin 2.4 beside the trailing
        // element lambda ("Unresolved reference 'sender'"), so it has to be a function-typed value.
        // TODO: fix
        val senderLength: ContextualValue<TestEnv, String, Int> = { success(sender.length) }
        val flag = require(requirement { sender == "correct" }, default = senderLength) {
            valueFlag("n", intParameter("n"), 0)
        }

        assertEquals(9, flag.default(testExecutionSender("incorrect")).expectSuccessValue())
    }

    @Test
    fun `require on HybridFlag gives denied sender Absent`() = structureTest<String> {
        val flag = require(requirement { sender == "correct" }) { hybridFlag("n", intParameter("n")) }
        assertIs<HybridFlagResult.Absent<Int>>(flag.default(testExecutionSender("incorrect")).expectSuccessValue())
    }

    @Test
    fun `require on HybridFlag gives denied sender denied default`() = structureTest<String> {
        val flag = require(requirement { sender == "correct" }, default = HybridFlagResult.Present()) {
            hybridFlag("n", intParameter("n"))
        }
        assertIs<HybridFlagResult.Present<Int>>(flag.default(testExecutionSender("incorrect")).expectSuccessValue())
    }

    @Test
    fun `requireSender on HybridFlag gives denied sender Absent`() = structureTest<BaseSender> {
        val flag = requireSender(Player::class) { hybridFlag("n", intParameter("n")) }
        val result = flag.default(testExecutionSender(BaseSender("console")))
        assertIs<HybridFlagResult.Absent<Int>>(result.expectSuccessValue())
    }

    @Test
    fun `require on OptionalParameter gives denied sender optional default`() = structureTest<String> {
        val optional = require(requirement { sender == "correct" }) { optionally(intParameter(""), 7) }

        assertEquals(7, withExecutionSender("incorrect") { optional.parse([]) }.expectSuccessValue())
        assertSame(
            Reason.InvalidSender,
            withExecutionSender("incorrect", "5") { optional.parse(["5"]) }.expectReason(),
        )
    }

    @Test
    fun `require on OptionalParameter resolves defaults by sender validity`() = structureTest<String> {
        val optional = require(requirement { sender == "correct" }, default = -1) {
            optionally(intParameter(""), 0)
        }

        assertEquals(0, withExecutionSender("correct") { optional.parse([]) }.expectSuccessValue())
        assertEquals(99, withExecutionSender("correct", "99") { optional.parse(["99"]) }.expectSuccessValue())

        assertEquals(-1, withExecutionSender("incorrect") { optional.parse([]) }.expectSuccessValue())
        assertSame(
            Reason.InvalidSender,
            withExecutionSender("incorrect", "5") { optional.parse(["5"]) }.expectReason(),
        )
    }

    @Test
    fun `require on nullable OptionalParameter resolves defaults by sender validity`() = structureTest<String> {
        val optional = require(requirement { sender == "correct" }, default = null) {
            optionally(intParameter(""), null)
        }

        assertNull(withExecutionSender("correct") { optional.parse([]) }.expectSuccessValue())
        assertEquals(1, withExecutionSender("correct", "1") { optional.parse(["1"]) }.expectSuccessValue())

        assertNull(withExecutionSender("incorrect") { optional.parse([]) }.expectSuccessValue())
        assertSame(
            Reason.InvalidSender,
            withExecutionSender("incorrect", "5") { optional.parse(["5"]) }.expectReason(),
        )
    }

    @Test
    fun `require on OptionalGroup resolves defaults by sender validity`() = structureTest<String> {
        val optional = require(requirement { sender == "correct" }, default = GroupResult.ResultA("invalid")) {
            optionally(group(literalParameter("on")), GroupResult.ResultA("absent"))
        }

        assertEquals(
            GroupResult.ResultA("absent"),
            withExecutionSender("correct") { optional.parse([]) }.expectSuccessValue(),
        )
        assertEquals(
            GroupResult.ResultA("on"),
            withExecutionSender("correct", "on") { optional.parse(["on"]) }.expectSuccessValue(),
        )

        assertEquals(
            GroupResult.ResultA("invalid"),
            withExecutionSender("incorrect") { optional.parse([]) }.expectSuccessValue(),
        )
        assertSame(
            Reason.InvalidSender,
            withExecutionSender("incorrect", "on") { optional.parse(["on"]) }.expectReason(),
        )
    }

    @Test
    fun `requireSender on OptionalParameter falls back to denied default`() = structureTest<BaseSender> {
        val optional = requireSender(Player::class, default = "console") {
            optionally(stringParameter(""), { success(sender.name) })
        }
        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        assertEquals("steve", withExecutionSender(player) { optional.parse([]) }.expectSuccessValue())
        assertEquals("bob", withExecutionSender(player, "bob") { optional.parse(["bob"]) }.expectSuccessValue())

        assertEquals("console", withExecutionSender(console) { optional.parse([]) }.expectSuccessValue())
        assertEquals(
            Reason.InvalidSenderType(Player::class),
            withExecutionSender(console, "bob") { optional.parse(["bob"]) }.expectReason(),
        )
    }

    @Test
    fun `requireSender on OptionalGroup falls back to denied default`() = structureTest<BaseSender> {
        val optional = requireSender(Player::class, default = GroupResult.ResultA("console")) {
            optionally(group(literalParameter("on")), { success(GroupResult.ResultA(sender.name)) })
        }
        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        assertEquals(
            GroupResult.ResultA("steve"),
            withExecutionSender(player) { optional.parse([]) }.expectSuccessValue(),
        )
        assertEquals(
            GroupResult.ResultA("on"),
            withExecutionSender(player, "on") { optional.parse(["on"]) }.expectSuccessValue(),
        )

        assertEquals(
            GroupResult.ResultA("console"),
            withExecutionSender(console) { optional.parse([]) }.expectSuccessValue(),
        )
        assertEquals(
            Reason.InvalidSenderType(Player::class),
            withExecutionSender(console, "on") { optional.parse(["on"]) }.expectReason(),
        )
    }

    private open class BaseSender(val name: String)

    private class Player(name: String) : BaseSender(name)
}
