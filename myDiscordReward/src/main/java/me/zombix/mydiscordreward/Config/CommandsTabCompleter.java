package me.zombix.mydiscordreward.Config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String enteredCommand = args[0].toLowerCase();

            List<String> subCommands = new ArrayList<>();

            if (command.getName().toLowerCase().equals("mydiscordreward")) {
                if (sender.hasPermission("mydiscordreward.reload")) {
                    subCommands.add("reload");
                }
                if (sender.hasPermission("mydiscordreward.update")) {
                    subCommands.add("update");
                }
                if (sender.hasPermission("mydiscordreward.addrewarditem")) {
                    subCommands.add("addrewarditem");
                }
                if (sender.hasPermission("mydiscordreward.addrewardcommand")) {
                    subCommands.add("addrewardcommand");
                }
            }

            for (String subCommand : subCommands) {
                if (subCommand.startsWith(enteredCommand)) {
                    completions.add(subCommand);
                }
            }
        }

        completions.replaceAll(completion -> completion.replaceFirst("^mydiscordreward:", ""));

        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

}
