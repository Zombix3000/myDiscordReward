package me.zombix.myDiscordReward.Actions;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GiveReward {
    public GiveReward() {}

    public static void giveReward(String nick) {
        FileConfiguration mainConfig = ConfigManager.getMainConfig();

        List<String> rewardCommands = mainConfig.getStringList("reward.commands");
        for (String command : rewardCommands) {
            Bukkit.getScheduler().runTask(ConfigManager.getPlugin(), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', command.replace("{player}", nick)));
            });
        }

        ConfigurationSection itemsSection = mainConfig.getConfigurationSection("reward.items");

        Player player = Bukkit.getPlayer(nick);

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                Material material = Material.getMaterial(itemSection.getString("material"));
                int amount = itemSection.getInt("amount");
                ItemStack itemStack = new ItemStack(material, amount);
                ItemStack itemStackMeta = itemSection.getItemStack("meta");
                ItemMeta itemMeta = itemStackMeta.getItemMeta();
                itemStack.setItemMeta(itemMeta);

                player.getInventory().addItem(itemStack);
            }
        }
    }

}
