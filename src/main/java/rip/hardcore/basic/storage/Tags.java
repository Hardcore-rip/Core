package rip.hardcore.basic.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import rip.hardcore.filter.util.TranslationKt;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tags {
    private final Plugin plugin;
    private File configFile;
    private JsonObject config;
    private final String filePath = "tags.json";
    private final Gson gson;

    public Tags(Plugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        createConfig();
    }


    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), filePath);
        if (!configFile.exists()) {
            plugin.saveResource(filePath, false);
        }
        loadConfig();
    }

    public String getTag(String key) {
        JsonElement element = config.get(key);

        if (element == null) {
            return "";
        }
        return TranslationKt.translate(element.getAsString());
    }

    public void setTag(String key, String value) {
        config.addProperty(key, value);
        saveConfig();
    }

    public void deleteTag(String key) {
        if (config.has(key)) {
            config.remove(key);
            saveConfig();
        }
    }

    public String listTags() {
        StringBuilder tagsList = new StringBuilder();
        for (Map.Entry<String, JsonElement> entry : config.entrySet()) {
            tagsList.append(entry.getKey()).append(",");
        }
        return tagsList.toString();
    }

    public List<String> tagList(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : config.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filePath);
        }
        loadConfig();
    }

    private void loadConfig() {
        try (FileReader reader = new FileReader(configFile)) {
            config = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            config = new JsonObject();
        }
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}