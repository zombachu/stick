package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.LeadingParameterRole
import com.zombachu.stick.element.Parameter
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
    fun `requireIs in group skips alternative for wrong sender`() = structureTest<BaseSender> {
        val gated = requireIs(Player::class) { stringParameter("") }
        val grouped = group(gated, stringParameter("fallback"))

        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        val playerResult = withExecutionSender(player, "bob") { grouped.parse(["bob"]) }
        val consoleResult = withExecutionSender(console, "bob") { grouped.parse(["bob"]) }

        assertIs<GroupResult.ResultA<String>>(playerResult.expectSuccessValue())
        assertIs<GroupResult.ResultB<String>>(consoleResult.expectSuccessValue())
    }

    @Test
    fun `requireAs enforces given requirement`() = structureTest {
        val parameter: StructureScope<TestEnv, Unit>.() -> Parameter.Bounded<TestEnv, Unit, String> = {
            stringParameter("")
        }
        val allowed = requireAs({ _: Unit -> }, requirement { true }, parameter)
        val denied = requireAs({ _: Unit -> }, requirement { false }, parameter)

        assertTrue(withInvocation { allowed.validateSender() }.isSuccess())
        assertSame(Reason.InvalidSender, withInvocation { denied.validateSender() }.expectReason())
    }

    @Test
    fun `requireIs on ValueFlag falls back to invalidDefault`() = structureTest<BaseSender> {
        val gatedFlag = requireIs(Player::class, invalidDefault(999)) { valueFlag("n", 0, intParameter("n")) }
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
            optionally(default(0), intParameter(""))
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
            optionallyNullable(intParameter(""))
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
            optionally(default(GroupResult.ResultA("absent")), group(literalParameter("on")))
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
    fun `requireAs on OptionalParameter parses with transformed sender`() = structureTest<String> {
        val transform: (String) -> Int = String::length
        val optional = requireAs(transform, invalidDefault(-1, requirement { sender != "" })) {
            optionally(default({ success(sender) }), intParameter(""))
        }

        assertEquals(8, withExecutionSender("zombachu") { optional.parse([]) }.expectSuccessValue())
        assertEquals(-1, withExecutionSender("") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `requireIs on OptionalParameter falls back to invalidDefault`() = structureTest<BaseSender> {
        val optional = requireIs(Player::class, invalidDefault("console")) {
            optionally(default({ success(sender.name) }), stringParameter(""))
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
    fun `requireIs on OptionalGroup falls back to invalidDefault`() = structureTest<BaseSender> {
        val optional = requireIs(Player::class, invalidDefault(GroupResult.ResultA("console"))) {
            optionally(default({ success(GroupResult.ResultA(sender.name)) }), group(literalParameter("on")))
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

    @Test
    fun `composing a requirement does not mutate it`() = structureTest<BaseSender> {
        val shared = requirement { true }
        val console = BaseSender("console")

        val narrowed = requireIs(Player::class, shared) { stringParameter("") }

        assertTrue(withInvocation(console) { shared.validateSender() }.isSuccess())
        assertEquals(
            Reason.InvalidSenderType(Player::class),
            withInvocation(console) { narrowed.validateSender() }.expectReason(),
        )
    }

    private open class BaseSender(val name: String)

    private class Player(name: String) : BaseSender(name)
}
