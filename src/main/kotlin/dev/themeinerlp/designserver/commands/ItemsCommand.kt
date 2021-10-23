package dev.themeinerlp.designserver.commands

import dev.themeinerlp.designserver.ItemService
import kotlinx.serialization.ExperimentalSerializationApi
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.item.ItemStack

@ExperimentalSerializationApi
class ItemsCommand(private val itemService: ItemService) : Command("items", "i") {
    init {
        val nameArgument = ArgumentType.String("item")
        nameArgument.setSuggestionCallback { _, _, suggestion ->
            itemService.items.map {
                SuggestionEntry(
                    it.key,
                    it.value.displayName
                )
            }.forEach {
                suggestion.addEntry(it)
            }

        }
        setDefaultExecutor { sender, _ ->
            if (sender.isPlayer) {
                sender.sendMessage("Usage: /command <item>");
            }
        }

        addSyntax({ sender, context ->
            if (sender.isPlayer) {
                val player = sender.asPlayer()
                val itemName = context.get(nameArgument)
                player.inventory.addItemStack(itemService.items.getOrDefault(itemName, ItemStack.AIR))
            }
        }, nameArgument)
    }
}