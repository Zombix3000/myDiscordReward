package me.zombix.mydiscordreward.Bot;

import me.zombix.mydiscordreward.Bot.Interactinos.ButtonInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.ModalInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.SlashCommandInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.StringSelectInteract;
import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    private static JDA jda;
    private static ConfigManager configManager;

    public Bot(String token, ConfigManager configManager) throws LoginException {
        this.configManager = configManager;
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(this);
        jda.addEventListener(new SlashCommandInteract(configManager));
        jda.addEventListener(new StringSelectInteract(configManager));
        jda.addEventListener(new ModalInteract(configManager));
        jda.addEventListener(new ButtonInteract(configManager));

        setBotStatus();
        setBotActivity();

        jda.updateCommands().addCommands(
            Commands.slash("setup-embed", "Setup reward embed"),
            Commands.slash("setup-button", "Setup reward button"),
            Commands.slash("setup-modal", "Setup reward modal"),
            Commands.slash("setup-bot", "Setup bot activity and status"),
            Commands.slash("send-embed", "Send reward embed")
        ).queue();
    }

    public static void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    private static void setBotStatus() {
        FileConfiguration discordConfig = configManager.getDiscordConfig();

        try {
            jda.getPresence().setStatus(OnlineStatus.valueOf(discordConfig.getString("bot.status").toUpperCase()));
        } catch (Exception ignored) {}
    }

    private static void setBotActivity() {
        FileConfiguration discordConfig = configManager.getDiscordConfig();
        Activity activity;
        String activityType = discordConfig.getString("bot.activity.type");
        if (activityType == null) {
            activityType = "";
        }
        String activityMessage = "Nothing";

        if (discordConfig.getString("bot.activity.message") != null) {
            activityMessage = discordConfig.getString("bot.activity.message");
        }

        switch (activityType) {
            case "playing":
                activity = Activity.playing(activityMessage);
                break;
            case "streaming":
                activity = Activity.streaming(activityMessage, "");
                break;
            case "listening":
                activity = Activity.listening(activityMessage);
                break;
            case "watching":
                activity = Activity.watching(activityMessage);
                break;
            default:
                activity = Activity.playing(activityMessage);
                break;
        }

        try {
            jda.getPresence().setActivity(activity);
        } catch (Exception ignored) {}
    }

}
