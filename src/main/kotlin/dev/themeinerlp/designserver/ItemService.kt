package dev.themeinerlp.designserver

import dev.themeinerlp.designserver.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.kyori.adventure.text.Component
import net.minestom.server.MinecraftServer
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import java.io.IOException
import java.lang.Exception
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.inputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText

class ItemService {

    private val modelsPath = Paths.get("models", "items")
    private val watchService = FileSystems.getDefault().newWatchService()
    private val registeredKeys = ArrayList<WatchKey>()
    val items = emptyMap<String, ItemStack>().toMutableMap()


    fun init() {
        Files.createDirectories(modelsPath)
        registeredKeys += modelsPath.register(
            watchService,
            ENTRY_CREATE,
            ENTRY_MODIFY,
            ENTRY_DELETE
        )

        Files.walkFileTree(modelsPath, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(subPath: Path, attrs: BasicFileAttributes?): FileVisitResult {
                registeredKeys += subPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                val item = Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                    encodeDefaults = false
                }.decodeFromString<Item>(file.readText())
                items[item.fileName] = ItemStack
                    .builder(Material.fromNamespaceId(item.material) ?: throw RuntimeException("Material not found"))
                    .amount(item.amount)
                    .displayName(Component.text(item.name))
                    .lore(item.lore.map { Component.text(it) })
                    .build()
                return super.visitFile(file, attrs)
            }
        })

    }

    private fun buildItem(event: WatchEvent<*>, dirPath: Path) = runBlocking(Dispatchers.IO) {
        val eventPath = dirPath.resolve((event.context() as Path).toString().replace("~", "")).normalize()
        if (eventPath.toFile().isFile) {
            CoroutineScope(Dispatchers.Default).launch {
                val item = try {
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                        encodeDefaults = false
                    }.decodeFromStream(eventPath.inputStream())
                } catch (e: Exception) {
                    val item = Item(
                        eventPath.fileName.toString(),
                        e.message!!,
                        e.message!!,
                        e.message!!,
                        e.stackTraceToString().split("\n").toList(),
                        1,
                        "minecraft:stone"
                    )
                    MinecraftServer.getConnectionManager().onlinePlayers.forEach { player ->
                        player.inventory.addItemStack(ItemStack
                            .builder(
                                Material.fromNamespaceId(item.material) ?: throw RuntimeException("Material not found")
                            )
                            .amount(item.amount)
                            .displayName(Component.text(item.name))
                            .lore(item.lore.map { Component.text(it) })
                            .build())
                    }
                    item
                }
                val itemBuilder = ItemStack.builder(Material.fromNamespaceId(item.material) ?: throw RuntimeException("Material not found"))
                itemBuilder.amount(item.amount)
                itemBuilder.displayName(Component.text(item.name))
                itemBuilder.lore(item.lore.map { Component.text(it) })
                if (item.meta != null ) {
                    itemBuilder.meta { meta ->
                        if (item.meta?.customModelData != null) {
                            meta.customModelData(item.meta?.customModelData ?: throw RuntimeException("customModelData not found"))
                        }
                        if (item.meta?.damage != null) {
                            meta.damage(item.meta?.damage ?: throw RuntimeException("damage not found"))
                        }
                        meta
                    }
                }
                items.replace(
                    item.fileName,
                    itemBuilder.build()
                )
            }
        }

    }

    fun run() {
        while (true) {
            val watchKey = watchService.take()
            val dirPath = watchKey.watchable() as Path
            for (event in watchKey.pollEvents()) {
                buildItem(event, dirPath)
            }

            if (!watchKey.reset()) {
                watchKey.cancel()
                watchService.close()
                break
            }
        }

    }

}