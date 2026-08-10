package com.zombachu.stick.structure

import com.zombachu.stick.GroupResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Signature1
import com.zombachu.stick.element.validateSender
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.withInvocationSender
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequiresTest {

    @Test
    fun `requireIs in group skips alternative for wrong sender`() = structureTest<BaseSender> {
        val gated = requireIs(Player::class) { stringParameter("") }
        val grouped = group(gated, stringParameter("fallback"))

        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        val playerResult = withInvocationSender(player, "bob") { grouped.parse(["bob"]) }
        val consoleResult = withInvocationSender(console, "bob") { grouped.parse(["bob"]) }

        assertIs<GroupResult.ResultA<String>>(playerResult.expectSuccessValue())
        assertIs<GroupResult.ResultB<String>>(consoleResult.expectSuccessValue())
    }

    @Test
    fun `requireAs enforces given requirement`() = structureTest {
        val parameter: StructureScope<TestEnv, Unit>.() -> Parameter.FixedSize<TestEnv, Unit, String> = {
            stringParameter("")
        }
        val allowed = requireAs({ _: Unit -> }, requirement { true }, parameter)
        val denied = requireAs({ _: Unit -> }, requirement { false }, parameter)

        assertTrue(withValidationContext { allowed.validateSender() }.isSuccess())
        assertSame(Feedback.InvalidSender, withValidationContext { denied.validateSender() }.expectFailure().feedback)
    }

    @Test
    fun `requireIs on ValueFlag falls back to invalidDefault`() = structureTest<BaseSender> {
        val gatedFlag = requireIs(Player::class, invalidDefault(999)) { valueFlag("n", 0, intParameter("n")) }
        val signature = Signature1<TestEnv, BaseSender, Int>({}, [gatedFlag])

        val player: BaseSender = Player("steve")
        val console = BaseSender("console")

        val consoleResult = withInvocationSender(console) { signature.execute() }
        val playerResult = withInvocationSender(player, "-n", "5") { signature.execute() }

        assertEquals(999, consoleResult.expectSuccessValue().a)
        assertEquals(5, playerResult.expectSuccessValue().a)
    }

    private open class BaseSender(val name: String)

    private class Player(name: String) : BaseSender(name)
}
