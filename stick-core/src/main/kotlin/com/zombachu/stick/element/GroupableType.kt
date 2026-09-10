package com.zombachu.stick.element

enum class GroupableType(internal val parsingPriority: Int) {
    Literal(0),
    Default(1),
    Passthrough(2),
}
