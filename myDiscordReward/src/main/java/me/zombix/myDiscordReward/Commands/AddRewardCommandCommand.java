package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class AddRewardCommandCommand implements CommandExecutor {
    private static String successfullyAdd;
    private static String badCommand;

    public AddRewardCommandCommand() {
        reloadValues();
    }

    public static void reloadValues() {
        FileConfiguration messagesConfig = ConfigManager.getMessagesConfig();
        successfullyAdd = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("successfully-add-command"));
        badCommand = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-command"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String[] remainingArgs = new String[args.length - 1];
        System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);

        String rewardCommand = String.join(" ", remainingArgs).replace("/", "");
        if (!rewardCommand.isEmpty()) {
            FileConfiguration mainConfig = ConfigManager.getMainConfig();

            List<String> rewardCommands = mainConfig.getStringList("reward.commands");
            rewardCommands.add(rewardCommand);
            mainConfig.set("reward.commands", rewardCommands);

            ConfigManager.saveMainConfig();

            sendConfigMessage(sender, sender instanceof Player, successfullyAdd);
        } else {
            sendConfigMessage(sender, sender instanceof Player, badCommand);
        }

        return true;
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
