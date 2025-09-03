package com.zombachu.stick.paper

import com.zombachu.stick.CommandResult
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.paper.P2.*
import com.zombachu.stick.structure.id
import io.papermc.paper.plugin.configuration.PluginMeta
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginLoader
import java.io.File
import java.io.InputStream
import java.util.logging.Logger
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
        val stick = BukkitStick(fakePlugin)

        stick.withContext {
            register(WarpCommand())
            register(PageCommand())
        }

        stick.withContext(CustomBukkitEnvironment(), CustomFailureHandler()) {
            register(CustomCommand())
            register(PageCommand())
        }
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

typealias StructureElemeno<E, S, T> = StructureScopeo<E, S>.() -> T

class P2 {

    interface SenderScopeo<E : Contexto<S>, S : Any>
    class StructureScopeo<E : Contexto<S>, S : Any> : SenderScopeo<E, S> {
        fun <E2 : Contexto<S2>, S2 : Any> forSender(): StructureScopeo<E2, S2> {
            TODO()
        }
    }

    class Requiremento<E : Contexto<S>, S : Any> constructor(validate: (env: E) -> CommandResult<Unit>)

    open class Parametero<E : Contexto<S>, S : Any, T>

    interface ValidatedParametero<E : Contexto<S>, S : Any, T>
    class ValidatedParameterImplo<E : Contexto<S>, S : Any, E2 : Contexto<S2>, S2 : Any, T>(
        val parameter: Parametero<E2, S2, T>,
        val requirement: Requiremento<E, S>,
        val transform: (S) -> S2,
    ) : ValidatedParametero<E, S, T>

    open class Contexto<S : Any>(val sender: S)
    class BukContext<S : Any>(sender: S) : Contexto<S>(sender)

    class StringParametero<E : Contexto<S>, S : Any>(
        id: TypedIdentifier<String>,
        description: String,
    ) : Parametero<E, S, String>()

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


fun <E : BukContext<S>, S : Any> SenderScopeo<E, S>.requiredSubtypeStringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<E, S, StringParametero<E, S>> = {
    StringParametero(id, description)
}


fun <E : Contexto<S>, S : Any> SenderScopeo<E, S>.stringParametero(
    id: TypedIdentifier<String>,
    description: String = "",
): StructureElemeno<E, S, StringParametero<E, S>> = {
    StringParametero(id, description)
}


inline fun <E : Contexto<S>, S : Any, E2 : Contexto<S2>, reified S2 : S, T> SenderScopeo<E, S>.testingRequireIs(
    contextType: Clazz<E2>,
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    noinline parameter: StructureElemeno<E2, S2, StructureElemeno<E2, S2, Parametero<E2, S2, T>>>,
): StructureElemeno<E, S, ValidatedParametero<E, S, T>> = {
    val scope: StructureScopeo<E2, S2> = this.forSender()
    ValidatedParameterImplo<E, S, E2, S2, T>(
        parameter(scope)(scope),
        Requiremento {
            if (it.sender is S2) {
                SenderValidationResult.success()
            } else {
                SenderValidationResult.failSenderType()
            }
        },
        { it as S2 },
    )
}


val fakePlugin: Plugin = object : Plugin {
    override fun getDataFolder(): File { TODO() }

    override fun getDescription(): PluginDescriptionFile { TODO() }

    override fun getPluginMeta(): PluginMeta { TODO() }

    override fun getConfig(): FileConfiguration { TODO() }

    override fun getResource(filename: String): InputStream? { TODO() }

    override fun saveConfig() { TODO() }

    override fun saveDefaultConfig() { TODO() }

    override fun saveResource(resourcePath: String, replace: Boolean) { TODO() }

    override fun reloadConfig() { TODO() }

    override fun getPluginLoader(): PluginLoader { TODO() }

    override fun getServer(): Server { TODO() }

    override fun isEnabled(): Boolean { TODO() }

    override fun onDisable() { TODO() }

    override fun onLoad() { TODO() }

    override fun onEnable() { TODO() }

    override fun isNaggable(): Boolean { TODO() }

    override fun setNaggable(canNag: Boolean) { TODO() }

    override fun getDefaultWorldGenerator(
        worldName: String,
        id: String?,
    ): ChunkGenerator? { TODO() }

    override fun getDefaultBiomeProvider(
        worldName: String,
        id: String?,
    ): BiomeProvider? { TODO() }

    override fun getLogger(): Logger { TODO() }

    override fun getName(): String { TODO() }

    override fun getLifecycleManager(): LifecycleEventManager<Plugin> { TODO() }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?,
    ): List<String?>? { TODO() }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?,
    ): Boolean { TODO() }
}
