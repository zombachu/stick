package com.zombachu.stick.element

import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Size
import com.zombachu.stick.TestEnv
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class HelperImplTest {

    @Test
    fun `parse evaluates to contextual value`() {
        val helper = HelperImpl<TestEnv, Unit, String>({ ParsingResult.success("computed") })
        val result = withInvocation { helper.parse(["ignored", "args"]) }
        assertEquals("computed", result.expectSuccessValue())
    }

    @Test
    fun `has correct properties`() {
        val helper = HelperImpl<TestEnv, Unit, String>({ ParsingResult.success("x") })

        assertIs<Size.Bounded>(helper.size)
        assertEquals(0, helper.size.min)
        assertEquals(0, helper.size.max)
        assertEquals(ElementType.Helper, helper.type)
    }
}
