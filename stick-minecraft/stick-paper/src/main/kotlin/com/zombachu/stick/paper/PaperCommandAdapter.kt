package com.zombachu.stick.paper

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.zombachu.stick.CommandRunner
import com.zombachu.stick.asCoroutineContext
import com.zombachu.stick.element.Structure
import com.zombachu.stick.failure.FailureHandler
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Predicate
import kotlin.coroutines.CoroutineContext

internal class PaperCommandAdapter<E : PaperEnvironment>(
    env: E,
    failureHandler: FailureHandler<E, CommandSender>,
    private val structure: Structure<E, CommandSender, *>,
    asyncContext: CoroutineContext,
) : Command<CommandSourceStack>, SuggestionProvider<CommandSourceStack>, Predicate<CommandSourceStack> {

    private val runner = CommandRunner(env, failureHandler, structure, mainThread(env.plugin), asyncContext)

    fun toBrigadierNode(): LiteralCommandNode<CommandSourceStack> =
        Commands.literal(structure.label)
            .requires(this)
            .executes(this)
            .then(Commands.argument("args", StringArgumentType.greedyString()).suggests(this).executes(this))
            .build()

    override fun run(context: CommandContext<CommandSourceStack>): Int {
        val tokens = context.range.get(context.input).split(' ').filter { it.isNotEmpty() }
        runner.execute(context.source.sender, tokens.first().stripNamespace(), tokens.drop(1))
        return Command.SINGLE_SUCCESS
    }

    override fun getSuggestions(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder,
    ): CompletableFuture<Suggestions> {
        val args = builder.remaining.split(' ')
        val suggestionsBuilder = builder.createOffset(builder.input.lastIndexOf(' ') + 1)
        return runner.suggestAsync(context.source.sender, structure.label, args).thenApply { suggestions ->
            suggestions.forEach { suggestionsBuilder.suggest(it) }
            suggestionsBuilder.build()
        }
    }

    override fun test(source: CommandSourceStack): Boolean = runner.canUse(source.sender)
}

private fun String.stripNamespace(): String = substringAfterLast(':')

private fun mainThread(plugin: Plugin): CoroutineContext {
    val executor = Executor { task ->
        if (Bukkit.isPrimaryThread()) {
            task.run()
        } else if (plugin.isEnabled) {
            Bukkit.getScheduler().runTask(plugin, task)
        }
    }
    return executor.asCoroutineContext()
}
