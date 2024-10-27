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

        if (homeName.length > 8) {
            player.sendMessage("&cHome name cannot be longer than 8 characters.".translate())
        } else if (homes.size >= homeGUI.getMaxHomes(player)) {
            player.sendMessage("&cYou have reached your home limit.".translate())
        } else if (homes.any { it.name == homeName.toLowerCase() }) {
            player.sendMessage("&cYou already have a home with that name.".translate())

        } else {
            homeManager.setHome(player.uniqueId, homeName.toLowerCase(), player.location)
            player.sendMessage("&aHome '$homeName' set!".translate())
        }
    }

    @CommandAlias("deletehome")
    fun deleteHome(player: Player, homeName: String) {
        homeManager.deleteHome(player.uniqueId, homeName.toLowerCase())
        player.sendMessage("&aHome '$homeName' deleted!".translate())
    }
}
