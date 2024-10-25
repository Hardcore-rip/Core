package rip.hardcore.basic.listeners

// [@] This file is authored by SnowyJS on 10/12/2024
// [@] https://snowyjs.lol
// [@] https://github.com/snowypy

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import rip.hardcore.basic.manager.LifeManager

class PlayerDeath(private val lifeManager: LifeManager) : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.player
        val victimUUID = victim.uniqueId
        val attacker = victim.killer

        val remainingLives = lifeManager.decreaseLife(victimUUID)
        if (remainingLives <= 0) {
            lifeManager.banPlayer(victimUUID)
        } else {
            victim.sendTitle("&#FF0000&lYOU DIED", "&7You died and now have &#FF0000&n$remainingLives&r &7left") // [:] This format might be broken. While using code with syntax takes 1-2m to load.
        }

    }

}