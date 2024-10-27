package rip.hardcore.basic.commands.Warps;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.basic.manager.LocationManager;
import rip.hardcore.basic.storage.Warps;
import rip.hardcore.filter.util.TranslationKt;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    WarpRequester warpRequester;
    LocationManager locationManager;
    Warps warps;

    public SpawnCommand(WarpRequester warpRequester, LocationManager locationManager, Warps warp) {
        this.warpRequester = warpRequester;
        this.locationManager = locationManager;
        this.warps = warp;
    }

    @Default
    public void onCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationKt.translate("&cError: Console cannot use this command"));
            return;
        }
        Player player = (Player) sender;

        if (warps.getValue("spawn.location.world").isEmpty()) {
            player.sendMessage(TranslationKt.translate("&cNo spawn location is currently set."));
            return;
        }

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
