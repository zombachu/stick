package com.zombachu.stick.dsl

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.testInvocation
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpersTest {

    @Test
    fun `helper from contextual value evaluates on parse`() = structureTest {
        val helper = helper { ParsingResult.success("computed") }
        val result = withInvocation { helper.parse([]) }
        assertEquals("computed", result.expectSuccessValue())
    }

    @Test
    fun `helper from TypedIdentifier reads stored value`() = structureTest {
        val identifier = id<String>("name")
        val helper = helper(identifier)

        val inv = testInvocation()
        inv.put(identifier, "bob")
        val result = context(inv) { helper.parse([]) }

        assertEquals("bob", result.expectSuccessValue())
    }
}
