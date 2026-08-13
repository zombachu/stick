package com.zombachu.stick

import com.zombachu.stick.dsl.id
import com.zombachu.stick.element.Group1Impl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.parameters.StringParameter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class InvocationImplTest {

    @Test
    fun `peek with too large size fails with InvalidSizeError`() {
        val inv = testInvocation("a")
        assertSame(PeekingResult.InvalidSizeError, inv.peek(Size(2)))
    }

    @Test
    fun `peek with Fixed size returns requested arguments`() {
        val inv = testInvocation("a", "b", "c")

        val peeked = inv.peek(Size(2))

        assertIs<PeekingResult.Success>(peeked)
        assertEquals(["a", "b"], peeked.value)
    }

    @Test
    fun `peek with non-Fixed size returns all args`() {
        val inv = testInvocation("a", "b", "c")

        val unbounded = inv.peek(Size.Unbounded)
        val deferred = inv.peek(Size.Deferred)

        assertIs<PeekingResult.Success>(unbounded)
        assertEquals(["a", "b", "c"], unbounded.value)
        assertIs<PeekingResult.Success>(deferred)
        assertEquals(["a", "b", "c"], deferred.value)
    }

    @Test
    fun `processElement consumes reported size`() {
        val inv = testInvocation("a", "b", "c")
        val parameter = StringParameter<TestEnv, Unit>("", "")

        val result = inv.processElement(parameter)

        assertEquals("a", result.expectSuccessValue())
        assertEquals(["b", "c"], inv.unparsed)
    }

    @Test
    fun `processElement consumes parameter size for Group`() {
        val inv = testInvocation("a", "b", "c")
        val group = Group1Impl<TestEnv, Unit, String>("", "", StringParameter("", ""))

        val result = inv.processElement(group)

        assertTrue(result.isSuccess())
        assertEquals(["b", "c"], inv.unparsed)
    }

    @Test
    fun `processElement fails when element over-consumes`() {
        val inv = testInvocation("a")
        val misbehavingParameter =
            object : Parameter.UnknownSize<TestEnv, Unit, String>(Size(1), "", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun parse(args: List<String>): CommandResult<String> = ParsingResult.success("a", Size(5))
            }

        val result = inv.processElement(misbehavingParameter)

        assertIs<ParsingResult.UnknownError>(result)
    }

    @Test
    fun `get put and getOrPut round-trip`() {
        val inv = testInvocation()
        val identifier = id<String>("name")

        assertEquals("default", inv.getOrPut(identifier, "default"))
        assertEquals("default", inv.get(identifier))

        inv.put(identifier, "updated")
        assertEquals("updated", inv.get(identifier))
        assertEquals("updated", inv.getOrPut(identifier, "ignored"))
    }

    @Test
    fun `forSender shares backing memory`() {
        val baseInvocation = testInvocationSender("base-sender", "a", "b")
        val identifier = id<String>("shared-identifier")
        baseInvocation.put(identifier, "from-base")

        val transformedInvocation = baseInvocation.forSender { it.length }

        transformedInvocation.unparsed.removeAt(0)
        assertEquals(["b"], baseInvocation.unparsed)
        assertEquals("from-base", transformedInvocation.get(identifier))

        transformedInvocation.put(identifier, "from-transformed")
        assertEquals("from-transformed", baseInvocation.get(identifier))
    }

    @Test
    fun `getSyntax prefixes with slash`() {
        val inv = testInvocation()
        assertTrue(inv.getSyntax().startsWith("/"))
    }
}
