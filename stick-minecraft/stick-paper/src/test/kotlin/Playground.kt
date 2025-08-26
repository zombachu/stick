package com.zombachu.stick.paper

import com.zombachu.stick.Result
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.paper.P2.*
import com.zombachu.stick.structure.id
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
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

    fun test() {
        val bridge = BukkitCommandBridge("synergy")
        bridge.register(WarpCommand())
        bridge.register(PageCommand())
        bridge.register(CustomCommand())
    }



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

typealias StructureElemeno<S, O, T> = StructureScopeo<S, O>.() -> T

class P2 {

    interface SenderScopeo<S : Contexto<O>, O : Any>
    class StructureScopeo<S : Contexto<O>, O : Any> : SenderScopeo<S, O> {
        fun <S2 : Contexto<O2>, O2 : Any> forSender(): StructureScopeo<S2, O2> {
            TODO()
        }
    }

    class Requiremento<S : Contexto<O>, O : Any> constructor(validate: (senderContext: S) -> Result<Unit>)

    open class Parametero<S : Contexto<O>, O : Any, T : Any>()

    interface ValidatedParametero<S : Contexto<O>, O : Any, T : Any>
    class ValidatedParameterImplo<S : Contexto<O>, O : Any, S2 : Contexto<O2>, O2 : Any, T : Any>(
        val parameter: Parametero<S2, O2, T>,
        val requirement: Requiremento<S, O>,
        val transform: (O) -> O2,
    ) : ValidatedParametero<S, O, T> {
    }

    open class Contexto<O : Any>(val sender: O)
    class BukContext<O : Any>(sender: O) : Contexto<O>(sender)

    class StringParametero<S : Contexto<O>, O : Any>(
        id: TypedIdentifier<String>,
        description: String,
    ) : Parametero<S, O, String>()

    @JvmInline
    value class Clazz<S : Contexto<*>>(val s: Byte = 0)

    fun <S2 : Contexto<*>> extractor(): Clazz<S2> {
        return Clazz<S2>()
    }

    class OCommand : SenderScopeo<Contexto<CommandSender>, CommandSender> {

        fun blah() {
//            val b = testingRequireIs<BukContext<CommandSender>, CommandSender, BukContext<Player>, Player, String>(
            val b = testingRequireIs(
                Clazz<BukContext<Player>>(),
            ) {
                requiredSubtypeStringParametero(id("yo"))
            }
        }
    }
}


fun <S : P2.BukContext<O>, O : Any> P2.SenderScopeo<S, O>.requiredSubtypeStringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<S, O, P2.StringParametero<S, O>> = {
    P2.StringParametero(id, description)
}


fun <S : Contexto<O>, O : Any> P2.SenderScopeo<S, O>.stringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<S, O, P2.StringParametero<S, O>> = {
    P2.StringParametero(id, description)
}


inline fun <S : Contexto<O>, O : Any, S2 : Contexto<O2>, reified O2 : O, T : Any> P2.SenderScopeo<S, O>.testingRequireIs(
    contextType: P2.Clazz<S2>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElemeno<S2, O2, StructureElemeno<S2, O2, Parametero<S2, O2, T>>>,
): StructureElemeno<S, O, ValidatedParametero<S, O, T>> = {
    val scope: StructureScopeo<S2, O2> = this.forSender()
    ValidatedParameterImplo<S, O, S2, O2, T>(
        parameter(scope)(scope),
        Requiremento {
            if (it.sender is O2) {
                SenderValidationResult.success()
            } else {
                SenderValidationResult.failSenderType()
            }
        },
        { it as O2 },
    )
}
