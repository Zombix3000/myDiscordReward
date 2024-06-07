package me.zombix.mydiscordreward.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration mainConfig;
    private FileConfiguration messagesConfig;
    private FileConfiguration discordConfig;
    private FileConfiguration usersConfig;
    private final File configFile;
    private final File messagesFile;
    private final File discordFile;
    private final File usersFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        this.discordFile = new File(plugin.getDataFolder(), "discord.yml");
        this.usersFile = new File(plugin.getDataFolder(), "users.yml");
    }

    public void setupConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        if (!discordFile.exists()) {
            plugin.saveResource("discord.yml", false);
        }
        if (!usersFile.exists()) {
            plugin.saveResource("users.yml", false);
        }

        mainConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        messagesConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "messages.yml"));
        discordConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "discord.yml"));
        usersConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "users.yml"));
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public FileConfiguration getMainConfig() {
        return mainConfig;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    public FileConfiguration getDiscordConfig() {
        return discordConfig;
    }

    public FileConfiguration getUsersConfig() {
        return usersConfig;
    }

    public void saveMainConfig() {
        try {
            mainConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save config.yml!");
        }
    }

    public void saveDiscordConfig() {
        try {
            discordConfig.save(discordFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save discord.yml!");
        }
    }

    public void saveUsersConfig() {
        try {
            usersConfig.save(usersFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save users.yml!");
        }
    }

    public String[] getPlayers() {
        String[] players = new String[plugin.getServer().getOnlinePlayers().size()];
        int i = 0;

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            players[i] = onlinePlayer.getName();
            i++;
        }

        return players;
    }

    public boolean canGetReward(String nick, String dc) {
        FileConfiguration usersConfig = getUsersConfig();
        List<?> list = usersConfig.getList("users");

        if (list != null) {
            if (list.contains(nick) || list.contains(dc)) {
                return false;
            }
        }

        return true;
    }

}