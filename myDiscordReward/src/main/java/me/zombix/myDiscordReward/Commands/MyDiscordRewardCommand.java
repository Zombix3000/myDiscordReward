package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MyDiscordRewardCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private static String noPermission;
    private static String badSender;

    public MyDiscordRewardCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadValues();
    }

    public static void reloadValues() {
        FileConfiguration messagesConfig = ConfigManager.getMessagesConfig();
        noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission-minecraft"));
        badSender = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-sender"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String subCommand = args[0];

        if (sender instanceof Player) {
            if (sender.hasPermission("mydiscordreward.admin")) {
                if (subCommand.equalsIgnoreCase("addrewarditem")) {
                    AddRewardItemCommand addRewardItemCommand = new AddRewardItemCommand();
                    return addRewardItemCommand.onCommand(sender, command, label, args);
                } else return commandExecute(sender, command, label, args, subCommand);
            } else if (sender.hasPermission("mydiscordreward.check") && subCommand.equalsIgnoreCase("check")) {
                CheckCommand checkCommand = new CheckCommand();
                return checkCommand.onCommand(sender, command, label, args);
            } else {
                sendConfigMessage(sender, true, noPermission);
                return true;
            }
        } else if (subCommand.equalsIgnoreCase("addrewarditem")) {
            sendConfigMessage(sender, false, badSender);
            return true;
        } else {
            return commandExecute(sender, command, label, args, subCommand);
        }
    }

    private boolean commandExecute(CommandSender sender, Command command, String label, String[] args, String subCommand) {
        if (subCommand.equalsIgnoreCase("addrewardcommand")) {
            AddRewardCommandCommand addRewardCommandCommand = new AddRewardCommandCommand();
            return addRewardCommandCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("check")) {
            CheckCommand checkCommand = new CheckCommand();
            return checkCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("reload")) {
            ReloadCommand reloadCommand = new ReloadCommand();
            return reloadCommand.onCommand(sender, command, label, args);
        } else if (subCommand.equalsIgnoreCase("update")) {
            UpdateCommand updateCommand = new UpdateCommand(plugin);
            return updateCommand.onCommand(sender, command, label, args);
        } else {
            return false;
        }
    }

    private void sendConfigMessage(CommandSender sender, Boolean player, String message) {
        if (!message.isEmpty()) {
            if (player) {
                sender.sendMessage(message.replace("{player}", sender.getName()));
            } else {
                sender.sendMessage(message);
            }
        }
    }

}
