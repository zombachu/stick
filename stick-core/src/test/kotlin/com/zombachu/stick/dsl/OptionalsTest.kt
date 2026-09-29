package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parse
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.testExecutionSender
import com.zombachu.stick.withExecution
import com.zombachu.stick.withExecutionSender
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class OptionalsTest {

    private val intParameter = IntParameter<TestEnv, String>("", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `defaultSender requires and casts sender type`() = structureTest<Any> {
        val sender = defaultSender<TestEnv, Any, String>()
        val stringSender: Any = "hello"
        val intSender: Any = 42

        val stringSenderResult = withInvocation(stringSender) { sender.validateSender() }
        assertTrue(stringSenderResult.isSuccess())
        assertEquals("hello", sender.value(testExecutionSender(stringSender)).expectSuccessValue())

        val intSenderResult = withInvocation(intSender) { sender.validateSender() }
        assertSame(Reason.InvalidSender, intSenderResult.expectReason())
    }

    @Test
    fun `optionally defaults to value`() = structureTest<String> {
        val optional = optionally(intParameter, 7)
        assertEquals(7, withExecutionSender("sender") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `optionally defaults to contextual value`() = structureTest<String> {
        val optional = optionally(intParameter, { success(sender.length) })
        assertEquals(6, withExecutionSender("sender") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `optionally defaults to null`() = structureTest<String> {
        val optional = optionally(intParameter, null)
        assertNull(withExecutionSender("sender") { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `optionals parse in order`() = structureTest {
        val structure =
            command("cmd")(
                optionals(
                    optionally(intParameter("a"), null),
                    optionally(stringParameter("b"), null)
                )
            ) { (a: Int?, b: String?) -> }

        val none = withExecution("cmd") { structure.parse(["cmd"]) }.expectSuccessValue()
        val first = withExecution("cmd", "5") { structure.parse(["cmd", "5"]) }.expectSuccessValue()
        val both = withExecution("cmd", "5", "x") { structure.parse(["cmd", "5", "x"]) }.expectSuccessValue()

        assertEquals([null, null], [none.a.a, none.a.b])
        assertEquals([5, null], [first.a.a, first.a.b])
        assertEquals([5, "x"], [both.a.a, both.a.b])
    }

    @Test
    fun `optionals require previous optionals to be specified`() = structureTest {
        val structure =
            command("cmd")(
                optionals(
                    optionally(intParameter("a"), null),
                    optionally(textParameter("b"), "")
                )
            )

        val result = withExecution("cmd", "hello", "world") { structure.parse(["cmd", "hello", "world"]) }

        assertIs<Reason.TypeNotMatched>(result.expectReason())
    }

    @Test
    fun `flag in optionals matches anywhere`() = structureTest {
        val structure =
            command("cmd")(
                stringParameter("a"),
                optionals(
                    optionally(intParameter("b"), null),
                    flag("silent")
                ),
            ) { a, (b, silent) -> }

        val none = withExecution("cmd", "hello") { structure.parse(["cmd",  "hello"]) }.expectSuccessValue()
        val before =
            withExecution("cmd", "-silent", "hello") { structure.parse(["cmd", "-silent", "hello"]) }
                .expectSuccessValue()
        val after =
            withExecution("cmd", "hello", "-silent") { structure.parse(["cmd", "hello", "-silent"]) }
                .expectSuccessValue()

        assertEquals([null, false], [none.b.a, none.b.b])
        assertEquals([null, true], [before.b.a, before.b.b])
        assertEquals([null, true], [after.b.a, after.b.b])
    }

    @Test
    fun `flag in optionals does not consume linear args`() = structureTest {
        val structure =
            command("cmd")(
                optionals(
                    flag("silent"),
                    optionally(intParameter("a"), null)
                ),
            ) { (silent, a) -> }

        val args = withExecution("cmd", "5") { structure.parse(["cmd", "5"]) }.expectSuccessValue()

        assertEquals([false, 5], [args.a.a, args.a.b])
    }

    @Test
    fun `optionals syntax renders flags together`() = structureTest {
        val structure =
            command("cmd")(
                flag("raw"),
                stringParameter("a"),
                optionals(
                    optionally(intParameter("b"), null),
                    flag("silent"),
                    optionally(textParameter("c"), ""),
                ),
            ) { raw, a, (b, silent, d) ->
            }

        val syntax = withInvocation { structure.getSyntax() }

        assertEquals("cmd <a> [b] [-raw] [-silent] [c]", syntax)
    }

    @Test
    fun `optionally group defaults`() = structureTest {
        val optional = optionally(group(literalParameter("on")), GroupResult.ResultA("off"))
        assertEquals(GroupResult.ResultA("off"), withExecution { optional.parse([]) }.expectSuccessValue())
    }

    @Test
    fun `optionally group defaults to null`() = structureTest {
        val structure =
            command("cmd")(
                optionals(
                    optionally(group(literalParameter("on"), literalParameter("off")), null),
                    optionally(stringParameter("reason"), null)
                )
            ) { (toggle: GroupResult2<String, String>?, reason: String?) -> }

        val none = withExecution("cmd") { structure.parse(["cmd"]) }.expectSuccessValue()
        val both = withExecution("cmd", "off", "someReason") { structure.parse(["cmd", "off", "someReason"]) }.expectSuccessValue()

        assertNull(none.a.a)
        assertIs<GroupResult.ResultB<String>>(both.a.a)
        assertEquals("someReason", both.a.b)
    }

    @Test
    fun `optional group has correct syntax`() = structureTest {
        val structure =
            command("cmd")(
                stringParameter("a"),
                optionally(group(literalParameter("on"), literalParameter("off")), null),
            ) { a, toggle -> }

        val syntax = withInvocation { structure.getSyntax() }

        assertEquals("cmd <a> [on|off]", syntax)
    }
}
