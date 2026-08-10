package com.zombachu.stick.velocity

import kotlin.test.Test
import kotlin.test.assertSame

class VelocityEnvironmentTest {

    @Test
    fun `BasicVelocityEnvironment exposes injected proxy`() {
        val proxy = FakeProxyServer(FakeCommandManager())
        assertSame(proxy, BasicVelocityEnvironment(proxy).proxy)
    }
}
