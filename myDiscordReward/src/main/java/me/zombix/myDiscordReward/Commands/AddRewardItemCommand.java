package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddRewardItemCommand implements CommandExecutor {
    private static String successfullyAdd;
    private static String badItem;

    public AddRewardItemCommand() {
        reloadValues();
    }

    public static void reloadValues() {
        FileConfiguration messagesConfig = ConfigManager.getMessagesConfig();
        successfullyAdd = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("successfully-add-item"));
        badItem = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-item"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (itemInHand != null && !itemInHand.getType().equals(Material.AIR)) {
            FileConfiguration mainConfig = ConfigManager.getMainConfig();

            int smallestFreeSlot = 1;
            while (mainConfig.contains("reward.items." + smallestFreeSlot)) {
                smallestFreeSlot++;
            }

            mainConfig.set("reward.items." + smallestFreeSlot + ".material", itemInHand.getType().toString());
            mainConfig.set("reward.items." + smallestFreeSlot + ".meta", itemInHand);
            mainConfig.set("reward.items." + smallestFreeSlot + ".amount", itemInHand.getAmount());

            ConfigManager.saveMainConfig();

            player.sendMessage(successfullyAdd.replace("{player}", player.getName()));
        } else {
            player.sendMessage(badItem.replace("{player}", player.getName()));
        }

        return true;
    }

}
