package me.zombix.myDiscordReward.Commands;

import me.zombix.myDiscordReward.Bot.Bot;
import me.zombix.myDiscordReward.Bot.Interactinos.ModalInteract;
import me.zombix.myDiscordReward.Managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    public ReloadCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigManager.loadConfig();
        Bot.setBotStatus();
        Bot.setBotActivity();
        MyDiscordRewardCommand.reloadValues();
        AddRewardCommandCommand.reloadValues();
        AddRewardItemCommand.reloadValues();
        ModalInteract.reloadValues();

        sender.sendMessage(ChatColor.GREEN + "Plugin myDiscordReward has been reloaded!");
        return true;
    }

}
