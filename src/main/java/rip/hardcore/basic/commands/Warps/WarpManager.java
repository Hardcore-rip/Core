package rip.hardcore.basic.commands.Warps;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class WarpManager {
    private final ConcurrentHashMap<UUID, Boolean> warpingPlayers;

    public WarpManager() {
        this.warpingPlayers = new ConcurrentHashMap<UUID, Boolean>();
    }

    public void addWarp(UUID uuid) {
        this.warpingPlayers.put(uuid, true);
    }

    public void removeWarp(UUID uuid) {
        this.warpingPlayers.remove(uuid);
    }

    public boolean isWarping(UUID uuid) {
        return this.warpingPlayers.getOrDefault(uuid, false);
    }
}
