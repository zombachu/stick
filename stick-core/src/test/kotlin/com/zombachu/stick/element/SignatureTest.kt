package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.LiteralParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectReason
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.failSender
import com.zombachu.stick.failure.Reason
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.noMatch
import com.zombachu.stick.presenceValueFlag
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SignatureTest {

    private val label = LiteralParameter<TestEnv, Unit>("cmd", [], "")
    private val amount = IntParameter<TestEnv, Unit>("amount", "", Int.MIN_VALUE, Int.MAX_VALUE)

    @Test
    fun `linear elements fill values in declared order`() {
        val name = StringParameter<TestEnv, Unit>("", "")
        val signature =
            Signature2<TestEnv, Unit, Int, String>({ a, b -> }, LeadingParameterRole.Label, [label, amount, name])

        val args = withExecution("cmd", "5", "bob") { signature.execute() }.expectSuccessValue()

        assertEquals([5, "bob"], [args.a, args.b])
    }

    @Test
    fun `flag parses before linear element`() {
        val signature =
            Signature2<TestEnv, Unit, Int, Boolean>(
                { a, loud -> },
                LeadingParameterRole.Label,
                [label, amount, loudFlag()],
            )

        val args = withExecution("cmd", "-loud", "5") { signature.execute() }.expectSuccessValue()

        assertEquals([5, true], [args.a, args.b])
    }

    @Test
    fun `flag parses after linear element`() {
        val signature =
            Signature2<TestEnv, Unit, Int, Boolean>(
                { a, loud -> },
                LeadingParameterRole.Label,
                [label, amount, loudFlag()],
            )

        val args = withExecution("cmd", "5", "-loud") { signature.execute() }.expectSuccessValue()

        assertEquals([5, true], [args.a, args.b])
    }

    @Test
    fun `absent flag parses default value`() {
        val signature =
            Signature2<TestEnv, Unit, Int, Boolean>(
                { a, loud -> },
                LeadingParameterRole.Label,
                [label, amount, loudFlag()],
            )

        val args = withExecution("cmd", "5") { signature.execute() }.expectSuccessValue()

        assertEquals([5, false], [args.a, args.b])
    }

    @Test
    fun `inaccessible flag parses invalidDefault value`() {
        val base = presenceValueFlag<TestEnv, String, Boolean>("loud", false, true)
        val invalidDefault = invalidSenderDefault<TestEnv, Unit, Boolean>(true) { failSender() }
        val gatedFlag = TransformedValueFlag(base, { _: Unit -> "x" }, invalidDefault)
        val signature = Signature1<TestEnv, Unit, Boolean>({ loud -> }, LeadingParameterRole.Label, [label, gatedFlag])

        val args = withExecution("cmd") { signature.execute() }.expectSuccessValue()

        assertEquals(true, args.a)
    }

    @Test
    fun `missing linear arg fails with InvalidSyntax`() {
        val signature = Signature1<TestEnv, Unit, Int>({}, LeadingParameterRole.Label, [label, amount])

        val result = withExecution("cmd") { signature.execute() }

        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `default mismatch fails with InvalidSyntax`() {
        class SilentParameter : Parameter.Size1<TestEnv, Unit, String>("", "") {
            context(validationContext: ValidationContext<TestEnv, Unit>)
            override fun resolve(arg0: String): CommandResult<String> = noMatch()
        }
        val signature = Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [label, SilentParameter()])

        val result = withExecution("cmd", "other") { signature.execute() }

        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `flag parsing error propagates`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("amount", amount, [])
        val flag = ValueFlagImpl("amount", { success(0) }, flagParameter)
        val signature = Signature1<TestEnv, Unit, Int>({}, LeadingParameterRole.Label, [label, flag])

        val result = withExecution("cmd", "-amount", "not-a-number") { signature.execute() }

        assertEquals(Reason.TypeNotMatched("integer", "not-a-number"), result.expectReason())
    }

    @Test
    fun `leftover args fail with InvalidSyntax`() {
        val name = StringParameter<TestEnv, Unit>("", "")
        val signature = Signature1<TestEnv, Unit, String>({}, LeadingParameterRole.Label, [label, name])

        val result = withExecution("cmd", "bob", "extra") { signature.execute() }

        assertIs<Reason.InvalidSyntax>(result.expectReason())
    }

    @Test
    fun `getSyntax places flags after linear elements but before terminating element`() {
        val str = StringParameter<TestEnv, Unit>("str", "")
        val text = TextParameter<TestEnv, Unit>("text", "")
        val signature =
            Signature3<TestEnv, Unit, String, Boolean, String>(
                { _, _, _ -> },
                LeadingParameterRole.Label,
                [label, loudFlag(), str, text],
            )

        val syntax = withValidationContext { signature.getSyntax() }

        assertEquals("<cmd> <str> [-loud] <text>", syntax)
    }

    private fun loudFlag(): ValueFlagImpl<TestEnv, Unit, Boolean> = presenceValueFlag(
        "loud",
        default = false,
        presentValue = true
    )
}
