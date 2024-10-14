package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CheckCommand implements CommandExecutor {
    private static String checkUser;
    private static String badUser;

    public CheckCommand() {
        reloadValues();
    }

    public static void reloadValues() {
        FileConfiguration messagesConfig = ConfigManager.getMessagesConfig();
        checkUser = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("check-user"));
        badUser = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("bad-user"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            return false;
        }

        FileConfiguration mainConfig = ConfigManager.getMainConfig();
        String ID = args[1];
        if (ID.isEmpty()) {
            return false;
        }

        List<List<Object>> listDiscord = (List<List<Object>>) mainConfig.getList("discord-list");
        List<List<Object>> listMinecraft = (List<List<Object>>) mainConfig.getList("minecraft-list");

        checkLists(sender, ID, listDiscord);
        checkLists(sender, ID, listMinecraft);

        return true;
    }

    private void checkLists(CommandSender sender, String ID, List<List<Object>> list) {
        if (list != null) {
            if (list.contains(ID)) {
                for (List<Object> sublist : list) {
                    if (sublist.contains(ID)) {
                        int rewardUses = (Integer) sublist.get(1);
                        String user = (String) sublist.get(2);
                        sender.sendMessage(checkUser.replace("{user}", user).replace("{provided-user}", ID).replace("{uses}", String.valueOf(rewardUses)));
                    }
                }
            } else {
                sender.sendMessage(badUser.replace("{provided-user}", ID));
            }
        } else {
            sender.sendMessage(badUser.replace("{provided-user}", ID));
        }
    }

}
