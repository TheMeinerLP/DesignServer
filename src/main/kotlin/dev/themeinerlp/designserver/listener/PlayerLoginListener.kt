package dev.themeinerlp.designserver.listener

import net.minestom.server.coordinate.Pos
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceContainer
import java.util.function.Consumer

class PlayerLoginListener(val instanceContainer: InstanceContainer) : Consumer<PlayerLoginEvent> {
    override fun accept(it: PlayerLoginEvent) {
        val player = it.player
        it.setSpawningInstance(this.instanceContainer)
        player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }
}