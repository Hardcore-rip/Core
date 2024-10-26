package rip.hardcore.basic.commands.Warps;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.hardcore.basic.manager.LocationManager;
import rip.hardcore.basic.storage.Warps;
import rip.hardcore.filter.util.TranslationKt;

@CommandAlias("setspawn")
public class SetSpawnCommand extends BaseCommand {

    LocationManager locationManager;
    Warps warps;

    public SetSpawnCommand(LocationManager locationManager, Warps warps) {
        this.locationManager = locationManager;
        this.warps = warps;
    }

    @Default
    @CommandPermission("basic.admin")
    public void onCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(TranslationKt.translate("&cError: Console cannot use that command."));
        }
        Player player = (Player) sender;

        if (!warps.getValue("spawn.location.world").isEmpty()) {
            warps.deleteValue("spawn");
        }
        locationManager.registerWarp("spawn", player.getLocation(), results ->{
            switch (results){
                case "existing":
                    System.out.print("Error while initalizing warp");
                    player.sendMessage(TranslationKt.translate("&cError while initalizing warp"));
                    break;
                case "created":
                    player.sendMessage(TranslationKt.translate("&#46e086Updated spawn location to your location!"));
                    break;
                default:
                    break;
            }
        });
    }
}
