package rip.hardcore.basic.commands.homes

import co.aikar.commands.BaseCommand
import co.aikar.commands.PaperCommandManager
import co.aikar.commands.annotation.*
import org.bukkit.entity.Player
import rip.hardcore.basic.manager.HomeManager
import rip.hardcore.basic.manager.TeleportManager
import rip.hardcore.basic.menus.HomeGUI
import rip.hardcore.filter.util.translate

@CommandAlias("home")
class HomeCommand(
    private val homeManager: HomeManager,
    private val teleportManager: TeleportManager,
    commandManager: PaperCommandManager
) : BaseCommand() {

    init {
        commandManager.commandCompletions.registerCompletion("homes") { context ->
            val player = context.player
            if (player != null) {
                homeManager.getHomes(player.uniqueId).map { it.name }
            } else {
                emptyList<String>()
            }
        }
    }

    @Default
    fun onHome(player: Player) {
        val homeGUI = HomeGUI(homeManager, player)
        homeGUI.open()
    }

    @Subcommand("set")
    @CommandCompletion("<name>")
    fun setHome(player: Player, homeName: String) {
        val homes = homeManager.getHomes(player.uniqueId)
        val homeGUI = HomeGUI(homeManager, player)

        if (homeName.length > 8) {
            player.sendMessage("&cHome name cannot be longer than 8 characters.".translate())
        } else if (homes.size >= homeGUI.getMaxHomes(player)) {
            player.sendMessage("&cYou have reached your home limit.".translate())
        } else if (homes.any { it.name.equals(homeName, ignoreCase = true) }) {
            player.sendMessage("&cYou already have a home with that name.".translate())
        } else {
            homeManager.setHome(player.uniqueId, homeName.toLowerCase(), player.location)
            player.sendMessage("&aHome '$homeName' set!".translate())
        }
    }

    @Subcommand("teleport")
    @CommandCompletion("<home>")
    fun teleportHome(player: Player, homeName: String) {
        val home = homeManager.getHome(player.uniqueId, homeName.toLowerCase())
        if (home == null) {
            player.sendMessage("&cYou do not have a home with that name.".translate())
            return
        }
        player.closeInventory()
        teleportManager.teleportPlayer(player, home.location, homeName)
    }

    @Subcommand("delete")
    @CommandCompletion("<home>")
    fun deleteHome(player: Player, homeName: String) {
        val home = homeManager.getHome(player.uniqueId, homeName.toLowerCase())
        if (home == null) {
            player.sendMessage("&cYou do not have a home with that name.".translate())
            return
        }
        homeManager.deleteHome(player.uniqueId, homeName.toLowerCase())
        player.sendMessage("&aHome '$homeName' deleted!".translate())
    }
}
