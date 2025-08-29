package com.zombachu.stick.feedback

import com.zombachu.stick.impl.Array
import com.zombachu.stick.impl.Array0
import com.zombachu.stick.impl.Array1
import com.zombachu.stick.impl.Array10
import com.zombachu.stick.impl.Array11
import com.zombachu.stick.impl.Array12
import com.zombachu.stick.impl.Array2
import com.zombachu.stick.impl.Array3
import com.zombachu.stick.impl.Array4
import com.zombachu.stick.impl.Array5
import com.zombachu.stick.impl.Array6
import com.zombachu.stick.impl.Array7
import com.zombachu.stick.impl.Array8
import com.zombachu.stick.impl.Array9

class Feedback<N : Array<String>> internal constructor(
    val message: FeedbackMessage<N>,
    val args: List<String>,
) {
    internal constructor(message: FeedbackMessage<N>, args: N) : this(message, args.toList())

    fun format(message: FeedbackMessage<N> = this.message): String {
        val stringBuilder = StringBuilder(message.text)
        args.forEachIndexed { index, arg ->
            val regex = Regex.fromLiteral("%$index")
            stringBuilder.replace(regex, arg)
        }
        return stringBuilder.toString()
    }
}
typealias Feedback0 = Feedback<Array0<String>>
typealias Feedback1 = Feedback<Array1<String>>
typealias Feedback2 = Feedback<Array2<String>>
typealias Feedback3 = Feedback<Array3<String>>
typealias Feedback4 = Feedback<Array4<String>>
typealias Feedback5 = Feedback<Array5<String>>
typealias Feedback6 = Feedback<Array6<String>>
typealias Feedback7 = Feedback<Array7<String>>
typealias Feedback8 = Feedback<Array8<String>>
typealias Feedback9 = Feedback<Array9<String>>
typealias Feedback10 = Feedback<Array10<String>>
typealias Feedback11 = Feedback<Array11<String>>
typealias Feedback12 = Feedback<Array12<String>>

sealed class FeedbackMessage<N : Array<String>>(val text: String)
class FeedbackMessage0(defaultMessage: String) : FeedbackMessage<Array0<String>>(defaultMessage) {
    fun with(): Feedback0 =
        Feedback(this, Array0())
}
class FeedbackMessage1(defaultMessage: String) : FeedbackMessage<Array1<String>>(defaultMessage) {
    fun with(arg0: String): Feedback1 =
        Feedback(this, Array1(arg0))
}
class FeedbackMessage2(defaultMessage: String) : FeedbackMessage<Array2<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String): Feedback2 =
        Feedback(this, Array2(arg0, arg1))
}
class FeedbackMessage3(defaultMessage: String) : FeedbackMessage<Array3<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String): Feedback3 =
        Feedback(this, Array3(arg0, arg1, arg2))
}
class FeedbackMessage4(defaultMessage: String) : FeedbackMessage<Array4<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String): Feedback4 =
        Feedback(this, Array4(arg0, arg1, arg2, arg3))
}
class FeedbackMessage5(defaultMessage: String) : FeedbackMessage<Array5<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Feedback5 =
        Feedback(this, Array5(arg0, arg1, arg2, arg3, arg4))
}
class FeedbackMessage6(defaultMessage: String) : FeedbackMessage<Array6<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String): Feedback6 =
        Feedback(this, Array6(arg0, arg1, arg2, arg3, arg4, arg5))
}
class FeedbackMessage7(defaultMessage: String) : FeedbackMessage<Array7<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
             arg6: String): Feedback7 =
        Feedback(this, Array7(arg0, arg1, arg2, arg3, arg4, arg5, arg6))
}
class FeedbackMessage8(defaultMessage: String) : FeedbackMessage<Array8<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String): Feedback8 =
        Feedback(this, Array8(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7))
}
class FeedbackMessage9(defaultMessage: String) : FeedbackMessage<Array9<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String): Feedback9 =
        Feedback(this, Array9(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8))
}
class FeedbackMessage10(defaultMessage: String) : FeedbackMessage<Array10<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String): Feedback10 =
        Feedback(this, Array10(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9))
}
class FeedbackMessage11(defaultMessage: String) : FeedbackMessage<Array11<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String, arg10: String): Feedback11 =
        Feedback(this, Array11(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10))
}
class FeedbackMessage12(defaultMessage: String) : FeedbackMessage<Array12<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String, arg10: String, arg11: String): Feedback12 =
        Feedback(this, Array12(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11))
}

class PreformattedFeedbackMessage<N : Array<String>>(defaultMessage: String): FeedbackMessage<N>(defaultMessage) {
    fun with(preformattedArgs: N, unformattedArgs: List<String>): Feedback<N> =
        Feedback(this, preformattedArgs.toList() + unformattedArgs)
}

fun Feedback0(defaultMessage: String): Feedback0 = FeedbackMessage0(defaultMessage).with()
