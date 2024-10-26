package rip.hardcore.basic.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import rip.hardcore.basic.storage.Warps;

import java.util.Objects;
import java.util.function.Consumer;

public class LocationManager {

    Warps warpStorage;

    public LocationManager(Warps warp) {
        this.warpStorage = warp;
    }

    public Location formatLocation(String warp) {

        String worldName = warpStorage.getValue(warp + ".location.world");
        double x = warpStorage.getDouble(warp + ".location.x");
        double y = warpStorage.getDouble(warp + ".location.y");
        double z = warpStorage.getDouble(warp + ".location.z");
        float pitch = (float) warpStorage.getDouble(warp + ".location.pitch");
        float yaw = (float) warpStorage.getDouble(warp + ".location.yaw");
        World world = Bukkit.getWorld(worldName);
        Location location = new Location(world, x, y, z, yaw, pitch);
        if (location.getWorld() == null){
            return null;
        } else {
            return location;
        }
    }

    public void registerWarp(String warp, Location location, Consumer<String> callback) {
        if (warpStorage.getValue(warp + ".location.world") != null && !warpStorage.getValue(warp + ".location.world").isEmpty()){
            callback.accept("existing");
        } else {
            callback.accept("created");
            warpStorage.setValue(warp + ".location.world", Objects.requireNonNull(location.getWorld()).getName());
            warpStorage.setDoubble(warp + ".location.x", location.getX());
            warpStorage.setDoubble(warp + ".location.y", location.getY());
            warpStorage.setDoubble(warp + ".location.z", location.getZ());
            warpStorage.setFloat(warp + ".location.pitch", location.getPitch());
            warpStorage.setFloat(warp + ".location.yaw", location.getYaw());
            warpStorage.saveConfig();
        }
        return;
    }
}
