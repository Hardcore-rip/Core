package rip.hardcore.basic;

import co.aikar.commands.PaperCommandManager;
import com.samjakob.spigui.SpiGUI;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import rip.hardcore.basic.commands.BroadcastCommand;
import rip.hardcore.basic.commands.DiscordCommand;
import rip.hardcore.basic.commands.StatsCommand;
import rip.hardcore.basic.commands.Warps.SetSpawnCommand;
import rip.hardcore.basic.commands.Warps.SpawnCommand;
import rip.hardcore.basic.commands.Warps.WarpManager;
import rip.hardcore.basic.commands.Warps.WarpRequester;
import rip.hardcore.basic.commands.homes.AdminHomeCommand;
import rip.hardcore.basic.commands.homes.HomeCommand;
import rip.hardcore.basic.commands.tags.TagCommand;
import rip.hardcore.basic.commands.tags.TagMenu;
import rip.hardcore.basic.listeners.General;
import rip.hardcore.basic.listeners.HomeListener;
import rip.hardcore.basic.listeners.OnFirstJoin;
import rip.hardcore.basic.listeners.PlayerDeath;
import rip.hardcore.basic.manager.HomeManager;
import rip.hardcore.basic.manager.LifeManager;
import rip.hardcore.basic.commands.LivesCommand;
import org.bukkit.permissions.Permission;
import rip.hardcore.basic.manager.LocationManager;
import rip.hardcore.basic.manager.TagManager;
import rip.hardcore.basic.storage.Tags;
import rip.hardcore.basic.storage.Warps;
import rip.hardcore.basic.utils.AnvilGUI;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class Basic extends JavaPlugin {

    private LifeManager lifeManager;
    Warps warps;
    WarpManager warpManager;
    WarpRequester warpRequester;
    private HomeManager homeManager;
    LocationManager locationManager;
    TagManager tagManager;
    Tags tags;
    public static SpiGUI spiGUI;

    @Override
    public void onEnable() {
        File dbFile = new File(getDataFolder(), "player_lives.db");
        if (!dbFile.exists()) {
            getDataFolder().mkdirs();
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                getLogger().severe("Could not create database file!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        try {
            File configFiles = new File(getDataFolder(), "tags.json");
            if (!configFiles.exists()) {
                saveResource("tags.json", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Failed to load tags file in start up");
            getLogger().severe("Auto disabling Basic... make a ticket in discord");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        tags = new Tags(this);

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            tagManager = new TagManager(getDataFolder().getAbsolutePath() + "/tags.db");
            if (tagManager != null) {
                getLogger().info("Tag data initialized successfully.");
            } else {
                getLogger().severe("Tag data initialization failed.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } catch (Exception e) {
            getLogger().severe("Player data initialization failed.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        spiGUI = new SpiGUI(this);
        lifeManager = new LifeManager(dbFile);
        homeManager = new HomeManager();
        YAML();
        Commands();

        getServer().getPluginManager().registerEvents(new PlayerDeath(lifeManager), this);
        getServer().getPluginManager().registerEvents(new OnFirstJoin(lifeManager), this);
        getServer().getPluginManager().registerEvents(new General(), this);
        getServer().getPluginManager().registerEvents(new HomeListener(homeManager), this);
        //        ("PlaceholderAPI")){
        //}

    }

    private void Commands(){
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DiscordCommand());
        commandManager.registerCommand(new BroadcastCommand());
        commandManager.registerCommand(new LivesCommand(lifeManager));
        commandManager.registerCommand(new StatsCommand(lifeManager));
        commandManager.registerCommand(new SpawnCommand(warpRequester,  locationManager, warps));
        commandManager.registerCommand(new SetSpawnCommand(locationManager, warps));
        commandManager.registerCommand(new HomeCommand(homeManager));
        commandManager.registerCommand(new AdminHomeCommand(homeManager));
        commandManager.registerCommand(new TagCommand(tagManager, new TagMenu(tagManager, tags), tags));

        Bukkit.getPluginManager().addPermission(new Permission("basic.auto", "Automaticaly teleport with warps", PermissionDefault.OP));
    }

    private void YAML(){
        try {
            File configFiles = new File(getDataFolder(), "warps.yml");
            if (!configFiles.exists()) {
                saveResource("warps.yml", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Failed to load warps file in start up");
            getLogger().severe("Auto disabling Basic... make a ticket in discord");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.warps = new Warps(this);
        warpManager = new WarpManager();
        warpRequester = new WarpRequester(warpManager);
        locationManager = new LocationManager(warps);
    }

    private void Listener(){

    }


    @Override
    public void onDisable() {
        if (lifeManager != null) {
            lifeManager.close();
        }
    }
}
