package com.zombachu.stick

data class GroupResult<T : Any>(
    val id: TypedIdentifier<out T>,
    val value: T,
) {
    inline fun <T2 : Any> on(id: TypedIdentifier<T2>, handler: (T2) -> Unit) {
        if (id == this.id) {
            handler(value as T2)
        }
    }
}
