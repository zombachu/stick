package com.zombachu.stick.paper

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.element.Flag
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.BuilderScope
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.paper.structure.permission
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
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PageCommand: BukkitCommand<CommandSender> {

    val pageId = id<Int>("page")

    override val structure =
        requireIs(Player::class) {
            command(
                name = "page",
            )(
                valueFlag(
                    id("wg"),
                    default = { ParsingResult.success("huh") },
                    stringParameter(
                        id("worldgroup")
                    ),
                ),
                listElementParameter(
                    id("intElement"),
                    list = {
                        ParsingResult.success(listOf(1, 2, 3))
                    },
                    onEmpty = {
                        sender.sendMessage("No pages")
                    }
                ),
                group(
                    literalParameter(
                        id("direction")
                    ),
                    intParameter(
                        pageId,
                    ),
                ),
                execute = ::goToPage,
            )
        }

    fun goToPage(context: Invocation<BukkitEnvironment, Player>, flagValue: String, intElement: Int, result: GroupResult<*>) {
        result.on(id<String>("direction")) {

        }
        result.on(pageId) {
        }
    }

    fun playerRequiredSubCommand(context: Invocation<BukkitEnvironment, Player>, string: String) {
    }
}

class PlayerRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<BukkitEnvironment, Player>(id, "")
fun <S> BuilderScope<BukkitEnvironment, S>.playerRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<BukkitEnvironment, Player, StringParameter<BukkitEnvironment, Player>> =
    { PlayerRequiredStringParameter(id) }

class McpRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<BukkitEnvironment, MinecraftProfile>(id, "")
fun <S> BuilderScope<BukkitEnvironment, S>.mcpRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<BukkitEnvironment, MinecraftProfile, StringParameter<BukkitEnvironment, MinecraftProfile>> =
    { McpRequiredStringParameter(id) }

class TestFlag: BukkitCommand<CommandSender> {

    override val structure =
        require(permission("syn.hi")) {
            command("hi")(
                requireIs(
                    Player::class,
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
                ),
                ::execute,
            )
        }

    fun execute(context: Invocation<BukkitEnvironment, CommandSender>, f: String, ints: List<Int>) {
        val id2: String by id<Int>("hello")
    }
}

class SomeClass : BukkitCommand<CommandSender> {

    override val structure: StructureElement<BukkitEnvironment, CommandSender, Structure<BukkitEnvironment, CommandSender>>
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

        val flag1: StructureElement<BukkitEnvironment, CommandSender, Flag<BukkitEnvironment, CommandSender, Boolean>> = flag(
            id("raw")
        )

        val test = object : Parameter.Size1<BukkitEnvironment, CommandSender, Boolean>(id(""), "") {
            context(inv: Invocation<BukkitEnvironment, CommandSender>)
            override fun parse(arg0: String): CommandResult<Boolean> {
                TODO("Not yet implemented")
            }
        }

        val flag: StructureElement<BukkitEnvironment, CommandSender, Flag<BukkitEnvironment, CommandSender, Boolean>> = flag(
            id("raw")
        )

        object : BuilderScope<BukkitEnvironment, Player> { }.playerRequiredStringParameter<Player>(
            id("worldgroup")
        )

        val structureElement: StructureElement<BukkitEnvironment, CommandSender, Parameter.Size1<BukkitEnvironment, CommandSender, String>> = {
            StringParameter<BukkitEnvironment, CommandSender>(
                id("name"),
                "description",
            )
        }
        val structureElement2: StructureElement<BukkitEnvironment, CommandSender, Parameter.FixedSize<BukkitEnvironment, CommandSender, String>> = stringParameter<BukkitEnvironment, CommandSender>(
            id("1234")
        )
        val structureElement3: StructureElement<BukkitEnvironment, CommandSender, StringParameter<BukkitEnvironment, CommandSender>> = stringParameter<BukkitEnvironment, CommandSender>(
            id("1234")
        )

        valueFlag<BukkitEnvironment, CommandSender, String>(
            id("wg"),
            default = "",
            { StringParameter<BukkitEnvironment, CommandSender>(
                id("name"),
                "description",
            ) },
        )
    }
}

class McpRequiredIntParameter(id: TypedIdentifier<Int>) : IntParameter<BukkitEnvironment, MinecraftProfile>(id, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<BukkitEnvironment, Player, Int>(Size.Unbounded,
    id(""), "") {
    context(inv: Invocation<BukkitEnvironment, Player>)
    override fun parse(args: List<String>): CommandResult<Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : BukkitCommand<CommandSender> {
    override val structure: StructureElement<BukkitEnvironment, CommandSender, Structure<BukkitEnvironment, CommandSender>> =
        command("orange", requirement = permission("syn.orange"))(
            requireIs(Player::class) {
                    command("apple")(
                            playerRequiredStringParameter(id("someString"))
                    )
                }
        )
}

fun <S : Any> BuilderScope<BukkitEnvironment, S>.mcpSender(
    command: StructureElement<BukkitEnvironment, MinecraftProfile, StructureElement<BukkitEnvironment, MinecraftProfile, Structure<BukkitEnvironment, MinecraftProfile>>>,
): StructureElement<BukkitEnvironment, S, Structure<BukkitEnvironment, S>> = requireAs<BukkitEnvironment, S, MinecraftProfile>(
    { PlayerUtil.getProfile(it as Player) },
    command = command,
)

fun <T : Any> BuilderScope<BukkitEnvironment, CommandSender>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureElement<BukkitEnvironment, Player, StructureElement<BukkitEnvironment, Player, Parameter<BukkitEnvironment, Player, T>>>,
): StructureElement<BukkitEnvironment, CommandSender, Groupable<BukkitEnvironment, CommandSender, T>> =
    requireIs<BukkitEnvironment, CommandSender, Player, T>(Player::class, parameter = element)

class MinecraftProfile

class PlayerUtil {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}
