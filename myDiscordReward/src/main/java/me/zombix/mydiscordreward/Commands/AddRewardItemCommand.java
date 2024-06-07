package me.zombix.mydiscordreward.Commands;

import me.zombix.mydiscordreward.Config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddRewardItemCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final String noPermission;
    private final String successfullyAdd;
    private final String badItem;
    private final String badSender;

    public AddRewardItemCommand(ConfigManager configManager) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.noPermission = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("no-permission"));
        this.successfullyAdd = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("successfully-add-item"));
        this.badItem = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-item"));
        this.badSender = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-sender"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission("mydiscordreward.addrewarditem")) {
                ItemStack itemInHand = player.getInventory().getItemInMainHand();

                if (itemInHand != null && !itemInHand.getType().equals(Material.AIR)) {
                    FileConfiguration mainConfig = configManager.getMainConfig();

                    int smallestFreeSlot = 1;
                    while (mainConfig.contains("reward.items." + smallestFreeSlot)) {
                        smallestFreeSlot++;
                    }

                    mainConfig.set("reward.items." + smallestFreeSlot + ".material", itemInHand.getType().toString());
                    mainConfig.set("reward.items." + smallestFreeSlot + ".meta", itemInHand);
                    mainConfig.set("reward.items." + smallestFreeSlot + ".amount", itemInHand.getAmount());

                    configManager.saveMainConfig();

                    player.sendMessage(successfullyAdd.replace("{player}", player.getName()));
                } else {
                    player.sendMessage(badItem.replace("{player}", player.getName()));
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
