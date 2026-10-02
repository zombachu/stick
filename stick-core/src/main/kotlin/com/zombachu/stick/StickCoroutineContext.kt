package com.zombachu.stick

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class StickCoroutineContext(val mainContext: CoroutineContext, val asyncContext: CoroutineContext) :
    AbstractCoroutineContextElement(StickCoroutineContext) {
    companion object Key : CoroutineContext.Key<StickCoroutineContext>
}
