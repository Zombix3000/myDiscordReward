package me.zombix.mydiscordreward.Actions;

import me.zombix.mydiscordreward.Config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GiveReward {
    private ConfigManager configManager;
    private JavaPlugin plugin;

    public GiveReward(ConfigManager configManager, JavaPlugin plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    public void giveReward(String nick) {
        FileConfiguration mainConfig = configManager.getMainConfig();

        List<String> rewardCommands = mainConfig.getStringList("reward.commands");
        for (String command : rewardCommands) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", nick));
            });
        }

        ConfigurationSection itemsSection = mainConfig.getConfigurationSection("reward.items");

        Player player = Bukkit.getPlayer(nick);

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
