package com.zombachu.stick

data class GroupResult<T>(
    val id: TypedIdentifier<T>,
    val value: T,
) {
    inline fun <T2> on(id: TypedIdentifier<T2>, onValue: (T2) -> Unit) {
        if (id == this.id) {
            @Suppress("UNCHECKED_CAST")
            onValue(value as T2)
        }
    }
}
