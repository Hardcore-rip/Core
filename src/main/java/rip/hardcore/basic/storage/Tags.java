package rip.hardcore.basic.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tags {

    private final Plugin plugin;
    private File configFile;
    private FileConfiguration config;

    public Tags(Plugin plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "storage/tags.yml");
        if (!configFile.exists()) {
            plugin.saveResource("storage/tags.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public double getDouble(String key) {
        String value = config.getString(key);
        if (value == null) {
            return 0;
        }
        return Double.parseDouble(value);
    }

    public String getValue(String key) {
        String message = config.getString(key);
        if (message == null) {
            return "";
        }
        return message;
    }

    public void setValue(String key, String value) {
        config.set(key, value);
        saveConfig();
    }

    public void setInteger(String key, Integer value) {
        config.set(key, value);
        saveConfig();
    }

    public void setBoolean(String key, Boolean value) {
        config.set(key, value);
        saveConfig();
    }

    public void setDoubble(String key, Double value) {
        config.set(key, value);
        saveConfig();
    }

    public void setFloat(String key, Float value) {
        config.set(key, value);
        saveConfig();
    }

    public void deleteValue(String key) {
        config.set(key, null);
        saveConfig();
    }

    public String listTags() {
        List<String> tags = new ArrayList<>();
        config.getKeys(false).forEach(tags::add);
        return tags.toString();
    }

    public List<String> getTags() {
        List<String> tags = new ArrayList<>();
        config.getKeys(false).forEach(tags::add);
        return tags;
    }

    public int getInteger(String key) {
        try {
            int message = Integer.parseInt(config.getString(key));
            return message;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getBoolean(String key) {
        try {
            boolean message = Boolean.parseBoolean(config.getString(key));
            return message;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "storage/tags.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
