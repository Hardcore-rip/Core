package rip.hardcore.basic.commands.Warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.basic.manager.LocationManager;
import rip.hardcore.filter.util.TranslationKt;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    WarpRequester warpRequester;
    LocationManager locationManager;

    public SpawnCommand(WarpRequester warpRequester, LocationManager locationManager) {
        this.warpRequester = warpRequester;
        this.locationManager = locationManager;
    }

    @Default
    public void onCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationKt.translate("&cError: Console cannot use this command"));
        }
        Player player = (Player) sender;
        warpRequester.requestWarp(player.getUniqueId(), locationManager.formatLocation("spawn"), 5, true,"&#46e086Teleporting to spawn in {COUNTDOWN}.", results -> {
            switch (results){
                case "invalid-location":
                    player.sendMessage(TranslationKt.translate("&cError: you moved while teleporting"));
                    break;
                case "teleportation-canceled":
                    player.sendMessage(TranslationKt.translate("&cError: teleportation canceled"));
                    break;
                case "teleported":
                    player.sendMessage(TranslationKt.translate("&#46e086Teleported to spawn!"));
            }
        });
    }
}
