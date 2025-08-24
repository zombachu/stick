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
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PageCommand: Command<CommandSender, BukkitContext<CommandSender>> {

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

    fun goToPage(context: ExecutionContext<Player, BukkitContext<Player>>, flagValue: String, intElement: Int, result: GroupResult<*>): ExecutionResult {
        result.on(id<String>("direction")) {

        }
        result.on(pageId) {
            return ExecutionResult.success()
        }
        return ExecutionResult.success()
    }

    fun playerRequiredSubCommand(context: ExecutionContext<Player, BukkitContext<Player>>, string: String): ExecutionResult {
        return ExecutionResult.success()
    }
}

class PlayerRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<Player, BukkitContext<Player>>(id, "")
fun <O, S : BukkitContext<O>> SenderScope<O, S>.playerRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<Player, BukkitContext<Player>, StringParameter<Player, BukkitContext<Player>>> =
    { PlayerRequiredStringParameter(id) }

class McpRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<MinecraftProfile, BukkitContext<MinecraftProfile>>(id, "")
fun <O, S : BukkitContext<O>> SenderScope<O, S>.mcpRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<MinecraftProfile, BukkitContext<MinecraftProfile>, StringParameter<MinecraftProfile, BukkitContext<MinecraftProfile>>> =
    { McpRequiredStringParameter(id) }

class TestFlag(): Command<CommandSender, BukkitContext<CommandSender>> {

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
                    parameter = intParameter("int")
                )
            )
        }

    fun execute(context: ExecutionContext<CommandSender, BukkitContext<CommandSender>>, f: String, ints: List<Int>): ExecutionResult {
        val id2: String by TypedIdentifier("hello", Int::class)

        return ExecutionResult.success()
    }
}

class SomeClass : Command<CommandSender, BukkitContext<CommandSender>> {

    override val structure: StructureElement<CommandSender, BukkitContext<CommandSender>, Structure<CommandSender, BukkitContext<CommandSender>>>
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

        val flag1: StructureElement<CommandSender, BukkitContext<CommandSender>, Flag<CommandSender, BukkitContext<CommandSender>, Boolean>> = flag(
            id("raw")
        )

        val test = object : Parameter.Size1<CommandSender, BukkitContext<CommandSender>, Boolean>(id(""), "") {
            override fun parse(
                context: ExecutionContext<CommandSender, BukkitContext<CommandSender>>,
                arg0: String,
            ): Result<Boolean> {
                TODO("Not yet implemented")
            }

        }

        val flag: StructureElement<CommandSender, BukkitContext<CommandSender>, Flag<CommandSender, BukkitContext<CommandSender>, Boolean>> = flag(
            id("raw")
        )

        object : SenderScope<Player, BukkitContext<Player>> { }.playerRequiredStringParameter<Player, BukkitContext<Player>>(
            id("worldgroup")
        )

        val structureElement: StructureElement<CommandSender, BukkitContext<CommandSender>, Parameter.Size1<CommandSender, BukkitContext<CommandSender>, String>> = {
            StringParameter<CommandSender, BukkitContext<CommandSender>>(
                id("name"),
                "description",
            )
        }
        val structureElement2: StructureElement<CommandSender, BukkitContext<CommandSender>, Parameter.FixedSize<CommandSender, BukkitContext<CommandSender>, String>> = stringParameter<CommandSender, BukkitContext<CommandSender>>(
            id("1234")
        )
        val structureElement3: StructureElement<CommandSender, BukkitContext<CommandSender>, StringParameter<CommandSender, BukkitContext<CommandSender>>> = stringParameter<CommandSender, BukkitContext<CommandSender>>(
            id("1234")
        )

        valueFlag<CommandSender, BukkitContext<CommandSender>, String>(
            id("wg"),
            default = "",
            { StringParameter<CommandSender, BukkitContext<CommandSender>>(
                id("name"),
                "description",
            ) },
        )
    }
}

class McpRequiredIntParameter(id: TypedIdentifier<Int>) : IntParameter<MinecraftProfile, BukkitContext<MinecraftProfile>>(id, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<Player, BukkitContext<Player>, Int>(Size.Unbounded,
    id(""), "") {
    override fun parse(
        context: ExecutionContext<Player, BukkitContext<Player>>,
        args: List<String>,
    ): Result<Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : Command<CommandSender, BukkitContext<CommandSender>> {
    override val structure: StructureElement<CommandSender, BukkitContext<CommandSender>, Structure<CommandSender, BukkitContext<CommandSender>>> =
        command("orange", requirement = permission("syn.orange"))(
            requireIs(Player::class) {
                    command("apple")(
                            playerRequiredStringParameter(id("someString"))
                    )
                }
        )
}

fun <O, S : BukkitContext<O>> SenderScope<O, S>.mcpSender(
    command: StructureElement<MinecraftProfile, BukkitContext<MinecraftProfile>, StructureElement<MinecraftProfile, BukkitContext<MinecraftProfile>, Structure<MinecraftProfile, BukkitContext<MinecraftProfile>>>>,
): StructureElement<O, S, Structure<O, S>> = requireAs(
    { PlayerUtil.getProfile(it as Player) },
    command = command,
)

fun <O, S : BukkitContext<O>, T : Any> SenderScope<O, S>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureElement<Player, BukkitContext<Player>, StructureElement<Player, BukkitContext<Player>, Parameter<Player, BukkitContext<Player>, T>>>,
): StructureElement<CommandSender, BukkitContext<CommandSender>, Groupable<CommandSender, BukkitContext<CommandSender>, T>> =
    requireIs(Player::class, parameter = element)

class MinecraftProfile()

class PlayerUtil() {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}
