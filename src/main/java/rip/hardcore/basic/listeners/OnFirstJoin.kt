package rip.hardcore.basic.listeners

// [@] This file is authored by SnowyJS on 10/25/2024
// [@] https://snowyjs.lol
// [@] https://github.com/snowypy

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

import rip.hardcore.basic.manager.LifeManager
import rip.hardcore.filter.util.translate

class OnFirstJoin(private val lifeManager: LifeManager) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerUUID = player.uniqueId


        if (lifeManager.isBanned(playerUUID)) {
            player.kickPlayer("&#FF0000&lYOU ARE BANNED &7- &fYou lost all of your lives so you are banned. This will expire 3 hours from your death.".translate())
        }
    }

}