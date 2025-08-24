package com.zombachu.stick.paper

import com.zombachu.stick.Command
import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.SenderContext
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.flag
import com.zombachu.stick.structure.group
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.intParameter
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.listElementParameter
import com.zombachu.stick.structure.listParameter
import com.zombachu.stick.structure.literalParameter
import com.zombachu.stick.structure.require
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requireIs
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.valueFlag
import org.bukkit.entity.Player

class PageCommand: Command<BukkitContext> {

    val pageId = id<Int>("page")

    override val structure =
        requireIs(PlayerContext::class) {
            command(
                name = "page",
            )(
                execute = ::goToPage,
                valueFlag(
                    id("wg"),
                    default = { "huh" },
                    stringParameter(
                        id("worldgroup")
                    ),
                ),
                listElementParameter(
                    id("intElement"),
                    list = {
                        listOf(1, 2, 3)
                    },
                    onEmpty = {
                        this.senderContext.sender.sendMessage("No pages")
                        ExecutionResult.success()
                    }
                ),
                group(
                    literalParameter(
                        id("direction")
                    ),
                    intParameter(
                        pageId,
                    ),
                )
            )
        }

    fun goToPage(context: ExecutionContext<PlayerContext>, flagValue: String, intElement: Int, result: GroupResult<*>): ExecutionResult {
        result.on(id<String>("direction")) {

        }
        result.on(pageId) {
            return ExecutionResult.success()
        }
        return ExecutionResult.success()
    }

    fun playerRequiredSubCommand(context: ExecutionContext<PlayerContext>, string: String): ExecutionResult {
        return ExecutionResult.success()
    }
}

class PlayerRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<PlayerContext>(id, "")
fun <S : SenderContext> SenderScope<S>.playerRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<PlayerContext, StringParameter<PlayerContext>> =
    { PlayerRequiredStringParameter(id) }

class McpRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<MinecraftProfileContext>(id, "")
fun <S : SenderContext> SenderScope<S>.mcpRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<MinecraftProfileContext, StringParameter<MinecraftProfileContext>> =
    { McpRequiredStringParameter(id) }

class MinecraftProfileContext(override val sender: MinecraftProfile) : SenderContext


class TestFlag(): Command<BukkitContext> {

    override val structure =
        require(permission("syn.hi")) {
            command("hi")(
                ::execute,
                requireIs(
                    PlayerContext::class,
                    invalidDefault = "him"
                ) {
                    valueFlag(
                        id("hello"),
                        default = "blank",
                        stringParameter(id("blah"))
                    )
                },
                listParameter(
                    id("ints"),
                    parameter = intParameter("int")
                )
            )
        }

    fun execute(context: ExecutionContext<BukkitContext>, f: String, ints: List<Int>): ExecutionResult {
        val id2: String by TypedIdentifier("hello", Int::class)

        return ExecutionResult.success()
    }
}

class SomeClass : Command<BukkitContext> {

    override val structure: StructureElement<BukkitContext, Structure<BukkitContext>>
        get() = TODO("Not yet implemented")

    init {
//        object : SenderScope<BukkitSender, CommandSender>{ }
//            .requireSender<BukkitSender, CommandSender, BukkitSender, CommandSender, StructureElement<BukkitSender, CommandSender, Flag<BukkitSender, CommandSender, Boolean>>>(BukkitSender::class, CommandSender::class) {
////            flag(
////                name = "raw"
////            )
//                StructureElement {
//                    object : Element.FixedSizeElement<BukkitSender, CommandSender, String>(Size(1), "", "", "") {
//
//                    }
//                }
//        }

        val flag1: StructureElement<BukkitContext, Flag<BukkitContext, Boolean>> = flag(
            id("raw")
        )

        val test = object : Parameter.Size1<BukkitContext, Boolean>(id(""), "") {
            override fun parse(
                context: ExecutionContext<BukkitContext>,
                arg0: String,
            ): Result<Boolean> {
                TODO("Not yet implemented")
            }

        }

        val flag: StructureElement<BukkitContext, Flag<BukkitContext, Boolean>> = flag(
            id("raw")
        )

        object : SenderScope<PlayerContext> { }.playerRequiredStringParameter<PlayerContext>(
            id("worldgroup")
        )

        val structureElement: StructureElement<BukkitContext, Parameter.Size1<BukkitContext, String>> = {
            StringParameter<BukkitContext>(
                id("name"),
                "description",
            )
        }
        val structureElement2: StructureElement<BukkitContext, Parameter.FixedSize<BukkitContext, String>> = stringParameter<BukkitContext>(
            id("1234")
        )
        val structureElement3: StructureElement<BukkitContext, StringParameter<BukkitContext>> = stringParameter<BukkitContext>(
            id("1234")
        )

        valueFlag<BukkitContext, String>(
            id("wg"),
            default = "",
            { StringParameter<BukkitContext>(
                id("name"),
                "description",
            ) },
        )
    }
}



class McpRequiredIntParameter(id: TypedIdentifier<Int>) : IntParameter<MinecraftProfileContext>(id, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<PlayerContext, Int>(Size.Unbounded,
    id(""), "") {
    override fun parse(
        context: ExecutionContext<PlayerContext>,
        args: List<String>,
    ): Result<Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : Command<BukkitContext> {
    override val structure: StructureElement<BukkitContext, Structure<BukkitContext>> =
        command("orange", requirement = permission("syn.orange"))(
            requireIs(PlayerContext::class) {
                    command("apple")(
                            playerRequiredStringParameter(id("someString"))
                    )
                }
        )
}

fun <S : SenderContext> SenderScope<S>.mcpSender(
    command: StructureElement<MinecraftProfileContext, StructureElement<MinecraftProfileContext, Structure<MinecraftProfileContext>>>,
): StructureElement<S, Structure<S>> = requireAs(
    { MinecraftProfileContext(PlayerUtil.getProfile(it.sender as Player)) },
    command = command,
)

fun <T : Any> SenderScope<SenderContext>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureElement<PlayerContext, StructureElement<PlayerContext, Parameter<PlayerContext, T>>>,
): StructureElement<SenderContext, Groupable<SenderContext, T>> =
    requireIs(PlayerContext::class, parameter = element)

class MinecraftProfile()

class PlayerUtil() {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}
