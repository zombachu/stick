package com.zombachu.stick.dsl

import com.zombachu.stick.element.parse
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.withExecution
import kotlin.test.Test
import kotlin.test.assertEquals

class MapsTest {

    @Test
    fun `map on bounded parameter transforms value`() = structureTest {
        val mapped = stringParameter("").map { s -> success(s.length) }
        val result = withExecution { mapped.parse(["hello"]) }
        assertEquals(5, result.expectSuccessValue())
    }

    @Test
    fun `chained maps run in order`() = structureTest {
        val mapped = stringParameter("").map { s -> success(s.length) }.map { n -> success(n * 2) }
        val result = withExecution { mapped.parse(["hello"]) }
        assertEquals(10, result.expectSuccessValue())
    }

    @Test
    fun `map on unbounded parameter transforms value`() = structureTest {
        val mapped = textParameter("").map { s -> success(s.uppercase()) }
        val result = withExecution { mapped.parse(["a", "b"]) }
        assertEquals("A B", result.expectSuccessValue())
    }

    @Test
    fun `map on ValueFlag transforms value`() = structureTest {
        val mapped = valueFlag("n", 0, intParameter("n")).map { n -> success(n * 10) }
        val result = withExecution { mapped.parse(["-n", "5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `map on OptionalParameter transforms value`() = structureTest {
        val mapped = optionally(parameter = intParameter(""), ifAbsent = default(0)).map { n -> success(n * 10) }
        val result = withExecution("5") { mapped.parse(["5"]) }
        assertEquals(50, result.expectSuccessValue())
    }

    @Test
    fun `map on Helper transforms value`() = structureTest {
        val mapped = helper { success(5) }.map { n -> success(n * 10) }
        val result = withExecution { mapped.parse([]) }
        assertEquals(50, result.expectSuccessValue())
    }
}
