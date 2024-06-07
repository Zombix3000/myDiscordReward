package me.zombix.mydiscordreward.Bot;

import me.zombix.mydiscordreward.Actions.GiveReward;
import me.zombix.mydiscordreward.Bot.Interactinos.ButtonInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.ModalInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.SlashCommandInteract;
import me.zombix.mydiscordreward.Bot.Interactinos.StringSelectInteract;
import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    private JDA jda;
    private final ConfigManager configManager;
    private final GiveReward giveReward;

    public Bot(String token, ConfigManager configManager, GiveReward giveReward) throws LoginException {
        this.configManager = configManager;
        this.giveReward = giveReward;
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(this);
        jda.addEventListener(new SlashCommandInteract(configManager));
        jda.addEventListener(new StringSelectInteract(configManager));
        jda.addEventListener(new ModalInteract(configManager, giveReward));
        jda.addEventListener(new ButtonInteract(configManager));

        jda.updateCommands().addCommands(
            Commands.slash("setup-embed", "Setup reward embed"),
            Commands.slash("setup-button", "Setup reward button"),
            Commands.slash("setup-modal", "Setup reward modal"),
            Commands.slash("send-embed", "Send reward embed")
        ).queue();
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }

}
