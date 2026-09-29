@file:OptIn(ExperimentalTypeInference::class)

package com.zombachu.stick.integration.fixtures

import com.zombachu.stick.Arguments
import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.ContextualValue
import com.zombachu.stick.Environment
import com.zombachu.stick.Invocation
import com.zombachu.stick.MessageReason
import com.zombachu.stick.Position
import com.zombachu.stick.Requirement
import com.zombachu.stick.Size
import com.zombachu.stick.StructureScope
import com.zombachu.stick.Suggestion
import com.zombachu.stick.consuming
import com.zombachu.stick.dsl.defaultSender
import com.zombachu.stick.dsl.helper
import com.zombachu.stick.dsl.mapSender
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.requireSender
import com.zombachu.stick.dsl.requirement
import com.zombachu.stick.element.GatedParameter
import com.zombachu.stick.element.Helper
import com.zombachu.stick.element.OptionalParameter
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.element.Structure
import com.zombachu.stick.fail
import com.zombachu.stick.failPermission
import com.zombachu.stick.failType
import com.zombachu.stick.failure.CustomReason
import com.zombachu.stick.failure.FailureOrigin
import com.zombachu.stick.success
import com.zombachu.stick.toSuggestions
import kotlin.experimental.ExperimentalTypeInference

data class UnknownWarp(val name: String) : CustomReason {
    override fun message(origin: FailureOrigin) = "Unknown warp: $name"
}

context(_: Invocation<*, *>)
fun customError(message: String): CommandResult.Failure.Error = fail(MessageReason(message))

// --- requirements -------------------------------------------------------------------------------------------------

fun <E : Environment, S : Sender> StructureScope<E, S>.permission(
    node: String,
): Requirement<E, S> = requirement({ sender.hasPermission(node) }) { failPermission() }

fun <E : Environment, S : Sender, T> StructureScope<E, S>.permissionedValue(
    node: String,
    value: T,
    fallback: T,
): ContextualValue<E, S, T> = { success(if (sender.hasPermission(node)) value else fallback) }

// --- parameters ---------------------------------------------------------------------------------------------------

class PlayerParameter<E : Server, S>(name: String) : Parameter.Size1<E, S, Player>(name, "") {
    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        inv.env.playerNames.toSuggestions()

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<Player> {
        val player = inv.env.getPlayer(arg0) ?: return failType("player", arg0)
        return success(player)
    }
}

fun <E : Server, S> StructureScope<E, S>.playerParameter(name: String): PlayerParameter<E, S> = PlayerParameter(name)

fun <E : Server> StructureScope<E, Sender>.targetPlayerParameter(
    name: String,
): OptionalParameter<E, Sender, Player, Position.Optional> =
    optionally(
        parameter = playerParameter(name),
        ifAbsent = defaultSender<E, Sender, Player>()
    )

class WarpParameter<E : WarpableServer, S>(name: String) : Parameter.Size1<E, S, Warp>(name, "") {
    context(inv: Invocation<E, S>)
    override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
        inv.env.warps.names.toSuggestions()

    context(inv: Invocation<E, S>)
    override fun resolve(arg0: String): CommandResult<Warp> {
        val warp = inv.env.warps[arg0] ?: return fail(UnknownWarp(arg0))
        return success(warp)
    }
}

fun <E : WarpableServer, S> StructureScope<E, S>.warpParameter(name: String): WarpParameter<E, S> = WarpParameter(name)

class RealNameParameter<E : Environment>(name: String) : Parameter.Size1<E, SocialData, String>(name, "") {
    context(inv: Invocation<E, SocialData>)
    override fun resolve(arg0: String): CommandResult<String> {
        val nicknameEntry = inv.sender.nicknames.entries.find { it.value == arg0 }
            ?: return customError("Unknown nickname: $arg0")
        return success(nicknameEntry.key)
    }
}

fun <E : Environment, S> StructureScope<E, S>.realNameParameter(
    name: String,
): RealNameParameter<E> = RealNameParameter(name)

class BioParameter<E : Environment>(name: String) :
    Parameter.Unbounded<E, SocialData, String>(Size.atLeast(1), name, "") {

    context(inv: Invocation<E, SocialData>)
    override fun resolve(args: List<String>): ConsumingResult<String> {
        val bioLine = args.joinToString(" ")
        return success(bioLine).consuming(args.size)
    }
}

fun <E : Environment, S> StructureScope<E, S>.bioParameter(name: String): BioParameter<E> = BioParameter(name)

// --- requires -----------------------------------------------------------------------------------------------------

@OverloadResolutionByLambdaReturnType
fun <E : Environment, T_ : Arguments> StructureScope<E, Sender>.requireSocialData(
    command: StructureScope<E, SocialData>.() -> Structure<E, SocialData, T_>
): Structure<E, Sender, T_> =
    requireSender(Player::class) {
        mapSender({ it.socialData }, command)
    }

@OverloadResolutionByLambdaReturnType
fun <E : Environment, T, P : Position> StructureScope<E, Sender>.requireSocialData(
    parameter: StructureScope<E, SocialData>.() -> Parameter<E, SocialData, T, P>
): GatedParameter<E, Sender, T, P> =
    requireSender(Player::class) {
        mapSender({ it.socialData }, parameter)
    }

// --- helpers ------------------------------------------------------------------------------------------------------

fun <E : Environment, S : Player> StructureScope<E, S>.socialDataHelper(): Helper<E, S, SocialData> = helper {
    success(sender.socialData)
}

fun <E : Environment, S : Player> StructureScope<E, S>.worldHelper(): Helper<E, S, String> = helper {
    success(sender.world)
}
