package rip.hardcore.basic.commands.homes

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.basic.menus.HomeGUI

@CommandAlias("adminhome")
class AdminHomeCommand(private val homeManager: HomeManager) : BaseCommand() {

    fun onAdminHome(sender: Player, targetName: String) {
        val target = Bukkit.getPlayer(targetName)
        if (target != null) {
            val gui = HomeGUI(homeManager, target)
            gui.open()
            sender.sendMessage("Opened ${target.name}'s home GUI.")
        } else {
            sender.sendMessage("Player not found.")
        }
    }

    fun deleteHome(player: Player, targetName: String, homeName: String) {
        val target = Bukkit.getPlayer(targetName)
        if (target != null) {
            homeManager.deleteHome(target.uniqueId, homeName)
            player.sendMessage("Home '$homeName' deleted for ${target.name}.")
        } else {
            player.sendMessage("Player not found.")
        }
    }
}
