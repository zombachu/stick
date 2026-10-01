package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
import com.zombachu.stick.element.GroupableType
import com.zombachu.stick.element.parse
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.isSuccess
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withExecution
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class StoresTest {

    @Test
    fun `store on Parameter stores parsed value`() = structureTest {
        val identifier = id<String>("name")
        val stored = stringParameter("").store(identifier)

        val ex = testExecution("bob")
        val result = context(ex) { stored.parse(["bob"]) }

        assertEquals("bob", result.expectSuccessValue())
        assertEquals("bob", ex.get(identifier))
    }

    @Test
    fun `store on Parameter stores nothing on failure`() = structureTest {
        val identifier = id<Int>("amount")
        val stored = intParameter("").store(identifier)

        val ex = testExecution("x")
        val result = context(ex) { stored.parse(["x"]) }

        assertFalse(result.isSuccess())
        assertNull(ex.get(identifier))
    }

    @Test
    fun `store on Parameter accepts wider identifier`() = structureTest {
        val identifier = id<Any>("amount")
        val stored = intParameter("").store(identifier)

        val ex = testExecution("5")
        val result: Int = context(ex) { stored.parse(["5"]) }.expectSuccessValue()

        assertEquals(5, result)
        assertEquals(5, ex.get(identifier))
    }

    @Test
    fun `store on Parameter keeps base type`() = structureTest {
        val stored = stringParameter("").store(id("name"))
        assertEquals(GroupableType.Passthrough, stored.type)
    }

    @Test
    fun `store on Parameter keeps base syntax`() = structureTest {
        val stored = enumParameter("", Color::class).store(id("color"))
        assertEquals("<red|green>", withInvocation { stored.getSyntax() })
    }

    @Test
    fun `store on Parameter keeps group priority`() = structureTest {
        val group = group(stringParameter("name").store(id("name")), intParameter("amount"))
        val result = withExecution("5") { group.parse(["5"]) }
        assertEquals(GroupResult.ResultB(5), result.expectSuccessValue())
    }

    @Test
    fun `store on Helper stores contextual value`() = structureTest {
        val identifier = id<String>("computed")
        val stored = helper { success("computed") }.store(identifier)

        val ex = testExecution()
        val result = context(ex) { stored.parse([]) }

        assertEquals("computed", result.expectSuccessValue())
        assertEquals("computed", ex.get(identifier))
    }

    @Test
    fun `store on OptionalParameter keeps value type for wider identifier`() = structureTest {
        val identifier = id<Any>("amount")
        val stored = optionally(intParameter(""), 3).store(identifier)

        val ex = testExecution()
        val result: Int = context(ex) { stored.parse([]) }.expectSuccessValue()

        assertEquals(3, result)
        assertEquals(3, ex.get(identifier))
    }

    @Test
    fun `store on Helper keeps value type for wider identifier`() = structureTest {
        val identifier = id<Any>("computed")
        val stored = helper { success(5) }.store(identifier)

        val ex = testExecution()
        val result: Int = context(ex) { stored.parse([]) }.expectSuccessValue()

        assertEquals(5, result)
        assertEquals(5, ex.get(identifier))
    }

    private enum class Color {
        RED,
        GREEN,
    }
}
