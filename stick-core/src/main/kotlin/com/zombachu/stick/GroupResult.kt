package com.zombachu.stick

sealed interface GroupResult1<out A> : GroupResult

sealed interface GroupResult2<out A, out B> : GroupResult

sealed interface GroupResult3<out A, out B, out C> : GroupResult

sealed interface GroupResult4<out A, out B, out C, out D> : GroupResult

sealed interface GroupResult5<out A, out B, out C, out D, out E> : GroupResult

sealed interface GroupResult6<out A, out B, out C, out D, out E, out F> : GroupResult

sealed interface GroupResult7<out A, out B, out C, out D, out E, out F, out G> : GroupResult

sealed interface GroupResult8<out A, out B, out C, out D, out E, out F, out G, out H> : GroupResult

sealed interface GroupResult {
    val value: Any?

    data class ResultA<out T>(override val value: T) :
        GroupResult1<T>,
        GroupResult2<T, Nothing>,
        GroupResult3<T, Nothing, Nothing>,
        GroupResult4<T, Nothing, Nothing, Nothing>,
        GroupResult5<T, Nothing, Nothing, Nothing, Nothing>,
        GroupResult6<T, Nothing, Nothing, Nothing, Nothing, Nothing>,
        GroupResult7<T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing>,
        GroupResult8<T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing>

    data class ResultB<out T>(override val value: T) :
        GroupResult2<Nothing, T>,
        GroupResult3<Nothing, T, Nothing>,
        GroupResult4<Nothing, T, Nothing, Nothing>,
        GroupResult5<Nothing, T, Nothing, Nothing, Nothing>,
        GroupResult6<Nothing, T, Nothing, Nothing, Nothing, Nothing>,
        GroupResult7<Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing>,
        GroupResult8<Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing>

    data class ResultC<out T>(override val value: T) :
        GroupResult3<Nothing, Nothing, T>,
        GroupResult4<Nothing, Nothing, T, Nothing>,
        GroupResult5<Nothing, Nothing, T, Nothing, Nothing>,
        GroupResult6<Nothing, Nothing, T, Nothing, Nothing, Nothing>,
        GroupResult7<Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing>,
        GroupResult8<Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing, Nothing>

    data class ResultD<out T>(override val value: T) :
        GroupResult4<Nothing, Nothing, Nothing, T>,
        GroupResult5<Nothing, Nothing, Nothing, T, Nothing>,
        GroupResult6<Nothing, Nothing, Nothing, T, Nothing, Nothing>,
        GroupResult7<Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing>,
        GroupResult8<Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing, Nothing>

    data class ResultE<out T>(override val value: T) :
        GroupResult5<Nothing, Nothing, Nothing, Nothing, T>,
        GroupResult6<Nothing, Nothing, Nothing, Nothing, T, Nothing>,
        GroupResult7<Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing>,
        GroupResult8<Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing, Nothing>

    data class ResultF<out T>(override val value: T) :
        GroupResult6<Nothing, Nothing, Nothing, Nothing, Nothing, T>,
        GroupResult7<Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing>,
        GroupResult8<Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing, Nothing>

    data class ResultG<out T>(override val value: T) :
        GroupResult7<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T>,
        GroupResult8<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T, Nothing>

    data class ResultH<out T>(override val value: T) :
        GroupResult8<Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, Nothing, T>
}
