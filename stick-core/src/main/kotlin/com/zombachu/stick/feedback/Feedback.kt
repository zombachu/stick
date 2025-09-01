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

open class Feedback<N : Array<String>> internal constructor(
    val defaultText: String,
    val args: N,
) {

    fun format(): String = format(defaultText)

    fun format(message: FeedbackMessage<N>) = format(message.text)

    private fun format(text: String): String {
        val stringBuilder = StringBuilder(text)
        args.toList().forEachIndexed { index, arg ->
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

class PreformattedFeedback<N : Array<String>>(
    defaultText: String,
    args: N,
    val extra: List<String>,
) : Feedback<N>(defaultText, args)

sealed class FeedbackMessage<N : Array<String>>(val text: String)
class FeedbackMessage0(defaultMessage: String) : FeedbackMessage<Array0<String>>(defaultMessage) {
    fun with(): Feedback0 =
        Feedback(text, Array0())
}
class FeedbackMessage1(defaultMessage: String) : FeedbackMessage<Array1<String>>(defaultMessage) {
    fun with(arg0: String): Feedback1 =
        Feedback(text, Array1(arg0))
}
class FeedbackMessage2(defaultMessage: String) : FeedbackMessage<Array2<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String): Feedback2 =
        Feedback(text, Array2(arg0, arg1))
}
class FeedbackMessage3(defaultMessage: String) : FeedbackMessage<Array3<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String): Feedback3 =
        Feedback(text, Array3(arg0, arg1, arg2))
}
class FeedbackMessage4(defaultMessage: String) : FeedbackMessage<Array4<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String): Feedback4 =
        Feedback(text, Array4(arg0, arg1, arg2, arg3))
}
class FeedbackMessage5(defaultMessage: String) : FeedbackMessage<Array5<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String): Feedback5 =
        Feedback(text, Array5(arg0, arg1, arg2, arg3, arg4))
}
class FeedbackMessage6(defaultMessage: String) : FeedbackMessage<Array6<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String): Feedback6 =
        Feedback(text, Array6(arg0, arg1, arg2, arg3, arg4, arg5))
}
class FeedbackMessage7(defaultMessage: String) : FeedbackMessage<Array7<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String,
             arg6: String): Feedback7 =
        Feedback(text, Array7(arg0, arg1, arg2, arg3, arg4, arg5, arg6))
}
class FeedbackMessage8(defaultMessage: String) : FeedbackMessage<Array8<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String): Feedback8 =
        Feedback(text, Array8(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7))
}
class FeedbackMessage9(defaultMessage: String) : FeedbackMessage<Array9<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String): Feedback9 =
        Feedback(text, Array9(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8))
}
class FeedbackMessage10(defaultMessage: String) : FeedbackMessage<Array10<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String): Feedback10 =
        Feedback(text, Array10(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9))
}
class FeedbackMessage11(defaultMessage: String) : FeedbackMessage<Array11<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String, arg10: String): Feedback11 =
        Feedback(text, Array11(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10))
}
class FeedbackMessage12(defaultMessage: String) : FeedbackMessage<Array12<String>>(defaultMessage) {
    fun with(arg0: String, arg1: String, arg2: String, arg3: String, arg4: String, arg5: String, arg6: String,
             arg7: String, arg8: String, arg9: String, arg10: String, arg11: String): Feedback12 =
        Feedback(text, Array12(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11))
}

class PreformattedFeedbackMessage<N : Array<String>>(defaultMessage: String): FeedbackMessage<N>(defaultMessage) {
    fun with(preformattedArgs: N, unformattedArgs: List<String>): PreformattedFeedback<N> =
        PreformattedFeedback(text, preformattedArgs, unformattedArgs)
}

fun Feedback0(defaultMessage: String): Feedback0 = FeedbackMessage0(defaultMessage).with()
