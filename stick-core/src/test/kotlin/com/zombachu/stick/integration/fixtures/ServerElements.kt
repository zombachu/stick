@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.Requirement
import com.zombachu.stick.SenderValidationResult
import com.zombachu.stick.Size
import com.zombachu.stick.StructureScope
import com.zombachu.stick.dsl.defaultSender
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.requireAs
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.element.Groupable
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.feedback.CustomFeedback
import kotlin.experimental.ExperimentalTypeInference

class CustomError(message: String) : ParsingResult.CustomError<CustomFeedback> {
    override val feedback: CustomFeedback = CustomFeedback { message }
}

// --- requirements -------------------------------------------------------------------------------------------------

fun <E : Environment, S : Sender> StructureScope<E, S>.permission(
    node: String,
): Requirement<E, S> = requirement(SenderValidationResult::failPermission) { it.sender.hasPermission(node) }

fun <E : Environment, S : Sender, T> StructureScope<E, S>.permissionedValue(
    node: String,
    value: T,
    fallback: T,
): ContextualValue<E, S, T> = { ParsingResult.success(if (sender.hasPermission(node)) value else fallback) }

// --- parameters ---------------------------------------------------------------------------------------------------

class PlayerParameter<E : Server, S>(name: String) : Parameter.Size1<E, S, Player>(name, "") {
    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<Player> {
        val player = inv.env.getPlayer(arg0) ?: return ParsingResult.failType("player", arg0)
        return ParsingResult.success(player)
    }
}

fun <E : Server, S> StructureScope<E, S>.playerParameter(name: String): PlayerParameter<E, S> = PlayerParameter(name)

fun <E : Server> StructureScope<E, Sender>.targetPlayerParameter(
    name: String,
): OptionalParameter<E, Sender, Player> =
    optionally(
        ifAbsent = defaultSender<E, Sender, Player>(),
        parameter = playerParameter(name)
    )

class WarpParameter<E : WarpableServer, S>(name: String) : Parameter.Size1<E, S, Warp>(name, "") {
    context(inv: Invocation<E, S>)
    override fun parse(arg0: String): CommandResult<Warp> {
        val warp = inv.env.warps[arg0] ?: return CustomError("Unknown warp: $arg0")
        return ParsingResult.success(warp)
    }
}

fun <E : WarpableServer, S> StructureScope<E, S>.warpParameter(name: String): WarpParameter<E, S> = WarpParameter(name)

class RealNameParameter<E : Environment>(name: String) : Parameter.Size1<E, SocialData, String>(name, "") {
    context(inv: Invocation<E, SocialData>)
    override fun parse(arg0: String): CommandResult<String> {
        val nicknameEntry = inv.sender.nicknames.entries.find { it.value == arg0 }
            ?: return CustomError("Unknown nickname: $arg0")
        return ParsingResult.success(nicknameEntry.key)
    }
}

fun <E : Environment, S> StructureScope<E, S>.realNameParameter(
    name: String,
): RealNameParameter<E> = RealNameParameter(name)

class BioParameter<E : Environment>(name: String) :
    Parameter.UnknownSize<E, SocialData, String>(Size.atLeast(1), name, "") {

    context(inv: Invocation<E, SocialData>)
    override fun parse(args: List<String>): CommandResult<String> {
        val bioLine = args.joinToString(" ")
        return ParsingResult.success(bioLine, Size(args.size))
    }
}

fun <E : Environment, S> StructureScope<E, S>.bioParameter(name: String): BioParameter<E> = BioParameter(name)

// --- requires -----------------------------------------------------------------------------------------------------

@OverloadResolutionByLambdaReturnType
fun <E : Environment, T_ : Arguments> StructureScope<E, Sender>.requireSocialData(
    command: StructureScope<E, SocialData>.() -> Structure<E, SocialData, T_>
): Structure<E, Sender, T_> =
    requireAs(
        { (it as Player).socialData },
        requirement { it.sender is Player },
        command,
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireSocialDataUnknownSizeParameter")
fun <E : Environment, T> StructureScope<E, Sender>.requireSocialData(
    parameter: StructureScope<E, SocialData>.() -> Parameter.UnknownSize<E, SocialData, T>
): Groupable<E, Sender, T> =
    requireAs(
        { (it as Player).socialData },
        requirement { it.sender is Player },
        parameter
    )

@OverloadResolutionByLambdaReturnType
@JvmName("requireSocialDataFixedSizeParameter")
fun <E : Environment, T> StructureScope<E, Sender>.requireSocialData(
    parameter: StructureScope<E, SocialData>.() -> Parameter.FixedSize<E, SocialData, T>
): Groupable<E, Sender, T> =
    requireAs(
        { (it as Player).socialData },
        requirement { it.sender is Player },
        parameter
    )

// --- helpers ------------------------------------------------------------------------------------------------------

fun <E : Environment, S : Player> StructureScope<E, S>.socialDataHelper(): Helper<E, S, SocialData> = helper {
    ParsingResult.success(sender.socialData)
}

fun <E : Environment, S : Player> StructureScope<E, S>.worldHelper(): Helper<E, S, String> = helper {
    ParsingResult.success(sender.world)
}
