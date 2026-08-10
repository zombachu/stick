package com.zombachu.stick.element

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectFailure
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.feedback.Feedback
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.presenceFlagParameter
import com.zombachu.stick.withInvocation
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SignatureTest {

    private val amount = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `linear elements fill values in declared order`() {
        val name = StringParameter<TestEnv, Unit>("", "")
        val signature = Signature2<TestEnv, Unit, Int, String>({ a, b -> }, [amount, name])

        val args = withInvocation("5", "bob") { signature.execute() }.expectSuccessValue()

        assertEquals([5, "bob"], [args.a, args.b])
    }

    @Test
    fun `flag parses before linear element`() {
        val signature = Signature2<TestEnv, Unit, Int, Boolean>({ a, loud -> }, [amount, loudFlag()])

        val args = withInvocation("-loud", "5") { signature.execute() }.expectSuccessValue()

        assertEquals([5, true], [args.a, args.b])
    }

    @Test
    fun `flag parses after linear element`() {
        val signature = Signature2<TestEnv, Unit, Int, Boolean>({ a, loud -> }, [amount, loudFlag()])

        val args = withInvocation("5", "-loud") { signature.execute() }.expectSuccessValue()

        assertEquals([5, true], [args.a, args.b])
    }

    @Test
    fun `absent flag parses default value`() {
        val signature = Signature2<TestEnv, Unit, Int, Boolean>({ a, loud -> }, [amount, loudFlag()])

        val args = withInvocation("5") { signature.execute() }.expectSuccessValue()

        assertEquals([5, false], [args.a, args.b])
    }

    @Test
    fun `inaccessible flag parses invalidDefault value`() {
        val base =
            ValueFlagImpl("loud", { ParsingResult.success(false) }, presenceFlagParameter<TestEnv, String, Boolean>("loud", true))
        val invalidDefault = invalidSenderDefault<TestEnv, Unit, Boolean>(true) { SenderValidationResult.failSender() }
        val gatedFlag = TransformedValueFlag(base, { _: Unit -> "x" }, invalidDefault)
        val signature = Signature1<TestEnv, Unit, Boolean>({ loud -> }, [gatedFlag])

        val args = withInvocation { signature.execute() }.expectSuccessValue()

        assertEquals(true, args.a)
    }

    @Test
    fun `InvalidSizeError fails with InvalidSyntax`() {
        val signature = Signature1<TestEnv, Unit, Int>({}, [amount])

        val result = withInvocation { signature.execute() }

        assertIs<Feedback.InvalidSyntax>(result.expectFailure().feedback)
    }

    @Test
    fun `flag parsing error propagates`() {
        val flagParameter = FlagParameter.ParameterFlagParameter(amount, [])
        val flag = ValueFlagImpl("amount", { ParsingResult.success(0) }, flagParameter)
        val signature = Signature1<TestEnv, Unit, Int>({}, [flag])

        val result = withInvocation("-amount", "not-a-number") { signature.execute() }

        assertEquals(Feedback.TypeNotMatched("integer", "not-a-number"), result.expectFailure().feedback)
    }

    @Test
    fun `leftover args fail with InvalidSyntax`() {
        val name = StringParameter<TestEnv, Unit>("", "")
        val signature = Signature1<TestEnv, Unit, String>({}, [name])

        val result = withInvocation("bob", "extra") { signature.execute() }

        assertIs<Feedback.InvalidSyntax>(result.expectFailure().feedback)
    }

    @Test
    fun `getSyntax places flags after linear elements but before terminating element`() {
        val str = StringParameter<TestEnv, Unit>("str", "")
        val text = TextParameter<TestEnv, Unit>("text", "")
        val signature = Signature3<TestEnv, Unit, String, Boolean, String>({ _, _, _ -> }, [loudFlag(), str, text])

        val syntax = withValidationContext { signature.getSyntax() }

        assertEquals("<str> [-loud] <text>", syntax)
    }

    private fun loudFlag(): ValueFlagImpl<TestEnv, Unit, Boolean> =
        ValueFlagImpl("loud", { ParsingResult.success(false) }, presenceFlagParameter("loud", true))
}
