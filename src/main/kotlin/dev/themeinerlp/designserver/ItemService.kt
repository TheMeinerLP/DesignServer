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
import net.minestom.server.attribute.Attribute
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.Enchantment
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.attribute.ItemAttribute
import java.io.IOException
import java.lang.Exception
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.collections.ArrayList
import kotlin.io.path.inputStream
import kotlin.io.path.readBytes
import kotlin.io.path.readText

class ItemService {

    private val modelsPath = Paths.get("models", "items")
    private val watchService = FileSystems.getDefault().newWatchService()
    private val registeredKeys = ArrayList<WatchKey>()
    val items = emptyMap<String, ItemStack>().toMutableMap()
    val pathToItems = emptyMap<Path, String>().toMutableMap()


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
                items[item.fileName] = transformItemToStack(item)
                pathToItems[file] = item.fileName
                return super.visitFile(file, attrs)
            }
        })

    }

    private fun buildItem(event: WatchEvent<*>, dirPath: Path) = runBlocking(Dispatchers.IO) {
        val eventPath = dirPath.resolve((event.context() as Path).toString().replace("~", "")).normalize()
        if (event.kind() == ENTRY_DELETE) {
            items.remove(pathToItems[eventPath])
            return@runBlocking
        }

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
                        player.inventory.addItemStack(
                            ItemStack
                                .builder(
                                    Material.fromNamespaceId(item.material)
                                        ?: throw RuntimeException("Material not found")
                                )
                                .amount(item.amount)
                                .displayName(Component.text(item.name))
                                .lore(item.lore.map { Component.text(it) })
                                .build()
                        )
                    }
                    item
                }
                if (items.containsKey(item.fileName)) {
                    items.replace(
                        item.fileName,
                        transformItemToStack(item)
                    )

                } else {
                    items[item.fileName] = transformItemToStack(item)
                }
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

    private fun transformItemToStack(item: Item): ItemStack {
        val itemBuilder = ItemStack.builder(
            Material.fromNamespaceId(item.material) ?: throw RuntimeException("Material not found")
        )
        itemBuilder.amount(item.amount)
        itemBuilder.displayName(Component.text(item.name))
        itemBuilder.lore(item.lore.map { Component.text(it) })
        if (item.meta != null) {
            itemBuilder.meta { meta ->
                if (item.meta?.customModelData != null) {
                    meta.customModelData(
                        item.meta?.customModelData ?: throw RuntimeException("customModelData not found")
                    )
                }
                if (item.meta?.damage != null) {
                    meta.damage(item.meta?.damage ?: throw RuntimeException("damage not found"))
                }
                if (item.meta?.itemFlags != null) {
                    meta.hideFlag(
                        *item.meta?.itemFlags?.toTypedArray() ?: throw RuntimeException("itemFlags not found")
                    )
                }
                if (item.meta?.enchantments != null) {
                    meta.enchantments(
                        item.meta?.enchantments?.associate { Enchantment.fromNamespaceId(it.enchantment) to it.level }
                            ?: throw RuntimeException("enchantments not found")
                    )
                }
                if (item.meta?.attributes != null) {
                    meta.attributes(
                        item.meta?.attributes?.map {
                            ItemAttribute(
                                UUID.fromString(it.uuid),
                                it.internalName,
                                Attribute.fromKey(it.attribute)
                                    ?: throw RuntimeException("attribute not found"),
                                it.operation,
                                it.value,
                                it.slot
                            )
                        } ?: throw RuntimeException("attributes not found")
                    )
                }
                meta
            }
        }
        return itemBuilder.build()
    }

}