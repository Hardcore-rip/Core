package rip.hardcore.basic;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.hardcore.basic.commands.BroadcastCommand;
import rip.hardcore.basic.commands.DiscordCommand;
import rip.hardcore.basic.commands.StatsCommand;
import rip.hardcore.basic.listeners.General;
import rip.hardcore.basic.listeners.OnFirstJoin;
import rip.hardcore.basic.listeners.PlayerDeath;
import rip.hardcore.basic.manager.LifeManager;
import rip.hardcore.basic.commands.LivesCommand;

import java.io.File;
import java.io.IOException;

public final class Basic extends JavaPlugin {

    private LifeManager lifeManager;

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
        lifeManager = new LifeManager(dbFile);
        Commands();

        getServer().getPluginManager().registerEvents(new PlayerDeath(lifeManager), this);
        getServer().getPluginManager().registerEvents(new OnFirstJoin(lifeManager), this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){

        }

    }

    private void Commands(){
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DiscordCommand());
        commandManager.registerCommand(new BroadcastCommand());
        commandManager.registerCommand(new LivesCommand(lifeManager));
        commandManager.registerCommand(new StatsCommand(lifeManager));
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
