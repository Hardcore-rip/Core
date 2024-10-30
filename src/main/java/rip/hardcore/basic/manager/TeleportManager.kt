package rip.hardcore.basic.manager

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import rip.hardcore.filter.util.translate

class TeleportManager(private val plugin: JavaPlugin) {

    fun teleportPlayer(player: Player, location: Location, name: String = "home") {
        val start = player.location
        val countdownSeconds = 5

        player.playSound(player.location, "block.note_block.pling", 1f, 1f)
        player.sendMessage("&#10f08a&lTeleporter &8» &#c4f5dfYou will be teleported to \"$name\" in $countdownSeconds seconds...".translate())

        object : BukkitRunnable() {
            var timeLeft = countdownSeconds

            override fun run() {

                if (playerHasMoved(player, start)) {
                    player.playSound(player.location, "entity.enderman.death", 1f, 1f)
                    player.sendMessage("&#10f08a&lTeleporter &8» &#f9445dYou moved! Teleportation cancelled.".translate())
                    cancel()
                    return
                }

                if (timeLeft <= 1) {
                    player.sendMessage("&#10f08a&lTeleporter &8» &#c4f5dfYou have been teleported to \"$name\"!".translate())
                    player.teleport(location)
                    player.playSound(player.location, "entity.enderman.teleport", 1f, 1f)
                    cancel()
                } else {
                    timeLeft -= 1
                    player.playSound(player.location, "block.note_block.pling", 1f, 1f)
                    player.sendMessage("&#10f08a&lTeleporter &8» &#c4f5dfYou will be teleported to \"$name\" in $timeLeft seconds...".translate())
                }
            }
        }.runTaskTimer(plugin, 20, 20)
    }

    private fun playerHasMoved(player: Player, start: Location): Boolean {
        return player.location.blockX != start.blockX || player.location.blockZ != start.blockZ
    }
}
