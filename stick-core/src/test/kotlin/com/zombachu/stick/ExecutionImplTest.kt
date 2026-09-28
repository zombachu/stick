package com.zombachu.stick

import com.zombachu.stick.dsl.id
import com.zombachu.stick.dsl.store
import com.zombachu.stick.element.Group1Impl
import com.zombachu.stick.element.MappedOptionalParameter
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.failure.Reason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExecutionImplTest {

    @Test
    fun `peek with too large size returns null`() {
        val ex = testExecution("a")
        assertNull(ex.peek(Size(2)))
        assertNull(ex.peek(Size.between(2, 4)))
        assertNull(ex.peek(Size.atLeast(2)))
    }

    @Test
    fun `peek with fixed size returns requested arguments`() {
        val ex = testExecution("a", "b", "c")

        val peeked = ex.peek(Size(2))

        assertEquals(["a", "b"], peeked)
    }

    @Test
    fun `peek with unbounded size returns all args`() {
        val ex = testExecution("a", "b", "c")

        val peeked = ex.peek(Size.atLeast(0))

        assertEquals(["a", "b", "c"], peeked)
    }

    @Test
    fun `peek with bounded size returns at most max args`() {
        val ex = testExecution("a", "b", "c")

        val smallPeek = ex.peek(Size.between(0, 2))
        assertEquals(["a", "b"], smallPeek)

        val largePeek = ex.peek(Size.between(0, 5))
        assertEquals(["a", "b", "c"], largePeek)
    }

    @Test
    fun `processElement consumes reported size`() {
        val ex = testExecution("a", "b", "c")
        val parameter = StringParameter<TestEnv, Unit>("", "")

        val result = ex.processElement(parameter)

        assertEquals("a", result.expectSuccessValue())
        assertEquals(["b", "c"], ex.unparsed)
    }

    @Test
    fun `processElement consumes parameter size for Group`() {
        val ex = testExecution("a", "b", "c")
        val group = Group1Impl<TestEnv, Unit, String, Position.Leading>("", "", StringParameter("", ""))

        val result = ex.processElement(group)

        assertTrue(result.isSuccess())
        assertEquals(["b", "c"], ex.unparsed)
    }

    @Test
    fun `processElement does not parse non-matching element`() {
        val ex = testExecution("foo")
        var parsed = false
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("bar", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.unmatched()

                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    parsed = true
                    return success(arg0)
                }
            }

        val result = ex.processElement(parameter)

        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
        assertFalse(parsed)
        assertEquals(["foo"], ex.unparsed)
    }

    @Test
    fun `processElement returns failure Unmatched carries`() {
        val ex = testExecution("foo")
        val parameter = LiteralParameter<TestEnv, Unit>("bar", [], "")

        val result = ex.processElement(parameter)

        assertEquals(Reason.LiteralNotMatched(["bar"], "foo"), result.expectReason())
    }

    @Test
    fun `processElement associates element with failure`() {
        val ex = testExecution("foo")
        val parameter = LiteralParameter<TestEnv, Unit>("bar", [], "")

        val result = ex.processElement(parameter)

        val origin = result.expectNoMatch().origin
        assertEquals("bar", origin.elementName)
        assertEquals(ex.getSyntax(), origin.usage)
    }

    @Test
    fun `processElement fails partial element with InvalidSyntax NoMatch`() {
        val ex = testExecution("a")
        val parameter =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(0, 2), "", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun match(args: List<String>): MatchResult = MatchResult.partial()

                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> =
                    success("").consuming(1)
            }

        val result = ex.processElement(parameter)

        assertIs<Reason.InvalidSyntax>(result.expectNoMatch().reason)
    }

    @Test
    fun `consumedArgs counts consumed arg`() {
        val ex = testExecution("a", "b", "c")
        val parameter = StringParameter<TestEnv, Unit>("", "")

        val first = ex.processElement(parameter)
        val second = ex.processElement(parameter)

        assertTrue(first.isSuccess() && second.isSuccess())
        assertEquals(2, ex.consumedArgs)
    }

    @Test
    fun `forSender shares consumedArgs`() {
        val ex = testExecution("a")
        val transformed = ex.forSender { 1 }

        val result = transformed.processElement(StringParameter<TestEnv, Int>("", ""))

        assertTrue(result.isSuccess())
        assertEquals(1, ex.consumedArgs)
    }

    @Test
    fun `processElement resolves derived parameter once`() {
        var resolves = 0
        val parameter = countingParameter { resolves++ }

        val result = testExecution("a").processElement(parameter)

        assertEquals("a", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement resolves through decorator once`() {
        var resolves = 0
        val identifier = id<String>("stored")
        val stored = countingParameter { resolves++ }.store(identifier)

        val ex = testExecution("a")
        val result = ex.processElement(stored)

        assertEquals("a", result.expectSuccessValue())
        assertEquals("a", ex.get(identifier))
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement resolves through wrapper chain once`() {
        var resolves = 0
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                validSenderDefault("absent"),
                countingParameter { resolves++ },
            )
        val mapped =
            MappedOptionalParameter<TestEnv, Unit, String, String, Position.Optional>(optional) { success("$it!") }

        val result = testExecution("a").processElement(mapped)

        assertEquals("a!", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement fails when element claims less than its declared size`() {
        val ex = testExecution("a", "b")
        val misbehavingParameter =
            object : Parameter.Unbounded<TestEnv, Unit, String>(Size.atLeast(1), "", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> =
                    success(args.joinToString(" ")).consuming(0)
            }

        val result = ex.processElement(misbehavingParameter)

        assertIs<Reason.Unknown>(result.expectError().reason)
        assertEquals(0, ex.consumedArgs)
    }

    @Test
    fun `processElement fails when element over-consumes`() {
        val ex = testExecution("a")
        val misbehavingParameter =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size(1), "", "") {
                context(inv: Invocation<TestEnv, Unit>)
                override fun match(args: List<String>): MatchResult = MatchResult.matchedExactly(1)

                context(inv: Invocation<TestEnv, Unit>)
                override fun resolve(args: List<String>): ConsumingResult<String> = success("a").consuming(5)
            }

        val result = ex.processElement(misbehavingParameter)

        assertIs<Reason.Unknown>(result.expectError().reason)
    }

    @Test
    fun `get put and getOrPut round-trip`() {
        val ex = testExecution()
        val identifier = id<String>("name")

        assertEquals("default", ex.getOrPut(identifier, "default"))
        assertEquals("default", ex.get(identifier))

        ex.put(identifier, "updated")
        assertEquals("updated", ex.get(identifier))
        assertEquals("updated", ex.getOrPut(identifier, "ignored"))
    }

    @Test
    fun `forSender shares backing memory`() {
        val baseExecution = testExecutionSender("base-sender", "a", "b")
        val identifier = id<String>("shared-identifier")
        baseExecution.put(identifier, "from-base")

        val transformedExecution = baseExecution.forSender { it.length }

        transformedExecution.unparsed.removeAt(0)
        assertEquals(["b"], baseExecution.unparsed)
        assertEquals("from-base", transformedExecution.get(identifier))

        transformedExecution.put(identifier, "from-transformed")
        assertEquals("from-transformed", baseExecution.get(identifier))
    }

    @Test
    fun `getSyntax prefixes with slash`() {
        val ex = testExecution()
        assertTrue(ex.getSyntax().startsWith("/"))
    }

    private fun countingParameter(onResolve: () -> Unit) =
        object : Parameter.Size1<TestEnv, Unit, String>("", "") {
            context(inv: Invocation<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> {
                onResolve()
                return success(arg0)
            }
        }
}
