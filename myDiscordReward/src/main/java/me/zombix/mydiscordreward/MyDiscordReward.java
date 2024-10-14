package me.zombix.mydiscordreward;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.zombix.mydiscordreward.Actions.GiveReward;
import me.zombix.mydiscordreward.Bot.Bot;
import me.zombix.mydiscordreward.Commands.MyDiscordRewardCommand;
import me.zombix.mydiscordreward.Config.CommandsTabCompleter;
import me.zombix.mydiscordreward.Config.ConfigManager;
import me.zombix.mydiscordreward.Config.Updates;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public final class MyDiscordReward extends JavaPlugin implements PluginMessageListener {
    private static JavaPlugin plugin;
    private static ConfigManager configManager;
    private static MyDiscordReward myDiscordReward;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager(this);
        configManager.setupConfig();
        myDiscordReward = this;
        new GiveReward(configManager, this);

        if (!configManager.getMainConfig().getBoolean("proxy-enabled")) {
            runBot();
        }

        loadChannels();

        registerCommands();

        getLogger().info("Plugin myDiscordReward has been enabled!");

        if (configManager.getMainConfig().getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");
            checkForUpdates();
        }
    }

    @Override
    public void onDisable() {
        Bot.shutdown();
        unloadChannels();
        getLogger().info("Plugin myDiscordReward has been disabled!");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("mydiscordreward:proxy")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("ServerSetup")) {
            String serverName = in.readUTF();

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("RegisteredServer");
            out.writeUTF(serverName);
            player.sendPluginMessage(this, "mydiscordreward:proxy", out.toByteArray());

            FileConfiguration mainConfig = configManager.getMainConfig();
            mainConfig.set("proxy-enabled", true);
            mainConfig.set("server-name", serverName);
            configManager.saveMainConfig();

            unloadChannels();
            loadChannels();
        }
    }

    public static void loadChannels() {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "mydiscordreward:proxy", myDiscordReward);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "mydiscordreward:proxy");

        if (configManager.getMainConfig().getBoolean("proxy-enabled")) {
            plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "mydiscordreward:" + configManager.getMainConfig().getString("server-name"), myDiscordReward);
        }
    }

    public static void unloadChannels() {
        try {
            plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin);
            plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            logger.severe("If you use myDiscordRewardProxy please add server to servers with reward!");
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
