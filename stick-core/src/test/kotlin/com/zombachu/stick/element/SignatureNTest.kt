package com.zombachu.stick.element

import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.expectSuccessValue
import com.zombachu.stick.withInvocation
import kotlin.test.Test
import kotlin.test.assertEquals

class SignatureNTest {

    @Test
    fun `signature returns arguments in order`() {
        val elements = (1..5).map { StringParameter<TestEnv, Unit>("", "") }
        val signature =
            Signature5<TestEnv, Unit, String, String, String, String, String>({ a, b, c, d, e -> }, elements)

        val args = withInvocation("a", "b", "c", "d", "e") { signature.execute() }.expectSuccessValue()

        assertEquals(["a", "b", "c", "d", "e"], [args.a, args.b, args.c, args.d, args.e])
    }
}
