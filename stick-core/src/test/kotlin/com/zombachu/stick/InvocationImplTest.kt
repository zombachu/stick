package com.zombachu.stick

import com.zombachu.stick.dsl.id
import com.zombachu.stick.element.Group1Impl
import com.zombachu.stick.element.OptionalParameterImpl
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.PipelineOperation
import com.zombachu.stick.element.PipelinedOptionalParameter
import com.zombachu.stick.element.StoredParameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.feedback.Feedback
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class InvocationImplTest {

    @Test
    fun `peek with too large size fails with InvalidSizeError`() {
        val inv = testInvocation("a")
        assertSame(PeekingResult.InvalidSizeError, inv.peek(Size(2)))
        assertSame(PeekingResult.InvalidSizeError, inv.peek(Size.between(2, 4)))
        assertSame(PeekingResult.InvalidSizeError, inv.peek(Size.atLeast(2)))
    }

    @Test
    fun `peek with fixed size returns requested arguments`() {
        val inv = testInvocation("a", "b", "c")

        val peeked = inv.peek(Size(2))

        assertIs<PeekingResult.Success>(peeked)
        assertEquals(["a", "b"], peeked.value)
    }

    @Test
    fun `peek with unbounded size returns all args`() {
        val inv = testInvocation("a", "b", "c")

        val peeked = inv.peek(Size.atLeast(0))

        assertIs<PeekingResult.Success>(peeked)
        assertEquals(["a", "b", "c"], peeked.value)
    }

    @Test
    fun `peek with bounded size returns at most max args`() {
        val inv = testInvocation("a", "b", "c")

        val smallPeek = inv.peek(Size.between(0, 2))
        assertIs<PeekingResult.Success>(smallPeek)
        assertEquals(["a", "b"], smallPeek.value)

        val largePeek = inv.peek(Size.between(0, 5))
        assertIs<PeekingResult.Success>(largePeek)
        assertEquals(["a", "b", "c"], largePeek.value)
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
        val group = Group1Impl<TestEnv, Unit, String, Position.Leading>("", "", StringParameter("", ""))

        val result = inv.processElement(group)

        assertTrue(result.isSuccess())
        assertEquals(["b", "c"], inv.unparsed)
    }

    @Test
    fun `processElement does not parse non-matching element`() {
        val inv = testInvocation("foo")
        var parsed = false
        val parameter =
            object : Parameter.Size1<TestEnv, Unit, String>("bar", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(arg0: String): MatchResult = MatchResult.unmatched()

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(arg0: String): CommandResult<String> {
                    parsed = true
                    return ParsingResult.success(arg0)
                }
            }

        val result = inv.processElement(parameter)

        assertSame(ParsingResult.TypeNotMatchedInternal, result)
        assertFalse(parsed)
        assertEquals(["foo"], inv.unparsed)
    }

    @Test
    fun `processElement returns failure Unmatched carries`() {
        val inv = testInvocation("foo")
        val parameter = LiteralParameter<TestEnv, Unit>("bar", [], "")

        val result = inv.processElement(parameter)

        assertEquals(Feedback.LiteralNotMatched(["bar"], "foo"), result.expectFailure().feedback)
    }

    @Test
    fun `processElement fails partial element with InvalidSizeError`() {
        val inv = testInvocation("a")
        val parameter =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size.between(0, 2), "", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(args: List<String>): CommandResult<String> = ParsingResult.failSize()
            }

        val result = inv.processElement(parameter)

        assertSame(PeekingResult.InvalidSizeError, result)
    }

    @Test
    fun `consumedArgs counts consumed arg`() {
        val inv = testInvocation("a", "b", "c")
        val parameter = StringParameter<TestEnv, Unit>("", "")

        val first = inv.processElement(parameter)
        val second = inv.processElement(parameter)

        assertTrue(first.isSuccess() && second.isSuccess())
        assertEquals(2, inv.consumedArgs)
    }

    @Test
    fun `forSender shares consumedArgs`() {
        val inv = testInvocation("a")
        val transformed = inv.forSender { 1 }

        val result = transformed.processElement(StringParameter<TestEnv, Int>("", ""))

        assertTrue(result.isSuccess())
        assertEquals(1, inv.consumedArgs)
    }

    @Test
    fun `processElement resolves derived parameter once`() {
        var resolves = 0
        val parameter = countingParameter { resolves++ }

        val result = testInvocation("a").processElement(parameter)

        assertEquals("a", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement resolves through decorator once`() {
        var resolves = 0
        val identifier = id<String>("stored")
        val stored = StoredParameter(countingParameter { resolves++ }, identifier)

        val inv = testInvocation("a")
        val result = inv.processElement(stored)

        assertEquals("a", result.expectSuccessValue())
        assertEquals("a", inv.get(identifier))
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement resolves through wrapper chain once`() {
        var resolves = 0
        val optional =
            OptionalParameterImpl<TestEnv, Unit, String, Position.Optional>(
                invalidSenderDefault("invalid"),
                validSenderDefault("absent"),
                countingParameter { resolves++ },
            )
        val exclaim: PipelineOperation<TestEnv, Unit, String, String> = { ParsingResult.success("$it!") }
        val piped = PipelinedOptionalParameter<TestEnv, Unit, String, String, Position.Optional>(optional, [exclaim])

        val result = testInvocation("a").processElement(piped)

        assertEquals("a!", result.expectSuccessValue())
        assertEquals(1, resolves)
    }

    @Test
    fun `processElement fails when element over-consumes`() {
        val inv = testInvocation("a")
        val misbehavingParameter =
            object : Parameter.Bounded<TestEnv, Unit, String>(Size(1), "", "") {
                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun match(args: List<String>): MatchResult = MatchResult.matched(1)

                context(validationContext: ValidationContext<TestEnv, Unit>)
                override fun resolve(args: List<String>): CommandResult<String> = ParsingResult.success("a", 5)
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

    private fun countingParameter(onResolve: () -> Unit) =
        object : Parameter.Size1<TestEnv, Unit, String>("", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> {
                onResolve()
                return ParsingResult.success(arg0)
            }
        }
}
