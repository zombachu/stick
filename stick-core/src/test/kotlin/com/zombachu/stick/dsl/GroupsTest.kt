package com.zombachu.stick.dsl

import com.zombachu.stick.GroupResult
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.element.parameters.TextParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.structureTest
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GroupsTest {

    @Test
    fun `group names synthesized child scope`() = structureTest {
        val group = group(StringParameter("", ""))

        assertEquals("_group", group.name)

        val result = withInvocation("hello") { group.parse(["hello"]) }
        assertIs<GroupResult.ResultA<String>>(result.expectSuccessValue())
    }

    @Test
    fun `group accepts terminating Groupable`() = structureTest {
        val group = group(TextParameter("", ""))

        val result = withInvocation("a", "b", "c") { group.parse(["a", "b", "c"]) }

        val tagged = result.expectSuccessValue()
        assertIs<GroupResult.ResultA<String>>(tagged)
        assertEquals("a b c", tagged.value)
    }
}
