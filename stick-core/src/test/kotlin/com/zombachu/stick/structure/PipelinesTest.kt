package com.zombachu.stick.structure

import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PipelinesTest {

    @Test
    fun `pipeline on FixedSize parameter chains operation`() = structureTest {
        val piped = stringParameter("").pipeline { s -> ParsingResult.success(s.length) }
        val result = withInvocation { piped.parse(["hello"]) }
        assertEquals(5, result.expectSuccessValue())
    }

    @Test
    fun `pipeline chains two operations in order`() = structureTest {
        val piped =
            stringParameter("")
                .pipeline({ s -> ParsingResult.success(s.length) }, { n -> ParsingResult.success(n * 2) })
        val result = withInvocation { piped.parse(["hello"]) }
        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `pipeline on UnknownSize parameter chains operation`() = structureTest {
        val piped = textParameter("").pipeline { s -> ParsingResult.success(s.uppercase()) }
        val result = withInvocation { piped.parse(["a", "b"]) }
        assertEquals("A B", result.expectSuccessValue())
    }

    @Test
    fun `pipeline on ValueFlag chains operation`() = structureTest {
        val piped = valueFlag("n", 0, intParameter("n")).pipeline { n -> ParsingResult.success(n * 10) }
        val result = withInvocation { piped.parse(["-n", "5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    // TODO
    @Test
    fun `KNOWN BUG - pipeline on a HybridFlag crashes instead of transforming the unwrapped inner value`() =
        structureTest {
            // HybridFlag<E,S,T> is a Flag<E,S,HybridFlagResult<T>> - its own SyntaxElement value type is
            // the *wrapped* HybridFlagResult<T>, not T. But the public .pipeline() extension for HybridFlag
            // declares `operation: PipelineOperation<E,S,A,B>` where A is the *unwrapped* inner type T
            // (matching every other .pipeline() overload). PipelinedHybridFlag.parse() then calls the
            // shared parsePipeline() helper, which - operating on HybridFlag's real SyntaxElement type -
            // hands the operation the *wrapped* HybridFlagResult<T> at runtime, not the unwrapped T the
            // public API promises. Since the operation's compiled bytecode expects an unboxed Int (per its
            // declared type), this throws a real ClassCastException the moment a HybridFlag pipeline
            // operation actually runs - group()/valueFlag()/optionally() pipelines are unaffected, this is
            // specific to hybridFlag(). This test documents the INTENDED behavior (transforming the
            // unwrapped inner value) and is expected to fail against the current source.
            val piped = hybridFlag("n", intParameter("")).pipeline { n -> ParsingResult.success(n * 2) }
            val result = withInvocation { piped.parse(["-n", "5"]) }
            val value = result.expectSuccessValue()
            assertIs<HybridFlagResult.Value<Int>>(value)
            assertEquals(10, value.value)
        }

    @Test
    fun `pipeline on OptionalParameter chains operation`() = structureTest {
        val piped =
            optionally(ifAbsent = default(0), parameter = intParameter(""))
                .pipeline { n -> ParsingResult.success(n * 10) }
        val result = withInvocation("5") { piped.parse(["5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `map wraps transform as always-succeeding operation`() = structureTest {
        val piped = stringParameter("").pipeline(map { s -> s.length })
        val result = withInvocation { piped.parse(["hello"]) }
        assertEquals(5, result.expectSuccessValue())
    }
}
