package dev.themeinerlp.designserver

import dev.themeinerlp.designserver.commands.ItemsCommand
import dev.themeinerlp.designserver.listener.ItemDropListener
import dev.themeinerlp.designserver.listener.ItemPickupListener
import dev.themeinerlp.designserver.listener.PlayerLoginListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import net.minestom.server.MinecraftServer
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import java.util.function.Consumer

fun main(args: Array<String>) = runBlocking {
    val mc = MinecraftServer.init()
    val itemServer = ItemService()
    itemServer.init()
    launch(newSingleThreadContext("ItemWatchServiceThread")) {
        itemServer.run()
    }

    val instanceManager = MinecraftServer.getInstanceManager()
    val instanceContainer = instanceManager.createInstanceContainer()
    MinecraftServer.getCommandManager().register(ItemsCommand(itemServer))

    instanceContainer.chunkGenerator = GeneratorDemo()
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    globalEventHandler.addListener(PlayerLoginEvent::class.java, PlayerLoginListener(instanceContainer))
    globalEventHandler.addListener(ItemDropEvent::class.java, ItemDropListener())
    globalEventHandler.addListener(PickupItemEvent::class.java, ItemPickupListener())

    mc.start("0.0.0.0", 25565)
}