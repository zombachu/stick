package com.zombachu.stick.failure

class FailureOrigin
internal constructor(
    val elementName: String?,
    lazyUsage: () -> String,
) {
    val usage: String by lazy(LazyThreadSafetyMode.NONE, lazyUsage)
}
