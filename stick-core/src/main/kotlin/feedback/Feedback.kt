package com.zombachu.stick.feedback

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
import com.zombachu.stick.impl.Tuple
import com.zombachu.stick.impl.Tuple0

data class Feedback<N : Tuple<String>>(
    val defaultMessage: FeedbackMessage<N>,
    val args: List<String>,
) {
    internal constructor(message: FeedbackMessage<N>, args: N) : this(message, args.toList())

    protected fun format(message: FeedbackMessage<N>): String {
        val stringBuilder = StringBuilder(message.text)
        args.forEachIndexed { index, arg ->
            val regex = Regex.fromLiteral("%$index")
            stringBuilder.replace(regex, arg)
        }
        return stringBuilder.toString()
    }

    companion object {
        fun empty(): Feedback<Tuple0<String>> = Feedback(FeedbackMessage0(""), Tuple0())
    }
}
typealias Feedback0 = Feedback<Array0<String>>
typealias Feedback1 = Feedback<Array1<String>>
typealias Feedback2 = Feedback<Array2<String>>
typealias Feedback3 = Feedback<Array3<String>>
typealias Feedback4 = Feedback<Array4<String>>
typealias Feedback5 = Feedback<Array1<String>>
typealias Feedback6 = Feedback<Array2<String>>
typealias Feedback7 = Feedback<Array3<String>>
typealias Feedback8 = Feedback<Array4<String>>
typealias Feedback9 = Feedback<Array1<String>>
typealias Feedback10 = Feedback<Array2<String>>
typealias Feedback11 = Feedback<Array3<String>>
typealias Feedback12 = Feedback<Array4<String>>

sealed class FeedbackMessage<N : Tuple<String>>(val text: String) {
    fun with(args: N): Feedback<N> = Feedback(this, args)
}
open class FeedbackMessage0(defaultMessage: String) : FeedbackMessage<Array0<String>>(defaultMessage)
open class FeedbackMessage1(defaultMessage: String) : FeedbackMessage<Array1<String>>(defaultMessage)
open class FeedbackMessage2(defaultMessage: String) : FeedbackMessage<Array2<String>>(defaultMessage)
open class FeedbackMessage3(defaultMessage: String) : FeedbackMessage<Array3<String>>(defaultMessage)
open class FeedbackMessage4(defaultMessage: String) : FeedbackMessage<Array4<String>>(defaultMessage)
open class FeedbackMessage5(defaultMessage: String) : FeedbackMessage<Array5<String>>(defaultMessage)
open class FeedbackMessage6(defaultMessage: String) : FeedbackMessage<Array6<String>>(defaultMessage)
open class FeedbackMessage7(defaultMessage: String) : FeedbackMessage<Array7<String>>(defaultMessage)
open class FeedbackMessage8(defaultMessage: String) : FeedbackMessage<Array8<String>>(defaultMessage)
open class FeedbackMessage9(defaultMessage: String) : FeedbackMessage<Array9<String>>(defaultMessage)
open class FeedbackMessage10(defaultMessage: String) : FeedbackMessage<Array10<String>>(defaultMessage)
open class FeedbackMessage11(defaultMessage: String) : FeedbackMessage<Array11<String>>(defaultMessage)
open class FeedbackMessage12(defaultMessage: String) : FeedbackMessage<Array12<String>>(defaultMessage)
