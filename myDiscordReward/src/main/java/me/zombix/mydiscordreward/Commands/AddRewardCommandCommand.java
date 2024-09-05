package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class AddRewardCommandCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private String noPermission;
    private String successfullyAdd;
    private String badCommand;
    private String badSender;

    public AddRewardCommandCommand(ConfigManager configManager) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
        this.successfullyAdd = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("successfully-add-command"));
        this.badCommand = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-command"));
        this.badSender = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-sender"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("mydiscordreward.admin")) {
                String[] remainingArgs = new String[args.length - 1];
                System.arraycopy(args, 1, remainingArgs, 0, args.length - 1);

                String rewardCommand = String.join(" ", remainingArgs).replace("/", "");
                if (!rewardCommand.isEmpty()) {
                    FileConfiguration mainConfig = configManager.getMainConfig();

                    List<String> rewardCommands = mainConfig.getStringList("reward.commands");

                    rewardCommands.add(rewardCommand);

                    mainConfig.set("reward.commands", rewardCommands);

                    configManager.saveMainConfig();

                    player.sendMessage(successfullyAdd.replace("{player}", player.getName()));
                } else {
                    player.sendMessage(badCommand.replace("{player}", player.getName()));
                }
            } else {
                player.sendMessage(noPermission.replace("{player}", player.getName()));
            }
        } else {
            sender.sendMessage(badSender);
        }
        return true;
    }

}
