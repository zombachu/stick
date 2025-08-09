package com.zombachu.stick.paper

import kotlin.experimental.ExperimentalTypeInference

typealias FruitScope = Fruit.() -> Int

interface Fruit {
    fun chop(): Int
}
class Apple : Fruit {
    override fun chop(): Int = 1
}
class Banana : Fruit {
    override fun chop(): Int = 1
}

fun Fruit.doSomething(
    action: FruitScope
): Int {
    return action(this)
}

fun main() {
    val apple = Apple()

    apple.doSomething {
        chop()
//        it.chop()
    }

}

class Playground {



//    class Sign<S, A, B, C, D, E>: Sign.Sign1<S, A>, Sign.Sign2<S, B>, Sign.Sign3<S, C>, Sign.Sign4<S, D>, Sign.Sign5<S, E> {
//        override var elementA: Element<S, A>? = null
//        override var elementB: Element<S, B>? = null
//        override var elementC: Element<S, C>? = null
//        override var elementD: Element<S, D>? = null
//        override var elementE: Element<S, E>? = null
//
//        interface Sign1<S, A> {
//            var elementA: Element<S, A>?
//        }
//
//        interface Sign2<S, B> {
//            var elementB: Element<S, B>?
//        }
//
//        interface Sign3<S, C> {
//            var elementC: Element<S, C>?
//        }
//
//        interface Sign4<S, D> {
//            var elementD: Element<S, D>?
//        }
//
//        interface Sign5<S, E> {
//            var elementE: Element<S, E>?
//        }
//
////        @OptIn(ExperimentalContracts::class)
////        fun intParam(element: IntParameter<S>) {
////            contract {
////                returns() implies (this@Sign is Sign1<S, Int>)
////            }
////            elementA = element
////        }
//
//        fun stringParam() {
//
//        }
//    }


    class Type1<A>
    class Type2<A, B>

    inline fun <reified T> test(
        l: T.() -> Type1<T>
    ): Type1<T> {
        return Type1<T>()
    }

    inline fun <reified T> test(
        l: T.() -> Type2<T, T>
    ): Type2<T, T> {
        return Type2<T, T>()
    }


    @OptIn(ExperimentalTypeInference::class)
    @OverloadResolutionByLambdaReturnType
    fun testInt(l: IntReceiver.() -> Int) {
        TODO()
    }

    @OptIn(ExperimentalTypeInference::class)
    @OverloadResolutionByLambdaReturnType
    fun testBoolean(l: BooleanReceiver.() -> Boolean) {
        TODO()
    }

    class IntReceiver {
        val someInt: Int = 10
    }

    class BooleanReceiver {
        val someBool: Boolean = true
    }

}
