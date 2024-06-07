package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final String successfullyReloaded;
    private final String noPermission;

    public ReloadCommand(ConfigManager configManager) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.successfullyReloaded = ChatColor.translateAlternateColorCodes('&', "&aPlugin myDiscordReward has been reloaded!");
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("mydiscordreward.reload")) {
            configManager.setupConfig();

            sender.sendMessage(successfullyReloaded);
        } else {
            sender.sendMessage(noPermission.replace("{player}", sender.getName()));
        }
        return true;
    }

}
