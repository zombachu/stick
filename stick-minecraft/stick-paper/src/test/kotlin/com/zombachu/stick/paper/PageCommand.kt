package com.zombachu.stick.paper

import com.zombachu.stick.CommandResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.GroupResult2
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.element.ValueFlag
import com.zombachu.stick.element.parameters.IntParameter
import com.zombachu.stick.element.parameters.StringParameter
import com.zombachu.stick.impl.Arguments
import com.zombachu.stick.impl.Arguments0
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.paper.structure.permission
import com.zombachu.stick.structure.booleanParameter
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.flag
import com.zombachu.stick.structure.group
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.intParameter
import com.zombachu.stick.structure.invalidDefault
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.listElementParameter
import com.zombachu.stick.structure.listParameter
import com.zombachu.stick.structure.literalParameter
import com.zombachu.stick.structure.require
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requireIs
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.structure
import com.zombachu.stick.structure.textParameter
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PageCommand: BukkitCommand<CommandSender> {

    val pageId = id<Int>("page")

    override val structure by structure {
        requireIs(Player::class) {
            command(
                name = "page",
            )(
                valueFlag(
                    name = "wg",
                    default = { ParsingResult.success("huh") },
                    stringParameter("worldgroup"),
                ),
                listElementParameter(
                    "intElement",
                    list = {
                        ParsingResult.success(listOf(1, 2, 3))
                    },
                    onEmpty = {
                        sender.sendMessage("No pages")
                    }
                ),
                group(
                    literalParameter(
                        "direction"
                    ),
                    intParameter(
                        pageId.name,
                    ),
                ),
                execute = ::goToPage,
            )
        }
    }

    fun goToPage(context: Invocation<BukkitEnvironment, Player>, flagValue: String, intElement: Int, result: GroupResult2<String, Int>) {
        when (result) {
            is GroupResult.ResultA -> result.value.lowercase()
            is GroupResult.ResultB -> result.value + 10
        }
    }

    fun playerRequiredSubCommand(context: Invocation<BukkitEnvironment, Player>, string: String) {
    }
}

class PlayerRequiredStringParameter(name: String) : StringParameter<BukkitEnvironment, Player>(name, "")
fun <S> StructureScope<BukkitEnvironment, S>.playerRequiredStringParameter(name: String): StringParameter<BukkitEnvironment, Player> =
    PlayerRequiredStringParameter(name)

class McpRequiredStringParameter(name: String) : StringParameter<BukkitEnvironment, MinecraftProfile>(name, "")
fun <S> StructureScope<BukkitEnvironment, S>.mcpRequiredStringParameter(name: String): StringParameter<BukkitEnvironment, MinecraftProfile> =
    McpRequiredStringParameter(name)

class TestFlag: BukkitCommand<CommandSender> {

    override val structure by structure {
        require(permission("syn.hi")) {
            command("hi")(
                requireIs(
                    Player::class,
                    invalidDefault("him")
                ) {
                    valueFlag(
                        "hello",
                        default = "blank",
                        stringParameter("blah")
                    )
                },
                listParameter(
                    "ints",
                    parameter = intParameter("int")
                ),
                propertyReference,
            )
        }
    }

    private val propertyReference = fun Invocation<BukkitEnvironment, CommandSender>.(f: String, ints: List<Int>) {
        val id2: String by id<Int>("hello")
    }
}

class SomeClass : BukkitCommand<CommandSender> {

    override val structure: Structure<BukkitEnvironment, CommandSender, Arguments0>
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

        structure<BukkitEnvironment, CommandSender, Nothing> {
            val flag1: ValueFlag<BukkitEnvironment, CommandSender, Boolean> = flag(
                "raw"
            )


            val test = object : Parameter.Size1<BukkitEnvironment, CommandSender, Boolean>("", "") {
                context(inv: Invocation<BukkitEnvironment, CommandSender>)
                override fun parse(arg0: String): CommandResult<Boolean> {
                    TODO("Not yet implemented")
                }
            }

            StructureScope.empty<BukkitEnvironment, Player>().playerRequiredStringParameter<Player>(
                "worldgroup"
            )

            val structureElement: Parameter.Size1<BukkitEnvironment, CommandSender, String> = StringParameter<BukkitEnvironment, CommandSender>(
                "name",
                "description",
            )
            val structureElement2: Parameter.FixedSize<BukkitEnvironment, CommandSender, String> = stringParameter<BukkitEnvironment, CommandSender>(
                "1234"
            )
            val structureElement3: StringParameter<BukkitEnvironment, CommandSender> = stringParameter<BukkitEnvironment, CommandSender>(
                "1234"
            )

            valueFlag<BukkitEnvironment, CommandSender, String>(
                "wg",
                default = "",
                StringParameter<BukkitEnvironment, CommandSender>(
                    "name",
                    "description",
                )
            )
            TODO()
        }
    }
}

class McpRequiredIntParameter(name: String) : IntParameter<BukkitEnvironment, MinecraftProfile>(name, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<BukkitEnvironment, Player, Int>(Size.Unbounded,
    "", "") {
    context(inv: Invocation<BukkitEnvironment, Player>)
    override fun parse(args: List<String>): CommandResult<Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : BukkitCommand<CommandSender> {
    override val structure: Structure<BukkitEnvironment, CommandSender, *> by structure {
        command("orange", requirement = permission("syn.orange"))(
            requireIs(Player::class) {
                command("apple")(
                    playerRequiredStringParameter("someString")
                )
            }
        )
    }
}

fun <S : Any, T_ : Arguments> StructureScope<BukkitEnvironment, S>.mcpSender(
    command: StructureScope<BukkitEnvironment, MinecraftProfile>.() -> Structure<BukkitEnvironment, MinecraftProfile, T_>,
): Structure<BukkitEnvironment, S, T_> = requireAs<BukkitEnvironment, S, MinecraftProfile, T_>(
    { PlayerUtil.getProfile(it as Player) },
    command = command,
)

fun <T> StructureScope<BukkitEnvironment, CommandSender>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureScope<BukkitEnvironment, Player>.() -> Parameter.FixedSize<BukkitEnvironment, Player, T>,
): Groupable<BukkitEnvironment, CommandSender, T> =
    requireIs<BukkitEnvironment, CommandSender, Player, T>(Player::class, parameter = element)

class MinecraftProfile

class PlayerUtil {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}

class ExampleCommand : BukkitCommand<CommandSender> {
    override val structure by structure {
        command("example")(
            group(
                intParameter("someInt"),
                stringParameter("someString"),
                booleanParameter("someBoolean"),
            ),
            intParameter("postGroupInt"),
        ) { groupArg, postGroupIntArg ->
            when (groupArg) {
                is GroupResult.ResultA -> println(groupArg.value + 1)
                is GroupResult.ResultB -> println(groupArg.value.lowercase())
                is GroupResult.ResultC -> println(if (groupArg.value) true else false)
            }
        }
    }
}

class Example2Command : BukkitCommand<CommandSender> {
    override val structure by structure {
        command("example")(
            intParameter("postGroupInt"),
            group(
                intParameter("someInt"),
                textParameter("unboundedArg"),
                booleanParameter("someBoolean"),
            ),
        ) { postGroupIntArg, groupArg ->
            when (groupArg) {
                is GroupResult.ResultA -> println(groupArg.value + 1)
                is GroupResult.ResultB -> println(groupArg.value.lowercase())
                is GroupResult.ResultC -> println(if (groupArg.value) true else false)
            }
        }
    }
}
