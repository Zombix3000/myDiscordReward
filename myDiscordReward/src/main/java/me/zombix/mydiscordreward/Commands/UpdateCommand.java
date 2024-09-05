package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Config.ConfigManager;
import me.zombix.mydiscordreward.Config.Updates;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private String noPermission;

    public UpdateCommand(ConfigManager configManager, JavaPlugin plugin) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.plugin = plugin;
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("mydiscordreward.admin")) {
            checkForUpdates(sender);
        } else {
            sender.sendMessage(noPermission.replace("{player}", sender.getName()));
        }
        return true;
    }

    private void checkForUpdates(CommandSender sender) {
        String pluginName = "myDiscordReward";
        String currentVersion = "v" + plugin.getDescription().getVersion();
        String owner = "Zombix3000";
        String repository = "myDiscordReward";

        Updates updates = new Updates(pluginName, currentVersion, owner, repository, plugin);

        if (updates.checkForUpdates()) {
            updates.updatePlugin();
            sender.sendMessage("Plugin was successfully updated!");
        } else {
            sender.sendMessage("No updates available.");
        }
    }

}
