package rip.hardcore.basic.listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.java.JavaPlugin
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.filter.util.translate

class HomeListener(private val homeManager: HomeManager) : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.view.title.toString() != "&7Your Homes".translate()) return

        event.isCancelled = true

        val item = event.currentItem ?: return

        when (item.type) {
            Material.LIME_BED -> {
                val homeName = item.itemMeta?.displayName?.toLowerCase()?.removeHexCodes() ?: return
                val home = homeManager.getHome(player.uniqueId, homeName.toLowerCase())

                if (home != null) {
                    player.teleport(home.location)
                    player.sendMessage("&aTeleported to home '${homeName.toUpperCase()}'".translate())
                }
            }
            Material.GRAY_BED -> {
                player.sendMessage("&cYou can create a home in this slot by using /sethome".translate())
            }
            Material.BLACK_STAINED_GLASS_PANE -> {
            }
            else -> {

            }
        }
    }

    private fun String.removeHexCodes(): String {
        return this.replace("§x(§[0-9a-fA-F]){6}|§[0-9a-fA-Fk-orK-OR]".toRegex(), "")
    }
}
