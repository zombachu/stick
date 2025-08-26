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



//    class Sign<E, A, B, C, D, E>: Sign.Sign1<E, A>, Sign.Sign2<E, B>, Sign.Sign3<E, C>, Sign.Sign4<E, D>, Sign.Sign5<E, E> {
//        override var elementA: Element<E, A>? = null
//        override var elementB: Element<E, B>? = null
//        override var elementC: Element<E, C>? = null
//        override var elementD: Element<E, D>? = null
//        override var elementE: Element<E, E>? = null
//
//        interface Sign1<E, A> {
//            var elementA: Element<E, A>?
//        }
//
//        interface Sign2<E, B> {
//            var elementB: Element<E, B>?
//        }
//
//        interface Sign3<E, C> {
//            var elementC: Element<E, C>?
//        }
//
//        interface Sign4<E, D> {
//            var elementD: Element<E, D>?
//        }
//
//        interface Sign5<E, E> {
//            var elementE: Element<E, E>?
//        }
//
////        @OptIn(ExperimentalContracts::class)
////        fun intParam(element: IntParameter<E>) {
////            contract {
////                returns() implies (this@Sign is Sign1<E, Int>)
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

typealias StructureElemeno<E, O, T> = StructureScopeo<E, O>.() -> T

class P2 {

    interface SenderScopeo<E : Contexto<O>, O : Any>
    class StructureScopeo<E : Contexto<O>, O : Any> : SenderScopeo<E, O> {
        fun <E2 : Contexto<O2>, O2 : Any> forSender(): StructureScopeo<E2, O2> {
            TODO()
        }
    }

    class Requiremento<E : Contexto<O>, O : Any> constructor(validate: (env: E) -> Result<Unit>)

    open class Parametero<E : Contexto<O>, O : Any, T : Any>()

    interface ValidatedParametero<E : Contexto<O>, O : Any, T : Any>
    class ValidatedParameterImplo<E : Contexto<O>, O : Any, E2 : Contexto<O2>, O2 : Any, T : Any>(
        val parameter: Parametero<E2, O2, T>,
        val requirement: Requiremento<E, O>,
        val transform: (O) -> O2,
    ) : ValidatedParametero<E, O, T> {
    }

    open class Contexto<O : Any>(val sender: O)
    class BukContext<O : Any>(sender: O) : Contexto<O>(sender)

    class StringParametero<E : Contexto<O>, O : Any>(
        id: TypedIdentifier<String>,
        description: String,
    ) : Parametero<E, O, String>()

    @JvmInline
    value class Clazz<E : Contexto<*>>(val s: Byte = 0)

    fun <E2 : Contexto<*>> extractor(): Clazz<E2> {
        return Clazz<E2>()
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


fun <E : P2.BukContext<O>, O : Any> P2.SenderScopeo<E, O>.requiredSubtypeStringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<E, O, P2.StringParametero<E, O>> = {
    P2.StringParametero(id, description)
}


fun <E : Contexto<O>, O : Any> P2.SenderScopeo<E, O>.stringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<E, O, P2.StringParametero<E, O>> = {
    P2.StringParametero(id, description)
}


inline fun <E : Contexto<O>, O : Any, E2 : Contexto<O2>, reified O2 : O, T : Any> P2.SenderScopeo<E, O>.testingRequireIs(
    contextType: P2.Clazz<E2>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElemeno<E2, O2, StructureElemeno<E2, O2, Parametero<E2, O2, T>>>,
): StructureElemeno<E, O, ValidatedParametero<E, O, T>> = {
    val scope: StructureScopeo<E2, O2> = this.forSender()
    ValidatedParameterImplo<E, O, E2, O2, T>(
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
