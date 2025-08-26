package com.zombachu.stick.paper

import com.zombachu.stick.Aliasable
import com.zombachu.stick.ExecutionResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.defaultSender
import com.zombachu.stick.structure.enumParameter
import com.zombachu.stick.structure.flag
import com.zombachu.stick.structure.group
import com.zombachu.stick.structure.helper
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.optionally
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requireIs
import com.zombachu.stick.structure.requirement
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : BukkitCommand<CommandSender> {

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
                name = "wg",
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
                requireIs(
                    Player::class,
                    permission("syn.warp.tp")) {
                    command(
                        name = "tp",
                        aliases = setOf("teleport"),
                        description = "Teleports you to a warp. If you have access to multiple warps with the same name, " +
                                "you will need to provide the full owner#warp name of the warp.",
                    )(
                        ::teleport,
                        helper(warpParameterId),
                        flag("raw"),
                        optionally(
                            defaultSender(),
                            parameter = playerParameter(
                                "player",
                                description = "The player to warp",
                            ),
                        ),
                    )
                },
                WarpInfoCommand().structure,
                requireAs<BukkitContext, CommandSender, MinecraftProfile>(
                    { PlayerUtil.getProfile(it as Player) },
                    requirement { it.sender is Player },
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

    fun teleport(context: Invocation<BukkitContext, Player>, warp: String, isRaw: Boolean, player: Player): ExecutionResult {
        val warp: String = context.get(warpParameterId)
        return ExecutionResult.success()
    }
}

class WarpInfoCommand(): BukkitCommand<CommandSender> {

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

    fun doSomething(context: Invocation<BukkitContext, MinecraftProfile>, wgFlag: String, weather: WeatherEnum, playerRequiredInt: Int): ExecutionResult {
        return ExecutionResult.success()
    }
}

class SomePlayerCommand(): BukkitCommand<Player> {
    override val structure =
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
