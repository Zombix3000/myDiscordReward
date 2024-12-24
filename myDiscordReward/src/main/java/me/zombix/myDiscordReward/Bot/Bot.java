package me.zombix.myDiscordReward.Bot;

import me.zombix.myDiscordReward.Bot.Interactinos.ButtonInteract;
import me.zombix.myDiscordReward.Bot.Interactinos.ModalInteract;
import me.zombix.myDiscordReward.Bot.Interactinos.SlashCommandInteract;
import me.zombix.myDiscordReward.Bot.Interactinos.StringSelectInteract;
import me.zombix.myDiscordReward.Managers.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.bukkit.configuration.file.FileConfiguration;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends ListenerAdapter {
    private static JDA jda;

    public Bot(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(this);
        jda.addEventListener(new SlashCommandInteract());
        jda.addEventListener(new StringSelectInteract());
        jda.addEventListener(new ModalInteract());
        jda.addEventListener(new ButtonInteract());

        setBotStatus();
        setBotActivity();

        jda.updateCommands().addCommands(
                Commands.slash("setup-reward", "Setup reward...").addOptions(
                        new OptionData(OptionType.STRING, "element", "Select element what you want to configure", true)
                                .addChoices(getSetupChoices())
                )
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("send-reward-message", "Send reward message (contains: embed, button)")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        ).queue();
    }

    public static void shutdown() {
        if (jda != null) {
            jda.shutdownNow();
            try {
                jda.awaitShutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Command.Choice> getSetupChoices() {
        List<Command.Choice> choices = new ArrayList<>();

        choices.add(new Command.Choice("Embed", "embed"));
        choices.add(new Command.Choice("Button", "button"));
        choices.add(new Command.Choice("Modal", "modal"));
        choices.add(new Command.Choice("Bot", "bot"));

        return choices;
    }

    public static void setBotStatus() {
        FileConfiguration mainConfig = ConfigManager.getMainConfig();

        try {
            jda.getPresence().setStatus(OnlineStatus.valueOf(mainConfig.getString("bot.status").toUpperCase()));
        } catch (Exception ignored) {}
    }

    public static void setBotActivity() {
        FileConfiguration mainConfig = ConfigManager.getMainConfig();
        Activity activity = null;

        String activityType = mainConfig.getString("bot.activity.type");
        if (activityType == null) {
            activityType = "";
        }

        String activityMessage = mainConfig.getString("bot.activity.message");
        if (activityMessage == null) {
            activityMessage = "";
        }

        switch (activityType) {
            case "playing":
                if (activityMessage.isEmpty()) {
                    activity = Activity.playing("Nothing");
                } else {
                    activity = Activity.playing(activityMessage);
                }
                break;
            case "streaming":
                if (activityMessage.isEmpty()) {
                    activity = Activity.streaming("Nothing", "");
                } else {
                    activity = Activity.streaming(activityMessage, "");
                }
                break;
            case "listening":
                if (activityMessage.isEmpty()) {
                    activity = Activity.listening("Nothing");
                } else {
                    activity = Activity.listening(activityMessage);
                }
                break;
            case "watching":
                if (activityMessage.isEmpty()) {
                    activity = Activity.watching("Nothing");
                } else {
                    activity = Activity.watching(activityMessage);
                }
                break;
            default:
                if (!activityMessage.isEmpty()) {
                    activity = Activity.playing(activityMessage);
                }
                break;
        }

        try {
            jda.getPresence().setActivity(activity);
        } catch (Exception ignored) {}
    }

}
