package com.zombachu.stick.integration

import com.zombachu.stick.CommandResult
import com.zombachu.stick.ConsumingResult
import com.zombachu.stick.Environment
import com.zombachu.stick.ParsingResult
import com.zombachu.stick.SimpleSuggestion
import com.zombachu.stick.Size
import com.zombachu.stick.Suggestion
import com.zombachu.stick.ValidationContext
import com.zombachu.stick.consuming
import com.zombachu.stick.dsl.booleanParameter
import com.zombachu.stick.dsl.branch
import com.zombachu.stick.dsl.command
import com.zombachu.stick.dsl.default
import com.zombachu.stick.dsl.enumParameter
import com.zombachu.stick.dsl.flag
import com.zombachu.stick.dsl.group
import com.zombachu.stick.dsl.hybridFlag
import com.zombachu.stick.dsl.invalidDefault
import com.zombachu.stick.dsl.invoke
import com.zombachu.stick.dsl.listParameter
import com.zombachu.stick.dsl.literalParameter
import com.zombachu.stick.dsl.optionally
import com.zombachu.stick.dsl.optionallyNullable
import com.zombachu.stick.dsl.optionals
import com.zombachu.stick.dsl.requireIs
import com.zombachu.stick.dsl.structure
import com.zombachu.stick.dsl.subcommands
import com.zombachu.stick.dsl.valueFlag
import com.zombachu.stick.element.Parameter
import com.zombachu.stick.integration.fixtures.Console
import com.zombachu.stick.integration.fixtures.Location
import com.zombachu.stick.integration.fixtures.Player
import com.zombachu.stick.integration.fixtures.Sender
import com.zombachu.stick.integration.fixtures.Server
import com.zombachu.stick.integration.fixtures.SynergyServer
import com.zombachu.stick.integration.fixtures.Warp
import com.zombachu.stick.integration.fixtures.WarpRegistry
import com.zombachu.stick.integration.fixtures.WarpableServer
import com.zombachu.stick.integration.fixtures.permission
import com.zombachu.stick.integration.fixtures.playerParameter
import com.zombachu.stick.integration.fixtures.suggest
import com.zombachu.stick.integration.fixtures.warpParameter
import com.zombachu.stick.toSuggestions
import kotlin.test.Test
import kotlin.test.assertEquals

class SuggestionTest {

    private val zombachu = Player("zombachu", ["server.warp.privacy"])
    private val steve = Player("Steve")
    private val console = Console()
    private val server =
        SynergyServer(
            [zombachu, steve],
            WarpRegistry([Warp("spawn", "Console", "overworld"), Warp("shop", "zombachu", "nether")]),
        )

    @Test
    fun `warp - inaccessible subcommands do not suggest`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                group(
                    requireIs(Player::class) {
                        command(name = "tp", aliases = ["goto"])(
                            warpParameter("warp")
                        ) { }
                    },
                    command("info")() { },
                )
            ) { }
        }

        assertEquals([], warpCommand.suggest(server, zombachu, "/warp"))
        assertEquals(["tp", "info"], warpCommand.suggest(server, zombachu, "/warp "))
        assertEquals(["goto"], warpCommand.suggest(server, zombachu, "/warp GO"))
        assertEquals([], warpCommand.suggest(server, zombachu, "/warp goto"))
        assertEquals(["spawn", "shop"], warpCommand.suggest(server, zombachu, "/warp tp "))
        assertEquals([], warpCommand.suggest(server, zombachu, "/warp info "))

        assertEquals(["info"], warpCommand.suggest(server, console, "/warp "))
    }

    @Test
    fun `give - flags suggest at any position`() {
        val giveCommand = structure(Server::class, Sender::class) {
            command("give")(
                flag("silent"),
                valueFlag("unbreakable", default = false, parameter = booleanParameter("unbreakable")),
                hybridFlag("glowing", booleanParameter("glowing")),
                playerParameter("player"),
                listParameter("items", MaterialParameter()),
            ) { _, _, _, _, _ -> }
        }

        assertEquals(
            ["-silent", "-unbreakable", "-glowing", "zombachu", "Steve"],
            giveCommand.suggest(server, zombachu, "/give "),
        )
        assertEquals(["true", "false"], giveCommand.suggest(server, zombachu, "/give -unbreakable "))
        assertEquals(
            ["true", "false", "zombachu", "Steve"],
            giveCommand.suggest(server, zombachu, "/give -silent -unbreakable true -glowing "),
        )
        assertEquals(
            ["-silent", "-unbreakable", "-glowing", "minecraft:"],
            giveCommand.suggest(server, zombachu, "/give Steve "),
        )

        assertEquals(
            ["minecraft:stone", "minecraft:dirt"],
            giveCommand.suggest(server, zombachu, "/give Steve minecraft:"),
        )
        assertEquals(
            ["minecraft:andesite"],
            giveCommand.suggest(server, zombachu, "/give Steve minecraft:a"),
        )
        assertEquals(
            ["stone,minecraft:stone", "stone,minecraft:dirt"],
            giveCommand.suggest(server, zombachu, "/give Steve stone,minecraft:"),
        )

        assertEquals(
            ["-silent", "-unbreakable", "-glowing"],
            giveCommand.suggest(server, zombachu, "/give Steve stone "),
        )

        assertEquals([], giveCommand.suggest(server, zombachu, "/give nobody "))
    }

    @Test
    fun `count - trailing optionals suggest left to right`() {
        val countCommand = structure(Server::class, Sender::class) {
            command("count")(
                optionals(
                    optionallyNullable(literalParameter("one")),
                    optionallyNullable(literalParameter("two")),
                )
            ) { }
        }

        assertEquals(["one"], countCommand.suggest(server, zombachu, "/count "))
        assertEquals(["two"], countCommand.suggest(server, zombachu, "/count one "))
    }

    @Test
    fun `throwing - throwing parameter suggests nothing`() {
        val throwingCommand = structure(Server::class, Sender::class) {
            command("throwing")(ThrowingParameter()) { }
        }

        assertEquals([], throwingCommand.suggest(server, zombachu, "/throwing a "))
    }

    @Test
    fun `select - group completes matching branch`() {
        val selectCommand = structure(Server::class, Sender::class) {
            command("select")(
                group(
                    literalParameter("all"),
                    PointParameter(),
                    NearParameter()
                )
            ) { }
        }

        assertEquals(["all", "x=", "near"], selectCommand.suggest(server, zombachu, "/select "))
        assertEquals([], selectCommand.suggest(server, zombachu, "/select all "))
        assertEquals(["y="], selectCommand.suggest(server, zombachu, "/select x=1 "))
        assertEquals(["z="], selectCommand.suggest(server, zombachu, "/select x=1 y=2 "))

        // KNOWN LIMITATION: Parameter.Fixed.match reports a short window as Partial without reading it, so point stays
        // open after "near"
        // TODO: fix
        assertEquals(["y=", "zombachu", "Steve"], selectCommand.suggest(server, zombachu, "/select near "))
    }

    @Test
    fun `rejecting - group branches match once`() {
        val rejectingParameter = RejectingParameter<Server, Sender>()
        val rejectingCommand = structure(Server::class, Sender::class) {
            command("rejecting")(
                group(
                    PointParameter(),
                    rejectingParameter,
                )
            ) { }
        }

        assertEquals(["y="], rejectingCommand.suggest(server, zombachu, "/rejecting x=1 "))
        assertEquals(1, rejectingParameter.count)
    }

    @Test
    fun `warp - branch flags do not suggest before leading parameter`() {
        val warpCommand = structure(WarpableServer::class, Sender::class) {
            command("warp")(
                subcommands(
                    command("tp")(
                        flag("silent"),
                        playerParameter("player")
                    ) { _, _ -> },
                    branch(warpParameter("warp"))(
                        flag("confirm"),
                        literalParameter("delete")
                    ),
                )
            ) { }
        }

        assertEquals(["tp", "spawn", "shop"], warpCommand.suggest(server, zombachu, "/warp "))
        assertEquals(["-silent", "zombachu", "Steve"], warpCommand.suggest(server, zombachu, "/warp tp "))
        assertEquals(["-confirm", "delete"], warpCommand.suggest(server, zombachu, "/warp shop "))
    }

    @Test
    fun `setwarp - variable size parameter can complete early`() {
        val setWarpCommand = structure(Server::class, Sender::class) {
            command("setwarp")(
                flag("announce"),
                requireIs(Player::class) { LocationParameter() },
                optionally(
                    ifInvalid = invalidDefault(Privacy.Private, permission("server.warp.privacy")),
                    ifAbsent = default(Privacy.Private),
                    parameter = enumParameter("privacy", Privacy::class),
                ),
            ) { _, _, _ -> }
        }

        assertEquals(["-announce", "~", "x="], setWarpCommand.suggest(server, zombachu, "/setwarp "))
        assertEquals(["y="], setWarpCommand.suggest(server, zombachu, "/setwarp x=1 "))
        assertEquals(["-announce", "public", "private"], setWarpCommand.suggest(server, zombachu, "/setwarp ~ "))

        assertEquals(["-announce"], setWarpCommand.suggest(server, steve, "/setwarp ~ "))

        assertEquals([], setWarpCommand.suggest(server, console, "/setwarp "))
    }

    @Test
    fun `tp - repeated spaces are ignored`() {
        val tpCommand = structure(Server::class, Sender::class) {
            command("tp")(
                playerParameter("player")
            ) { }
        }

        assertEquals(["zombachu", "Steve"], tpCommand.suggest(server, zombachu, "/tp  "))
        assertEquals(["Steve"], tpCommand.suggest(server, zombachu, "/tp  St"))
    }

    private class ThrowingParameter<E : Environment, S> : Parameter.Size1<E, S, String>("", "") {

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(arg0: String): CommandResult<String> = error("")
    }

    private class MaterialParameter<E : Environment, S> : Parameter.Size1<E, S, String>("", "") {

        context(validationContext: ValidationContext<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> {
            val colon = partial.indexOf(':')
            if (colon < 0) return ["minecraft:"].toSuggestions()
            return [
                SimpleSuggestion("stone", colon + 1),
                SimpleSuggestion("dirt", colon + 1),
                SimpleSuggestion("andesite", colon + 1, isAlias = true),
            ]
        }

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(arg0: String): CommandResult<String> = ParsingResult.success(arg0)
    }

    /** x=X y=Y z=Z **/
    private class PointParameter<E : Environment, S> : Parameter.Size3<E, S, Location>("", "") {

        private val axes = ["x", "y", "z"]

        context(validationContext: ValidationContext<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            [axes[preceding.size] + "="].toSuggestions()

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(arg0: String, arg1: String, arg2: String): CommandResult<Location> {
            val (x, y, z) = [arg0, arg1, arg2].mapIndexed { i, arg ->
                if (!arg.startsWith(axes[i] + "=")) return ParsingResult.failType("point", arg)
                arg.drop(2).toIntOrNull() ?: return ParsingResult.failType("point", arg)
            }
            return ParsingResult.success(Location(x, y, z))
        }
    }

    /** near <player> **/
    private class NearParameter<E : Server, S> : Parameter.Bounded<E, S, Player>(Size(2), "", "") {

        context(validationContext: ValidationContext<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            if (preceding.isEmpty()) ["near"].toSuggestions() else validationContext.env.playerNames.toSuggestions()

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<Player> {
            if (args.firstOrNull() != "near") return ParsingResult.failType("near", args.firstOrNull() ?: "")
            if (args.size < 2) return ParsingResult.failSize()
            val player = validationContext.env.getPlayer(args[1]) ?: return ParsingResult.failType("player", args[1])
            return ParsingResult.success(player).consuming(2)
        }
    }

    /** ~ or x=X y=Y z=Z **/
    private class LocationParameter<E : Environment, S : Player> :
        Parameter.Bounded<E, S, Location>(Size.between(1, 3), "", "") {

        private val point = PointParameter<E, S>()

        context(validationContext: ValidationContext<E, S>)
        override fun suggest(preceding: List<String>, partial: String): List<Suggestion> =
            if (preceding.isEmpty()) ["~"].toSuggestions() + point.suggest(preceding, partial)
            else point.suggest(preceding, partial)

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<Location> {
            if (args.firstOrNull() == "~") {
                return ParsingResult.success(validationContext.sender.position).consuming(1, canConsumeMore = false)
            }
            if (args.size < 3) return ParsingResult.failSize()
            return point.resolve(args)
        }
    }

    private class RejectingParameter<E : Environment, S> : Parameter.Bounded<E, S, String>(Size.between(1, 2), "", "") {
        var count = 0

        context(validationContext: ValidationContext<E, S>)
        override fun resolve(args: List<String>): ConsumingResult<String> {
            count++
            return ParsingResult.failType("", args.first())
        }
    }

    private enum class Privacy {
        Public,
        Private,
    }
}
