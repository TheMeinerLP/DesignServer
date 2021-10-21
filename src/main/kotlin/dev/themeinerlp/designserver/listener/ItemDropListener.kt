package dev.themeinerlp.designserver.listener

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.ItemEntity
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.inventory.TransactionOption.DRY_RUN
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.function.Consumer


class ItemDropListener : Consumer<ItemDropEvent> {
    override fun accept(event: ItemDropEvent) {
        val player = event.player
        val droppedItem = event.itemStack
        if (player.isSneaking) {
            player.inventory.takeItemStack(droppedItem, DRY_RUN)
            return
        }

        val playerPos: Pos = player.position
        val itemEntity = ItemEntity(droppedItem)
        itemEntity.setPickupDelay(Duration.of(500, ChronoUnit.MILLIS))
        itemEntity.setInstance(player.instance ?: throw RuntimeException("Instance not found"), playerPos.withY { y -> y + 1.5 })
        val velocity: Vec = playerPos.direction().mul(6.0)
        itemEntity.velocity = velocity
    }
}