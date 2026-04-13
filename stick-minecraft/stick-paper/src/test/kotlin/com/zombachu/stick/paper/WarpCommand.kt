package com.zombachu.stick.paper

import com.zombachu.stick.Aliasable
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.element.Element
import com.zombachu.stick.impl.Arguments1
import com.zombachu.stick.impl.Arguments3
import com.zombachu.stick.impl.StructureScope
import com.zombachu.stick.paper.structure.permission
import com.zombachu.stick.paper.structure.permissionedValue
import com.zombachu.stick.paper.structure.playerParameter
import com.zombachu.stick.structure.command
import com.zombachu.stick.structure.default
import com.zombachu.stick.structure.defaultSender
import com.zombachu.stick.structure.enumFlag
import com.zombachu.stick.structure.enumParameter
import com.zombachu.stick.structure.flag
import com.zombachu.stick.structure.group
import com.zombachu.stick.structure.helper
import com.zombachu.stick.structure.hybridFlag
import com.zombachu.stick.structure.id
import com.zombachu.stick.structure.invalidDefault
import com.zombachu.stick.structure.invoke
import com.zombachu.stick.structure.nullableEnumFlag
import com.zombachu.stick.structure.optionally
import com.zombachu.stick.structure.pipeline
import com.zombachu.stick.structure.require
import com.zombachu.stick.structure.requireAs
import com.zombachu.stick.structure.requireIs
import com.zombachu.stick.structure.requirement
import com.zombachu.stick.structure.store
import com.zombachu.stick.structure.stringParameter
import com.zombachu.stick.structure.structure
import com.zombachu.stick.structure.valueFlag
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : BukkitCommand<CommandSender> {

    private val warpParameterId = id<String>("warp")

    override val structure = structure {
        command(
            name = "warp",
            aliases = setOf("home", "warps"),
            description = "The main command for warps",
        )(
            stringParameter(
                warpParameterId.name,
                description = "The warp to target",
            ),
            valueFlag(
                "wg",
                default = permissionedValue(
                    permission = "syn.secretwgs",
                    default = "secretworld",
                    fallback = "lobby"
                ),
                stringParameter(
                    "worldgroup"
                ),
            ),
            group(
                requireIs(
                    Player::class,
                    permission("syn.warp.tp")
                ) {
                    command(
                        name = "tp",
                        aliases = setOf("teleport"),
                        description = "Teleports you to a warp. If you have access to multiple warps with the same name, " +
                                "you will need to provide the full owner#warp name of the warp.",
                    )(
                        helper(warpParameterId),
                        flag("raw"),
                        optionally(
                            defaultSender(),
                            parameter = playerParameter(
                                "player",
                                description = "The player to warp",
                            ),
                        ),
                        ::teleport,
                    )
                },
                WarpInfoCommand().structure,
                requireAs<BukkitEnvironment, CommandSender, MinecraftProfile, Arguments1<String>>(
                    { PlayerUtil.getProfile(it as Player) },
                    requirement { it.sender is Player },
                ) {
                    command("anothercommand")(
                        mcpRequiredStringParameter("playerStringParameter")
                    )
                },
                requireIs(Player::class) {
                    stringParameter("blah")
                },
                enumParameter(
                    "rgb",
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
        ) {
                a: String,
                b: String,
                c: GroupResult5<
                        Arguments3<String, Boolean, Player>,
                        Arguments3<String, WeatherEnum, Int>,
                        Arguments1<String>,
                        String,
                        Rgb>,
            ->

        }
    }

    fun teleport(context: Invocation<BukkitEnvironment, Player>, warp: String, isRaw: Boolean, player: Player) {
        val warp: String = context.get(warpParameterId)
    }
}

class WarpInfoCommand: BukkitCommand<CommandSender> {

    override val structure = structure {
        mcpSender {
            command(
                name = "info",
                aliases = setOf("i"),
                description = "Displays information about a warp.",
            )(
                valueFlag(
                    "wg",
                    default = "",
                    stringParameter(
                        "worldgroup"
                    ),
                ),
                enumParameter(
                    "weather",
                    WeatherEnum::class
                ),
                optionally(
                    ifAbsent = default(5),
                    parameter = McpRequiredIntParameter("mcpRequired")
                ),
            ) { wgFlag: String, weather: WeatherEnum, playerRequiredInt: Int ->
            }
        }
    }
}

class SomePlayerCommand: BukkitCommand<Player> {
    override val structure = structure {
        command("hey")(
            stringParameter("hello").pipeline(
                { ParsingResult.success(it.toInt()) },
                { ParsingResult.success(it * 5f) },
                { ParsingResult.success(it.toDouble()) },
            ),
            flag(
                name = "there",
                aliases = setOf("a", "b", "c", "d"),
                description = "yup",
            ).pipeline(
                { ParsingResult.success(if (it) 1 else 0) },
                { ParsingResult.success(it * 5f) },
                { ParsingResult.success(it.toDouble()) },
            ),
            optionally(
                ifAbsent = default(10f),
                parameter = stringParameter(
                    name = "num",
                    description = "Number as a string for some reason"
                ).pipeline(
                    { ParsingResult.success(it.toInt()) },
                    { ParsingResult.success(it / 2f) }
                )
            )
        ) { hello: Double, there: Double, num: Float ->

        }
    }
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

fun <E : BasicBukkitEnvironment> StructureScope<E, CommandSender>.targetPlayer(

): Element<E, CommandSender, Player> =
    optionally(
        ifAbsent = defaultSender<E, CommandSender, Player>(),
        parameter = playerParameter("player", "The player to explode.").pipeline {
            if (!it.hasPermission("some_permission")) {
                ParsingResult.failSyntax("bad permission")
            } else {
                ParsingResult.success(it)
            }
        }
    )

class ColorPlayerCommand: BukkitCommand<Player> {
    override val structure = structure {
        command("asdf")(
            enumFlag(
                Rgb.Red,
                enumParameter("color", Rgb::class),
            ).store(id("storedColor")),
            nullableEnumFlag(
                enumParameter("otherColor", Rgb::class)
            ),
            require(
                invalidDefault(false, permission("some_permission"))
            ) {
                flag("hi")
            }
        ) { color: Rgb, other: Rgb?, fl: Boolean ->

        }
    }
}

class HybridFlagCommand: BukkitCommand<CommandSender> {
    override val structure = structure {
        command("asdf")(
            hybridFlag(
                "color",
                enumParameter("color", Rgb::class),
            ),
        ) { color: HybridFlagResult<Rgb> ->
            when (color) {
                is HybridFlagResult.Absent -> TODO()
                is HybridFlagResult.Present -> TODO()
                is HybridFlagResult.Value -> color.value.name
            }
        }
    }
}

class HybridFlagRequireCommand: BukkitCommand<CommandSender> {
    override val structure = structure {
        command("asdf")(
            requireIs(Player::class, invalidDefault(HybridFlagResult.Value(Rgb.Blue))) {
                hybridFlag(
                    "color",
                    enumParameter("color", Rgb::class),
                )
            },
        ) { color: HybridFlagResult<Rgb> ->
            when (color) {
                is HybridFlagResult.Absent -> TODO()
                is HybridFlagResult.Present -> TODO()
                is HybridFlagResult.Value -> color.value.name
            }
        }
    }
}
