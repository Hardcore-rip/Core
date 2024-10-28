package rip.hardcore.basic.listeners

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.basic.menus.HomeGUI
import rip.hardcore.filter.util.translate
import java.util.*

class HomeListener(private val homeManager: HomeManager) : Listener {

    private val pendingHomeNames = mutableSetOf<UUID>()
    private val pendingDeletions = mutableMapOf<UUID, String>()

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.view.title.toString() != "&7Your Homes".translate()) return

        event.isCancelled = true
        val item = event.currentItem ?: return

        when (item.type) {
            Material.LIME_BED -> {
                val homeName = item.itemMeta?.displayName?.toLowerCase()?.removeHexCodes() ?: return
                val home = homeManager.getHome(player.uniqueId, homeName)

                if (home != null) {
                    if (event.click == ClickType.RIGHT) {
                        openDeleteConfirmation(player, homeName)
                    } else {
                        player.teleport(home.location)
                        player.sendMessage("&aTeleported to home '${homeName.toUpperCase()}'".translate())
                    }
                }
            }
            Material.GRAY_BED -> {
                player.closeInventory()
                player.sendMessage("&ePlease type a name for your new home in chat.".translate())
                pendingHomeNames.add(player.uniqueId)
            }
            Material.BLACK_STAINED_GLASS_PANE -> {
            }
            else -> {
            }
        }
    }

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (pendingHomeNames.contains(player.uniqueId)) {
            event.isCancelled = true

            val homeName = event.message.removeHexCodes()
            if (homeManager.getHome(player.uniqueId, homeName.toLowerCase()) == null) {
                homeManager.setHome(player.uniqueId, homeName.toLowerCase(), player.location)
                player.sendMessage("&aHome '$homeName' has been set at your current location.".translate())
            } else {
                player.sendMessage("&cA home with that name already exists. Please try again.".translate())
            }

            pendingHomeNames.remove(player.uniqueId)
        }
    }

    @EventHandler
    fun onDeleteConfirmationClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.view.title.toString() != "&cConfirm Deletion".translate()) return

        event.isCancelled = true
        val item = event.currentItem ?: return

        when (item.type) {
            Material.LIME_CONCRETE -> {
                val homeName = pendingDeletions[player.uniqueId] ?: return
                homeManager.deleteHome(player.uniqueId, homeName)
                player.sendMessage("&aHome '$homeName' has been deleted.".translate())
                pendingDeletions.remove(player.uniqueId)
                val homeGUI = HomeGUI(homeManager, player)
                homeGUI.open()
            }
            Material.RED_CONCRETE -> {
                player.sendMessage("&cHome deletion cancelled.".translate())
                pendingDeletions.remove(player.uniqueId)
                val homeGUI = HomeGUI(homeManager, player)
                homeGUI.open()
            }
            else -> {
            }
        }
    }

    private fun openDeleteConfirmation(player: Player, homeName: String) {
        val inventory = Bukkit.createInventory(null, 27, "&cConfirm Deletion".translate())

        val confirmItem = ItemStack(Material.LIME_CONCRETE)
        val confirmMeta = confirmItem.itemMeta
        confirmMeta?.setDisplayName("&aConfirm Delete".translate())
        confirmItem.itemMeta = confirmMeta
        inventory.setItem(12, confirmItem)

        val cancelItem = ItemStack(Material.RED_CONCRETE)
        val cancelMeta = cancelItem.itemMeta
        cancelMeta?.setDisplayName("&cCancel".translate())
        cancelItem.itemMeta = cancelMeta
        inventory.setItem(14, cancelItem)
        pendingDeletions[player.uniqueId] = homeName
        player.openInventory(inventory)
    }

    private fun String.removeHexCodes(): String {
        return this.replace("§x(§[0-9a-fA-F]){6}|§[0-9a-fA-Fk-orK-OR]".toRegex(), "")
    }
}
