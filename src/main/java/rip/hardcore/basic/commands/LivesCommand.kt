package rip.hardcore.basic.commands

// [@] This file is authored by SnowyJS on 10/12/2024
// [@] https://snowyjs.lol
// [@] https://github.com/snowypy

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player

import rip.hardcore.basic.manager.LifeManager

@CommandAlias("lives")
class LivesCommand(private val lifeManager: LifeManager) : BaseCommand() {
    @Subcommand("stats")
    @CommandCompletion("@players")
    fun onStats(sender: Player, @Optional target: String?) {
        val targetPlayer = if (target == null) {
            sender
        } else {
            Bukkit.getPlayerExact(target)
        }

        if (targetPlayer == null) {
            sender.sendMessage("&#FF0000Player not found.")
            return
        }

        val lives = lifeManager.getLives(targetPlayer.uniqueId)
        if (targetPlayer == sender) {
            sender.sendMessage("&aYou have $lives lives remaining.")
        } else {
            sender.sendMessage("&a${targetPlayer.name} has $lives lives remaining.")
        }
    }

    @Subcommand("pay")
    @CommandCompletion("@players")
    @Syntax("<player> <amount>")
    fun onPay(sender: Player, targetName: String, amount: Int) {
        val targetPlayer = Bukkit.getPlayerExact(targetName)

        if (targetPlayer == null) {
            sender.sendMessage("&#FF0000Player not found.")
            return
        }

        if (amount <= 0) {
            sender.sendMessage("&#FF0000You must send a positive number of lives.")
            return
        }

        val senderLives = lifeManager.getLives(sender.uniqueId)
        if (senderLives < amount) {
            sender.sendMessage("&#FF0000You don't have enough lives to give.")
            return
        }

        lifeManager.setLives(sender.uniqueId, senderLives - amount)
        val targetLives = lifeManager.getLives(targetPlayer.uniqueId)
        lifeManager.setLives(targetPlayer.uniqueId, targetLives + amount)

        sender.sendMessage("&aYou gave $amount lives to ${targetPlayer.name}. You now have ${senderLives - amount} lives.")
        targetPlayer.sendMessage("&aYou received $amount lives from ${sender.name}. You now have ${targetLives + amount} lives.")
    }
}
