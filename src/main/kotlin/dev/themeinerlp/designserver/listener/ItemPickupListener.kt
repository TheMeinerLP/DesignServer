package dev.themeinerlp.designserver.listener

import net.minestom.server.entity.Player
import net.minestom.server.event.item.PickupItemEvent
import java.util.function.Consumer

class ItemPickupListener : Consumer<PickupItemEvent> {
    override fun accept(event: PickupItemEvent) {
        if (event.livingEntity is Player && event.itemEntity.isPickable) {
            (event.livingEntity as Player).inventory.addItemStack(event.itemEntity.itemStack)
            event.itemEntity.remove()
        }
    }
}