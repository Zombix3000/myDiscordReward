package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Config.ConfigManager;
import me.zombix.mydiscordreward.Config.Updates;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MyDiscordRewardCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final Updates updates;
    private final JavaPlugin plugin;
    private final String noPermission;

    public MyDiscordRewardCommand(ConfigManager configManager, Updates updates, JavaPlugin plugin) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.updates = updates;
        this.plugin = plugin;
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("mydiscordreward.mydiscordreward")) {
            if (args.length == 0) {
                return false;
            }

            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("reload")) {
                ReloadCommand reloadCommand = new ReloadCommand(configManager);
                return reloadCommand.onCommand(sender, command, label, args);
            } else if (subCommand.equalsIgnoreCase("update")) {
                UpdateCommand updateCommand = new UpdateCommand(configManager, updates, plugin);
                return updateCommand.onCommand(sender, command, label, args);
            } else if (subCommand.equalsIgnoreCase("addrewarditem")) {
                AddRewardItemCommand addRewardItemCommand = new AddRewardItemCommand(configManager);
                return addRewardItemCommand.onCommand(sender, command, label, args);
            } else if (subCommand.equalsIgnoreCase("addrewardcommand")) {
                AddRewardCommandCommand addRewardCommandCommand = new AddRewardCommandCommand(configManager);
                return addRewardCommandCommand.onCommand(sender, command, label, args);
            } else {
                return false;
            }
        } else {
            sender.sendMessage(noPermission.replace("{player}", sender.getName()));
            return true;
        }
    }

}
