package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Result
import com.zombachu.stick.TypedIdentifier
import com.zombachu.stick.command
import com.zombachu.stick.flag
import com.zombachu.stick.group
import com.zombachu.stick.id
import com.zombachu.stick.impl.Flag
import com.zombachu.stick.impl.Groupable
import com.zombachu.stick.impl.Parameter
import com.zombachu.stick.impl.SenderScope
import com.zombachu.stick.impl.Size
import com.zombachu.stick.impl.Structure
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.intParameter
import com.zombachu.stick.invoke
import com.zombachu.stick.listElementParameter
import com.zombachu.stick.listParameter
import com.zombachu.stick.literalParameter
import com.zombachu.stick.parameters.IntParameter
import com.zombachu.stick.parameters.StringParameter
import com.zombachu.stick.require
import com.zombachu.stick.requireAs
import com.zombachu.stick.requireIs
import com.zombachu.stick.stringParameter
import com.zombachu.stick.valueFlag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PageCommand: BukkitCommand {

    val pageId = id<Int>("page")

    override val structure =
        requireIs(Player::class) {
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

    fun goToPage(context: ExecutionContext<Player>, flagValue: String, intElement: Int, result: GroupResult<*>): ExecutionResult {
        result.on(id<String>("direction")) {

        }
        result.on(pageId) {
            return ExecutionResult.success()
        }
        return ExecutionResult.success()
    }

    fun playerRequiredSubCommand(context: ExecutionContext<Player>, string: String): ExecutionResult {
        return ExecutionResult.success()
    }
}

class PlayerRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<Player>(id, "")
fun <S> SenderScope<S>.playerRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<Player, StringParameter<Player>> =
    { PlayerRequiredStringParameter(id) }

class McpRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<MinecraftProfile>(id, "")
fun <S> SenderScope<S>.mcpRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<MinecraftProfile, StringParameter<MinecraftProfile>> =
    { McpRequiredStringParameter(id) }


class TestFlag(): BukkitCommand {

    override val structure =
        require(permission("syn.hi")) {
            command("hi")(
                ::execute,
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
                    parameter = intParameter(id("int"))
                )
            )
        }

    fun execute(context: ExecutionContext<CommandSender>, f: String, ints: List<Int>): ExecutionResult {
        val id2: String by TypedIdentifier("hello", Int::class)

        return ExecutionResult.success()
    }
}

class SomeClass : BukkitCommand {

    override val structure: StructureElement<CommandSender, Structure<CommandSender>>
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

        val flag1: StructureElement<CommandSender, Flag<CommandSender, Boolean>> = flag(
            id("raw")
        )

        val test = object : Parameter.Size1<CommandSender, Boolean>(id(""), "") {
            override fun parse(
                context: ExecutionContext<CommandSender>,
                arg0: String,
            ): Result<Boolean> {
                TODO("Not yet implemented")
            }

        }

        val flag: StructureElement<CommandSender, Flag<CommandSender, Boolean>> = flag(
            id("raw")
        )

        object : SenderScope<Player> { }.playerRequiredStringParameter<Player>(
            id("worldgroup")
        )

        val structureElement: StructureElement<CommandSender, Parameter.Size1<CommandSender, String>> = {
            StringParameter<CommandSender>(
                id("name"),
                "description",
            )
        }
        val structureElement2: StructureElement<CommandSender, Parameter.FixedSize<CommandSender, String>> = stringParameter<CommandSender>(
            id("1234")
        )
        val structureElement3: StructureElement<CommandSender, StringParameter<CommandSender>> = stringParameter<CommandSender>(
            id("1234")
        )

        valueFlag<CommandSender, String>(
            id("wg"),
            default = "",
            { StringParameter<CommandSender>(
                id("name"),
                "description",
            ) },
        )
    }
}



class McpRequiredIntParameter(id: TypedIdentifier<Int>) : IntParameter<MinecraftProfile>(id, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<Player, Int>(Size.Unbounded,
    id(""), "") {
    override fun parse(
        context: ExecutionContext<Player>,
        args: List<String>,
    ): Result<Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : BukkitCommand {
    override val structure: StructureElement<CommandSender, Structure<CommandSender>> =
        command("orange", requirement = permission("syn.orange"))(
            requireIs(Player::class) {
                    command("apple")(
                            playerRequiredStringParameter(id("someString"))
                    )
                }
        )
}

fun <S> SenderScope<S>.mcpSender(
    command: StructureElement<MinecraftProfile, StructureElement<MinecraftProfile, Structure<MinecraftProfile>>>,
): StructureElement<S, Structure<S>> = requireAs(
    { PlayerUtil.getProfile(it as Player) },
    command = command,
)

fun <T : Any> SenderScope<CommandSender>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureElement<Player, StructureElement<Player, Parameter<Player, T>>>,
): StructureElement<CommandSender, Groupable<CommandSender, T>> =
    requireIs(Player::class, parameter = element)

class MinecraftProfile()

class PlayerUtil() {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}
