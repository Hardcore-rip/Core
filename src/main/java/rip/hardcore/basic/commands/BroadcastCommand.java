package rip.hardcore.basic.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.filter.util.TranslationKt;

import java.util.Arrays;

@CommandAlias("broadcast|bc|alert")
public class BroadcastCommand extends BaseCommand {

    @Default
    @CommandPermission("basic.broadcast")
    public void onCommand(CommandSender sender, String[] args) {
        String message = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        Bukkit.broadcastMessage(TranslationKt.translate("&4&lALERT &f» " + message));
        for(Player player : Bukkit.getOnlinePlayers()){
            Sound sounds = Sound.BLOCK_BELL_RESONATE;
            if (sounds != null){
                player.playSound(player.getLocation(), sounds, 1, 1);
            } else {
                System.out.print("Failed to send broadcast sound due to an error in configuration");
            }
        }
    }
}
