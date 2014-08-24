package me.sonarbeserk.timedbroadcast.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

public class Messages {
    private JavaPlugin plugin = null;

    private FileConfiguration messages = null;
    private File messageFile = null;

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Reloads the message file
     */
    public void reload() {
        if (messageFile == null) {
            messageFile = new File(plugin.getDataFolder(), "messages.yml");
        }

        messages = YamlConfiguration.loadConfiguration(messageFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource("messages.yml");

        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        } else {
            plugin.getLogger().severe("Unable to load default messages!");
            return;
        }
    }

    private FileConfiguration getFileConfiguration() {
        if (messages == null) {
            reload();
        }

        return messages;
    }

    /**
     * Saves the default version of the message file if it was not found
     */
    private void saveDefault() {
        if (messageFile == null) {
            messageFile = new File(plugin.getDataFolder(), "messages.yml");
        }

        if (!messageFile.exists()) {
            plugin.saveResource("messages.yml", false);
            return;
        }
    }

    /**
     * Saves the current state of the message file, also destroys any comments in the file
     */
    public void save() {
        if (messages == null || messageFile == null) {
            return;
        }

        try {
            getFileConfiguration().save(messageFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save messages file to " + messageFile, ex);
        }
    }

    /**
     * Sets the value at the path specified
     *
     * @param path  the path to set at
     * @param value the value to set
     */
    public void set(String path, Object value) {
        messages.set(path, value);
    }

    /**
     * Returns a data entry from the path specified
     *
     * @param path the path to the entry
     * @return a data entry from the path specified
     */
    public Object get(String path) {
        return getFileConfiguration().get(path);
    }

    /**
     * Returns the configuration section at the path specified
     *
     * @param path the path to the configuration section
     * @return the configuration section at the path specified
     */
    public ConfigurationSection getConfigurationSection(String path) {
        return getFileConfiguration().getConfigurationSection(path);
    }

    /**
     * Creates a configuration section with a map at the path specified
     *
     * @param path the path to create the section at
     * @param map  the value to set
     */
    public void createSection(String path, Map<?, ?> map) {
        getFileConfiguration().createSection(path, map);
    }
}
