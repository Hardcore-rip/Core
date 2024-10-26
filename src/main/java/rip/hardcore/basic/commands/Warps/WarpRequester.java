package rip.hardcore.basic.commands.Warps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.hardcore.basic.Basic;

import java.io.ObjectInputFilter;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class WarpRequester {


    WarpManager manager;
    Basic plugin;

    public WarpRequester(WarpManager manager) {
        this.manager = manager;
    }

    public void requestWarp(UUID uuid, Location location, double delay, boolean concurrentMessage, String message, Consumer<String> callback) {
        if (!(manager.isWarping(uuid))){
            manager.addWarp(uuid);

            Player player = Bukkit.getPlayer(uuid);
            Location currentLocation = player.getLocation();


            if (delay == 0 || Bukkit.getPlayer(uuid).hasPermission("basic.auto")){
                player.teleport(location);
                callback.accept("teleported");
            } else {
                new BukkitRunnable() {
                    byte count = 0;
                    @Override
                    public void run() {
                        if (count < delay){
                            if (concurrentMessage){
                                player.sendMessage(message.replace("{COUNTDOWN}", Math.round(delay - count) + ""));
                                player.sendActionBar(message.replace("{COUNTDOWN}", Math.round(delay - count) + ""));
                            }
                            count++;
                            if (!manager.isWarping(player.getUniqueId())) {
                                callback.accept("teleportation-canceled");
                                cancel();
                            }
                            if (!Objects.equals(formatWithString(currentLocation), formatWithString(player.getLocation()))) {
                                callback.accept("invalid-location");
                                manager.removeWarp(player.getUniqueId());
                                cancel();
                            }
                        } else {
                            manager.removeWarp(uuid);
                            callback.accept("teleported");
                            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.6f, 1);
                            player.teleport(location);
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }
        } else {
            return;
        }
    }


    public String formatWithString(Location loc) {
        String x = loc.getX() + "";
        String y = loc.getY() + "";
        String z = loc.getZ() + "";
        return x + "-" + y + "-" + z;
    }
}
