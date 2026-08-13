package com.zombachu.stick.paper.legacy

import com.zombachu.stick.Aliasable
import com.zombachu.stick.Arguments1
import com.zombachu.stick.Arguments3
import com.zombachu.stick.GroupResult5
import com.zombachu.stick.HybridFlagResult
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.defaultSender
import com.zombachu.stick.dsl.enumFlag
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.id
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.nullableEnumFlag
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.pipeline
import com.zombachu.stick.dsl.require
import com.zombachu.stick.dsl.requireAs
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.dsl.store
import com.zombachu.stick.dsl.stringParameter
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.element.Element
import com.zombachu.stick.paper.BasicBukkitEnvironment
import com.zombachu.stick.paper.BukkitCommand
import com.zombachu.stick.paper.BukkitEnvironment
import com.zombachu.stick.paper.dsl.permission
import com.zombachu.stick.paper.dsl.permissionedValue
import com.zombachu.stick.paper.dsl.playerParameter
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WarpCommand : BukkitCommand<CommandSender> {

    private val warpParameterId = id<String>("warp")

    override val structure = structure {
        command(
            name = "warp",
            aliases = ["home", "warps"],
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
                        aliases = ["teleport"],
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
                aliases = ["i"],
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
                aliases = ["a", "b", "c", "d"],
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
    Sun("sun", ["dryaf", "hot"]),
    Rain("rain", ["rainy", "wet"]),
    Storm("storm", ["stormy", "qgir7ewfubausdbf"]),
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
