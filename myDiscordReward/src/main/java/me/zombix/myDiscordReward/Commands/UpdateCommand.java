package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Managers.UpdatesManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getLogger;

public class UpdateCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public UpdateCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String pluginName = "myDiscordReward";
        String currentVersion = "v" + plugin.getDescription().getVersion();
        String owner = "Zombix3000";
        String repository = "myDiscordReward";

        UpdatesManager updates = new UpdatesManager(pluginName, currentVersion, owner, repository, plugin);

        if (updates.checkForUpdates()) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.YELLOW + "Updating plugin...");
            }
            getLogger().info("[myDiscordReward] Updating plugin...");
            updates.updatePlugin(sender);
        } else {
            sender.sendMessage("No updates available.");
        }

        return true;
    }

}
