package com.zombachu.stick.element

import com.zombachu.stick.CommandResult
import com.zombachu.stick.Environment
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.Suggestion
import com.zombachu.stick.TestEnv
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.invalidSenderDefault
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals

class TransformedElementTest {

    private val allowed = Requirement<TestEnv, String> { SenderValidationResult.success() }

    @Test
    fun `TransformedParameter suggests with the transformed sender`() {
        val base = SenderParameter<TestEnv, Int>()
        val transformed =
            TransformedParameter<TestEnv, String, Int, String, Position.Leading>(base, String::length, allowed)

        val suggestions = withValidationContext("zombachu") { transformed.suggest([], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedValueFlag suggests with the transformed sender`() {
        val flagParameter = FlagParameter.ParameterFlagParameter("f", SenderParameter<TestEnv, Int>(), [])
        val base = ValueFlagImpl("f", { ParsingResult.success("") }, flagParameter)
        val transformed = TransformedValueFlag(base, String::length, invalidSenderDefault<TestEnv, String, String>(""))

        val suggestions = withValidationContext("zombachu") { transformed.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    @Test
    fun `TransformedHybridFlag suggests with the transformed sender`() {
        val base = HybridFlagImpl<TestEnv, Int, String>("f", SenderParameter(), [])
        val transformed =
            TransformedHybridFlag(
                base,
                String::length,
                invalidSenderDefault<TestEnv, String, HybridFlagResult<String>>(HybridFlagResult.Absent()),
            )

        val suggestions = withValidationContext("zombachu") { transformed.suggest(["-f"], "") }

        assertEquals(["8"], suggestions.map { it.value })
    }

    private class SenderParameter<E : Environment, S> : Parameter.Size1<E, S, String>("", "") {

        context(validationContext: ValidationContext<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            [SimpleSuggestion("${validationContext.sender}")]

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(arg0: String): CommandResult<String> = ParsingResult.success(arg0)
    }
}
