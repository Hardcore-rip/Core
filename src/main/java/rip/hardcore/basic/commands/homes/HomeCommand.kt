package rip.hardcore.basic.commands.homes

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.CommandAlias
import rip.hardcore.basic.manager.HomeManager
import org.bukkit.entity.Player
import rip.hardcore.basic.menus.HomeGUI

@CommandAlias("home")
class HomeCommand(private val homeManager: HomeManager) : BaseCommand() {

    @Default
    fun onHome(player: Player) {
        val homeGUI = HomeGUI(homeManager, player)
        homeGUI.open()
    }


}