package com.zombachu.stick.dsl

import com.zombachu.stick.element.parse
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import kotlin.test.Test
import kotlin.test.assertEquals

class PipelinesTest {

    @Test
    fun `pipeline on bounded parameter chains operation`() = structureTest {
        val piped = stringParameter("").pipeline { s -> success(s.length) }
        val result = withExecution { piped.parse(["hello"]) }
        assertEquals(5, result.expectSuccessValue())
    }

    @Test
    fun `pipeline chains two operations in order`() = structureTest {
        val piped =
            stringParameter("")
                .pipeline({ s -> success(s.length) }, { n -> success(n * 2) })
        val result = withExecution { piped.parse(["hello"]) }
        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `pipeline on unbounded parameter chains operation`() = structureTest {
        val piped = textParameter("").pipeline { s -> success(s.uppercase()) }
        val result = withExecution { piped.parse(["a", "b"]) }
        assertEquals("A B", result.expectSuccessValue())
    }

    @Test
    fun `pipeline on ValueFlag chains operation`() = structureTest {
        val piped = valueFlag("n", 0, intParameter("n")).pipeline { n -> success(n * 10) }
        val result = withExecution { piped.parse(["-n", "5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `pipeline on OptionalParameter chains operation`() = structureTest {
        val piped =
            optionally(ifAbsent = default(0), parameter = intParameter(""))
                .pipeline { n -> success(n * 10) }
        val result = withExecution("5") { piped.parse(["5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `map wraps transform as always-succeeding operation`() = structureTest {
        val piped = stringParameter("").pipeline(map { s -> s.length })
        val result = withExecution { piped.parse(["hello"]) }
        assertEquals(5, result.expectSuccessValue())
    }
}
