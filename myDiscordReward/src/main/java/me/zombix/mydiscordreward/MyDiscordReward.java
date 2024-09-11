package me.zombix.mydiscordreward;

import me.zombix.mydiscordreward.Actions.GiveReward;
import me.zombix.mydiscordreward.Bot.Bot;
import me.zombix.mydiscordreward.Commands.MyDiscordRewardCommand;
import me.zombix.mydiscordreward.Config.CommandsTabCompleter;
import me.zombix.mydiscordreward.Config.ConfigManager;
import me.zombix.mydiscordreward.Config.Updates;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class MyDiscordReward extends JavaPlugin {
    private static ConfigManager configManager;
    private GiveReward giveReward;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.setupConfig();
        giveReward = new GiveReward(configManager, this);

        registerCommands();
        runBot();

        getLogger().info("Plugin myDiscordReward has been enabled!");

        if (configManager.getMainConfig().getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        Bot.shutdown();
        getLogger().info("Plugin myDiscordReward has been disabled!");
    }

    private void registerCommands() {
        CommandExecutor myDiscordRewardCommand = new MyDiscordRewardCommand(configManager, this);
        TabCompleter commandsTabCompleter = new CommandsTabCompleter();

        getCommand("mydiscordreward").setExecutor(myDiscordRewardCommand);
        getCommand("mydiscordreward").setTabCompleter(commandsTabCompleter);
    }

    public static void runBot() {
        String token = configManager.getMainConfig().getString("bot-token");
        Logger logger = configManager.getPlugin().getLogger();

        if (!Objects.equals(token, "nope")) {
            try {
                new Bot(token, configManager);
            } catch (Exception e) {
                if (e.toString().contains("The provided token is invalid!")) {
                    logger.severe("There was en error while enabling bot! Please check is token correct.");
                } else {
                    logger.severe(e.toString());
                }
                configManager.getPlugin().getServer().getPluginManager().disablePlugin(configManager.getPlugin());
            }
        } else {
            logger.severe("There was en error while enabling bot! Please sing the bot token in config.yml.");
            configManager.getPlugin().getServer().getPluginManager().disablePlugin(configManager.getPlugin());
        }
    }

    private void checkForUpdates() {
        String pluginName = "myDiscordReward";
        String currentVersion = "v" + getDescription().getVersion();
        String owner = "Zombix3000";
        String repository = "myDiscordReward";

        Updates updates = new Updates(pluginName, currentVersion, owner, repository, this);

        if (updates.checkForUpdates()) {
            getLogger().warning("A new version of the plugin is available! (Current: " + getDescription().getVersion() + ", Latest: " + updates.getLatestVersion() + ")");
        } else {
            getLogger().info("The current version of the plugin is the latest.");
        }
    }

}
