package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.GroupResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.Result
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

class PageCommand: BukkitCommand<CommandSender> {

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
                        sender.sendMessage("No pages")
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

    fun goToPage(context: Invocation<BukkitContext, Player>, flagValue: String, intElement: Int, result: GroupResult<*>): ExecutionResult {
        result.on(id<String>("direction")) {

        }
        result.on(pageId) {
            return ExecutionResult.success()
        }
        return ExecutionResult.success()
    }

    fun playerRequiredSubCommand(context: Invocation<BukkitContext, Player>, string: String): ExecutionResult {
        return ExecutionResult.success()
    }
}

class PlayerRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<BukkitContext, Player>(id, "")
fun <O> SenderScope<BukkitContext, O>.playerRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<BukkitContext, Player, StringParameter<BukkitContext, Player>> =
    { PlayerRequiredStringParameter(id) }

class McpRequiredStringParameter(id: TypedIdentifier<String>) : StringParameter<BukkitContext, MinecraftProfile>(id, "")
fun <O> SenderScope<BukkitContext, O>.mcpRequiredStringParameter(id: TypedIdentifier<String>): StructureElement<BukkitContext, MinecraftProfile, StringParameter<BukkitContext, MinecraftProfile>> =
    { McpRequiredStringParameter(id) }

class TestFlag(): BukkitCommand<CommandSender> {

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

    fun execute(context: Invocation<BukkitContext, CommandSender>, f: String, ints: List<Int>): ExecutionResult {
        val id2: String by TypedIdentifier("hello", Int::class)

        return ExecutionResult.success()
    }
}

class SomeClass : BukkitCommand<CommandSender> {

    override val structure: StructureElement<BukkitContext, CommandSender, Structure<BukkitContext, CommandSender>>
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

        val flag1: StructureElement<BukkitContext, CommandSender, Flag<BukkitContext, CommandSender, Boolean>> = flag(
            id("raw")
        )

        val test = object : Parameter.Size1<BukkitContext, CommandSender, Boolean>(id(""), "") {
            context(senderContext: BukkitContext, invocation: Invocation<BukkitContext, CommandSender>)
            override fun parse(arg0: String): Result<out Boolean> {
                TODO("Not yet implemented")
            }
        }

        val flag: StructureElement<BukkitContext, CommandSender, Flag<BukkitContext, CommandSender, Boolean>> = flag(
            id("raw")
        )

        object : SenderScope<BukkitContext, Player> { }.playerRequiredStringParameter<Player>(
            id("worldgroup")
        )

        val structureElement: StructureElement<BukkitContext, CommandSender, Parameter.Size1<BukkitContext, CommandSender, String>> = {
            StringParameter<BukkitContext, CommandSender>(
                id("name"),
                "description",
            )
        }
        val structureElement2: StructureElement<BukkitContext, CommandSender, Parameter.FixedSize<BukkitContext, CommandSender, String>> = stringParameter<BukkitContext, CommandSender>(
            id("1234")
        )
        val structureElement3: StructureElement<BukkitContext, CommandSender, StringParameter<BukkitContext, CommandSender>> = stringParameter<BukkitContext, CommandSender>(
            id("1234")
        )

        valueFlag<BukkitContext, CommandSender, String>(
            id("wg"),
            default = "",
            { StringParameter<BukkitContext, CommandSender>(
                id("name"),
                "description",
            ) },
        )
    }
}

class McpRequiredIntParameter(id: TypedIdentifier<Int>) : IntParameter<BukkitContext, MinecraftProfile>(id, "", 0, 10)

class PlayerRequiredUnknownInt(name: String) : Parameter.UnknownSize<BukkitContext, Player, Int>(Size.Unbounded,
    id(""), "") {
    context(senderContext: BukkitContext, invocation: Invocation<BukkitContext, Player>)
    override fun parse(args: List<String>): Result<out Int> {
        TODO("Not yet implemented")
    }
}

class OrangeCommand : BukkitCommand<CommandSender> {
    override val structure: StructureElement<BukkitContext, CommandSender, Structure<BukkitContext, CommandSender>> =
        command("orange", requirement = permission("syn.orange"))(
            requireIs(Player::class) {
                    command("apple")(
                            playerRequiredStringParameter(id("someString"))
                    )
                }
        )
}

fun <O : Any> SenderScope<BukkitContext, O>.mcpSender(
    command: StructureElement<BukkitContext, MinecraftProfile, StructureElement<BukkitContext, MinecraftProfile, Structure<BukkitContext, MinecraftProfile>>>,
): StructureElement<BukkitContext, O, Structure<BukkitContext, O>> = requireAs<BukkitContext, O, MinecraftProfile>(
    { PlayerUtil.getProfile(it as Player) },
    command = command,
)

fun <T : Any> SenderScope<BukkitContext, CommandSender>.playerSender(
    // Outer StructureElement is to provide syntax compatibility with other extension functions w/ trailing lambda
    element: StructureElement<BukkitContext, Player, StructureElement<BukkitContext, Player, Parameter<BukkitContext, Player, T>>>,
): StructureElement<BukkitContext, CommandSender, Groupable<BukkitContext, CommandSender, T>> =
    requireIs<BukkitContext, CommandSender, Player, T>(Player::class, parameter = element)

class MinecraftProfile()

class PlayerUtil() {
    companion object {
        fun getProfile(player: Player) : MinecraftProfile {
            return null as MinecraftProfile
        }
    }
}
