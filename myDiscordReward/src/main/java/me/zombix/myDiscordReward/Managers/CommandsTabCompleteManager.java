package me.zombix.myDiscordReward.Managers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandsTabCompleteManager implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String enteredCommand = args[0].toLowerCase();
            List<String> subCommands = new ArrayList<>();

            if (command.getName().toLowerCase().equals("mydiscordreward")) {
                if (sender.hasPermission("mydiscordreward.admin")) {
                    subCommands.add("addrewarditem");
                    subCommands.add("addrewardcommand");
                    subCommands.add("reload");
                    subCommands.add("update");
                }
            }

            for (String subCommand : subCommands) {
                if (subCommand.startsWith(enteredCommand)) {
                    completions.add(subCommand);
                }
            }
        }

        return completions;
    }

}
