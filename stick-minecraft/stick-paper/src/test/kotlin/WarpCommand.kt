package com.zombachu.stick.paper

import com.zombachu.stick.ExecutionContext
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.command
import com.zombachu.stick.defaultSender
import com.zombachu.stick.enumParameter
import com.zombachu.stick.flag
import com.zombachu.stick.group
import com.zombachu.stick.helper
import com.zombachu.stick.id
import com.zombachu.stick.impl.Aliasable
import com.zombachu.stick.impl.Structure
import com.zombachu.stick.impl.StructureElement
import com.zombachu.stick.invoke
import com.zombachu.stick.optionally
import com.zombachu.stick.requireAs
import com.zombachu.stick.requireIs
import com.zombachu.stick.stringParameter
import com.zombachu.stick.valueFlag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : BukkitCommand {

    private val warpParameterId = id<String>("warp")

    override val structure =
        command(
            name = "warp",
            aliases = setOf("home", "warps"),
            description = "The main command for warps",
        )(
            stringParameter(
                warpParameterId,
                description = "The warp to target",
            ),
            valueFlag(
                id("wg"),
                default = permissionedValue(
                    permission = "syn.secretwgs",
                    default = "secretworld",
                    fallback = "lobby"
                ),
                stringParameter(
                    id("worldgroup")
                ),
            ),
            group(
                requireIs(Player::class, permission("syn.warp.tp")) {
                    command(
                        name = "tp",
                        aliases = setOf("teleport"),
                        description = "Teleports you to a warp. If you have access to multiple warps with the same name, " +
                                "you will need to provide the full owner#warp name of the warp.",
                    )(
                        ::teleport,
                        helper(warpParameterId),
                        flag(id("raw")),
                        optionally(
                            defaultSender(),
                            playerParameter(
                                id("player"),
                                description = "The player to warp",
                            ),
                        ),
                    )
                },
                WarpInfoCommand().structure,
                requireAs<CommandSender, MinecraftProfile>(
                    { PlayerUtil.getProfile(it as Player) },
                    { it::class == Player::class },
                ) {
                    command("anothercommand")(
                            mcpRequiredStringParameter(id("playerStringParameter"))
                    )
                },
                requireIs(Player::class) {
                    stringParameter(id("blah"))
                },
                enumParameter(
                    id("rgb"),
                    Rgb::class
                ),
//                require(
//                    Player::class
//                ) {
//                    optionally(
//                        default = "hello",
//                        stringParameter(name("blah"))
//                    )
//                },
            ),
        )

    fun teleport(context: ExecutionContext<Player>, warp: String, isRaw: Boolean, player: Player): ExecutionResult {
        val warp: String = context.get(warpParameterId)
        return ExecutionResult.success()
    }
}

class WarpInfoCommand(): BukkitCommand {

    override val structure = mcpSender {
        command(
            name = "info",
            aliases = setOf("i"),
            description = "Displays information about a warp.",
        )(
            ::doSomething,
            valueFlag(
                id("wg"),
                default = "",
                stringParameter(
                    id("worldgroup")
                ),
            ),
            enumParameter(
                id("weather"),
                WeatherEnum::class
            ),
            optionally(
                default = 5,
                parameter = { McpRequiredIntParameter(id("mcpRequired")) }
            ),
        )
    }

    fun doSomething(context: ExecutionContext<MinecraftProfile>, wgFlag: String, weather: WeatherEnum, playerRequiredInt: Int): ExecutionResult {
        return ExecutionResult.success()
    }
}

class SomePlayerCommand(): PlayerCommand {
    override val structure: StructureElement<Player, Structure<Player>> =
        command("hey")(stringParameter(id("hi")))
}

enum class WeatherEnum(
    override val label: String,
    override val aliases: Set<String>,
) : Aliasable {
    Sun("sun", setOf("dryaf", "hot")),
    Rain("rain", setOf("rainy", "wet")),
    Storm("storm", setOf("stormy", "qgir7ewfubausdbf")),
}

enum class Rgb {
    Red, Green, Blue
}
