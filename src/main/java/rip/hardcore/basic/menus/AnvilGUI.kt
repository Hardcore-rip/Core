package rip.hardcore.basic.utils

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import rip.hardcore.filter.util.translate

class AnvilGUI(private val plugin: JavaPlugin, private val callback: (String) -> Unit) {

    fun open(player: Player) {
        val inventory = Bukkit.createInventory(null, 9, "Enter Home Name")
        val item = ItemStack(Material.NAME_TAG)
        val meta = item.itemMeta
        meta?.setDisplayName("Enter a name for your home")
        item.itemMeta = meta

        inventory.setItem(4, item)
        player.openInventory(inventory)

        object : BukkitRunnable() {
            override fun run() {
                player.closeInventory()
            }
        }.runTaskLater(plugin, 200)

        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler
            fun onInventoryClick(event: InventoryClickEvent) {
                if (event.whoClicked != player || event.inventory != inventory) return

                event.isCancelled = true
                val inputName = event.currentItem?.itemMeta?.displayName ?: return

                callback(inputName)
                player.closeInventory()
            }
        }, plugin)
    }
}
