package me.zombix.myDiscordReward;

import me.zombix.myDiscordReward.Bot.Bot;
import me.zombix.myDiscordReward.Commands.MyDiscordRewardCommand;
import me.zombix.myDiscordReward.Managers.CommandsTabCompleteManager;
import me.zombix.myDiscordReward.Managers.ConfigManager;
import me.zombix.myDiscordReward.Managers.UpdatesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MyDiscordReward extends JavaPlugin {
    @Override
    public void onEnable() {
        new ConfigManager(this);
        ConfigManager.loadConfig();

        registerCommands();

        runBot();

        getLogger().info("Plugin myDiscordReward has been enabled!");

        if (ConfigManager.getMainConfig().getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutting down Discord bot...");
        Bot.shutdown();
        getLogger().info("Plugin myDiscordReward has been disabled!");
    }

    private void registerCommands() {
        CommandExecutor myDiscordRewardCommand = new MyDiscordRewardCommand(this);
        TabCompleter commandsTabCompleter = new CommandsTabCompleteManager();

        getCommand("mydiscordreward").setExecutor(myDiscordRewardCommand);
        getCommand("mydiscordreward").setTabCompleter(commandsTabCompleter);
    }

    public static void runBot() {
        String token = ConfigManager.getMainConfig().getString("bot-token");
        Logger logger = ConfigManager.getPlugin().getLogger();

        if (token != null && !token.isEmpty()) {
            try {
                new Bot(token);
            } catch (Exception e) {
                if (e.toString().contains("The provided token is invalid!")) {
                    logger.severe("There was en error while enabling bot! Please check is token correct.");
                } else {
                    logger.severe(e.toString());
                }
                ConfigManager.getPlugin().getServer().getScheduler().runTaskLater(ConfigManager.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        ConfigManager.getPlugin().getServer().getPluginManager().disablePlugin(ConfigManager.getPlugin());
                    }
                }, 1L);
            }
        } else {
            logger.severe("There was en error while enabling bot! Please sing the bot token in config.yml.");
            ConfigManager.getPlugin().getServer().getScheduler().runTaskLater(ConfigManager.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    ConfigManager.getPlugin().getServer().getPluginManager().disablePlugin(ConfigManager.getPlugin());
                }
            }, 1L);
        }
    }

    private void checkForUpdates() {
        String pluginName = "myDiscordReward";
        String currentVersion = "v" + getDescription().getVersion();
        String owner = "Zombix3000";
        String repository = "myDiscordReward";

        UpdatesManager updatesManager = new UpdatesManager(pluginName, currentVersion, owner, repository, this);

        if (updatesManager.checkForUpdates()) {
            getLogger().warning("A new version of the plugin is available! (Current: " + getDescription().getVersion() + ", Latest: " + updatesManager.getLatestVersion() + ")");
            if (ConfigManager.getMainConfig().getBoolean("auto-update")) {
                getLogger().warning("Plugin autoupdate will start in 10 seconds!");
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        getLogger().info("Updating plugin...");
                        updatesManager.updatePlugin(null);
                    }
                }, 200L);
            }
        } else {
            getLogger().info("The current version of the plugin is the latest.");
        }
    }

}
