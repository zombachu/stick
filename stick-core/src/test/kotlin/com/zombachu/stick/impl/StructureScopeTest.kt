package com.zombachu.stick.impl

import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TestEnv
import com.zombachu.stick.element.Signature0
import com.zombachu.stick.isSuccess
import com.zombachu.stick.withValidationContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class StructureScopeTest {

    @Test
    fun `empty scope initializes blank fields`() {
        val scope = StructureScope.empty<TestEnv, Unit>()

        assertEquals("", scope.name)
        assertEquals([], scope.aliases)
        assertEquals("", scope.description)
        assertNull(scope.parent)
    }

    @Test
    fun `empty scope requirement succeeds`() {
        val scope = StructureScope.empty<TestEnv, Unit>()
        val result = withValidationContext { scope.requirement.validateSender() }
        assertTrue(result.isSuccess())
    }

    @Test
    fun `forSender preserves fields and resets requirement`() {
        val parent = StructureScope.empty<TestEnv, Unit>()
        val scope = StructureScope<TestEnv, Unit>(
            "sub",
            ["s"],
            "desc",
            parent,
            Requirement
            { SenderValidationResult.failSender() }
        )

        val forSender = scope.forSender<TestEnv, String>()

        assertEquals("sub", forSender.name)
        assertEquals(["s"], forSender.aliases)
        assertEquals("desc", forSender.description)
        assertEquals(parent, forSender.parent)
        assertTrue(withValidationContext("anything") { forSender.requirement.validateSender() }.isSuccess())
    }

    @Test
    fun `build copies fields into StructureImpl`() {
        val scope = StructureScope<TestEnv, Unit>(
            "sub",
            ["s"],
            "desc",
            null,
            Requirement { SenderValidationResult.success() }
        )

        val structure = scope.build(Signature0({}, []))

        assertEquals("sub", structure.name)
        assertEquals(["s"], structure.aliases)
        assertEquals("desc", structure.description)
    }
}
