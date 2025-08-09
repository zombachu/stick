package com.zombachu.stick.impl

sealed class ElementType(internal val parsingPriority: Int) {
    // Implicitly used by signatures
    private class Helper : ElementType(0)
    private class Flag : ElementType(1)
    // Used by groups
    private class Literal : ElementType(2)
    private class Default : ElementType(3)
    private class Passthrough : ElementType(4)

    companion object {
        internal val Helper: ElementType = Helper()
        internal val Flag: ElementType = Flag()
        val Literal: ElementType = Literal()
        val Default: ElementType = Default()
        val Passthrough: ElementType = Passthrough()
    }
}
