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

public final class MyDiscordReward extends JavaPlugin {
    private ConfigManager configManager;
    private Updates updates;
    private Bot discordBot;
    private GiveReward giveReward;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        giveReward = new GiveReward(configManager, this);
        configManager.setupConfig();

        registerCommands();

        String token = configManager.getMainConfig().getString("bot-token");

        if (!Objects.equals(token, "nope")) {
            try {
                discordBot = new Bot(token, configManager, giveReward);
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().severe("There was en error while enabling bot! Please check is token correct.");
            }
        } else {
            getLogger().severe("There was en error while enabling bot! Please sing the bot token in config.yml.");
        }

        getLogger().info("Plugin myDiscordReward has been enabled!");

        if (configManager.getMainConfig().getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin myDiscordReward has been disabled!");
    }

    private void registerCommands() {
        CommandExecutor myDiscordRewardCommand = new MyDiscordRewardCommand(configManager, updates, this);
        TabCompleter commandsTabCompleter = new CommandsTabCompleter();

        getCommand("mydiscordreward").setExecutor(myDiscordRewardCommand);
        getCommand("mydiscordreward").setTabCompleter(commandsTabCompleter);
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
