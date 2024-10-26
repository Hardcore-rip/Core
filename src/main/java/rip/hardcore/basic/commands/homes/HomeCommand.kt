package rip.hardcore.basic.commands.homes

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import org.bukkit.entity.Player
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.basic.menus.HomeGUI
import rip.hardcore.filter.util.translate

@CommandAlias("home")
class HomeCommand(private val homeManager: HomeManager) : BaseCommand() {

    @Default
    fun onHome(player: Player) {
        val homeGUI = HomeGUI(homeManager, player)
        homeGUI.open()
    }

    @CommandAlias("sethome")
    fun setHome(player: Player, homeName: String) {
        val homes = homeManager.getHomes(player.uniqueId)
        val homeGUI = HomeGUI(homeManager, player)

        if (homes.size >= homeGUI.getMaxHomes(player)) {
            player.sendMessage("&cYou have reached your home limit.".translate())
        } else {
            homeManager.setHome(player.uniqueId, homeName, player.location)
            player.sendMessage("&aHome '$homeName' set!".translate())
        }
    }
}
