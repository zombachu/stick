package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
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
    fun `requireSender on ValueFlag falls back to invalidDefault`() = structureTest<BaseSender> {
        val gatedFlag = requireSender(Player::class, invalidDefault(999)) { valueFlag("n", intParameter("n"), 0) }
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
    fun `require on OptionalParameter resolves defaults by sender validity`() = structureTest<String> {
        val optional = require(invalidDefault(-1, requirement { sender == "correct" })) {
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
        val optional = require(invalidDefault(null, requirement { sender == "correct" })) {
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
        val optional = require(invalidDefault(GroupResult.ResultA("invalid"), requirement { sender == "correct" })) {
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
    fun `requireSender on OptionalParameter falls back to invalidDefault`() = structureTest<BaseSender> {
        val optional = requireSender(Player::class, invalidDefault("console")) {
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
    fun `requireSender on OptionalGroup falls back to invalidDefault`() = structureTest<BaseSender> {
        val optional = requireSender(Player::class, invalidDefault(GroupResult.ResultA("console"))) {
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
