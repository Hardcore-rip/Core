package rip.hardcore.basic.menus

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.filter.util.translate
import org.bukkit.inventory.meta.ItemMeta

class HomeGUI(private val homeManager: HomeManager, private val player: Player) {

    fun open() {
        val inventory = Bukkit.createInventory(null, 9, "&7Your Homes".translate())
        val homes = homeManager.getHomes(player.uniqueId)
        val maxHomes = getMaxHomes(player)

        for (i in 0 until maxHomes) {
            val home = homes.getOrNull(i)
            val item = if (home != null) {
                ItemStack(Material.EMERALD_BLOCK).apply {
                    itemMeta = createItemMeta("$home.name", "Click to teleport")
                }
            } else {
                ItemStack(Material.COAL_BLOCK).apply {
                    itemMeta = createItemMeta("Empty Slot", "You have no home set here")
                }
            }
            inventory.setItem(i, item)
        }
        player.openInventory(inventory)
    }

    fun getMaxHomes(player: Player): Int {
        return when {
            player.hasPermission("home.toprank") -> 5
            player.hasPermission("home.midrank") -> 4
            player.hasPermission("home.shitrank") -> 2
            else -> 1
        }
    }

    private fun createItemMeta(name: String, lore: String): ItemMeta {
        val meta = ItemStack(Material.PAPER).itemMeta
        meta.setDisplayName(name)
        meta.lore = listOf(lore)
        return meta
    }

}