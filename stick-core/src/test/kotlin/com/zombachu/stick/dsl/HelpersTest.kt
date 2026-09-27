package com.zombachu.stick.dsl

import com.zombachu.stick.element.parse
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.success
import com.zombachu.stick.testExecution
import com.zombachu.stick.withExecution
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {

    @Test
    fun `helper from contextual value evaluates on parse`() = structureTest {
        val helper = helper { success("computed") }
        val result = withExecution { helper.parse([]) }
        assertEquals("computed", result.expectSuccessValue())
    }

    @Test
    fun `helper from TypedIdentifier reads stored value`() = structureTest {
        val identifier = id<String>("name")
        val helper = helper(identifier)

        val ex = testExecution()
        ex.put(identifier, "bob")
        val result = context(ex) { helper.parse([]) }

        assertEquals("bob", result.expectSuccessValue())
    }
}
