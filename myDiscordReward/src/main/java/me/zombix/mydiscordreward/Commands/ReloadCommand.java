package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Bot.Bot;
import me.zombix.mydiscordreward.Config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private String successfullyReloaded;
    private String noPermission;

    public ReloadCommand(ConfigManager configManager) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.successfullyReloaded = ChatColor.translateAlternateColorCodes('&', "&aPlugin myDiscordReward has been reloaded!");
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("mydiscordreward.admin")) {
            configManager.setupConfig();
            Bot.shutdown();
            MyDiscordReward.runBot();
            MyDiscordReward.unloadChannels();
            MyDiscordReward.loadChannels();

            sender.sendMessage(successfullyReloaded);
        } else {
            sender.sendMessage(noPermission.replace("{player}", sender.getName()));
        }
        return true;
    }

}
