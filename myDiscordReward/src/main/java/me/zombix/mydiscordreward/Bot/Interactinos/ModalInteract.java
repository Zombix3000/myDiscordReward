package me.zombix.mydiscordreward.Bot.Interactinos;

import me.zombix.mydiscordreward.Actions.GiveReward;
import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;

public class ModalInteract extends ListenerAdapter {
    private ConfigManager configManager;
    private String getReward;
    private String mustBeOnline;
    private String alreadyGetReward;
    private String noPermission;

    public ModalInteract(ConfigManager configManager) {
        FileConfiguration messagesConfig = configManager.getMessagesConfig();

        this.configManager = configManager;
        this.getReward = messagesConfig.getString("successfully-get-reward");
        this.mustBeOnline = messagesConfig.getString("must-be-online");
        this.alreadyGetReward = messagesConfig.getString("already-get-reward");
        this.noPermission = messagesConfig.getString("no-permission");
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("embedSetupTitleModal")) {
            String title = event.getValue("title").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("embed.title", title);
            configManager.saveDiscordConfig();

            event.reply("Successfully set embed title to: " + title).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupDescriptionModal")) {
            String description = event.getValue("description").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("embed.description", description);
            configManager.saveDiscordConfig();

            event.reply("Successfully set embed description to: " + description).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupColorModal")) {
            String color = event.getValue("color").getAsString();
            FileConfiguration discordConfig = configManager.getDiscordConfig();

            int i = 0;
            try {
                String embedColorHex = color;
                if (!embedColorHex.startsWith("#")) { embedColorHex = "#" + embedColorHex; }
                Color colorTest = embedColorHex != null ? Color.decode(embedColorHex) : null;
            } catch (Exception e) {
                i++;
            }

            if (i == 0) {
                discordConfig.set("embed.color", color);
                configManager.saveDiscordConfig();
                event.reply("Successfully set embed color code to: " + color).setEphemeral(true).queue();
            } else {
                event.reply("Incorrect color code!").setEphemeral(true).queue();
            }
        } else if (event.getModalId().equals("embedSetupFooterModal")) {
            String footerText = event.getValue("footerText").getAsString();
            String footerLink = event.getValue("footerLink").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("embed.footer.text", footerText);
            discordConfig.set("embed.footer.link", footerLink);
            configManager.saveDiscordConfig();

            event.reply("Successfully set embed footer link, and text to: " + footerText).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupAuthorModal")) {
            String authorText = event.getValue("authorText").getAsString();
            String authorLink = event.getValue("authorLink").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("embed.author.text", authorText);
            discordConfig.set("embed.author.link", authorLink);
            configManager.saveDiscordConfig();

            event.reply("Successfully set embed author link, and text to: " + authorText).setEphemeral(true).queue();
        } else if (event.getModalId().equals("buttonSetupLabelModal")) {
            String label = event.getValue("label").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("button.label", label);
            configManager.saveDiscordConfig();

            event.reply("Successfully set button label to: " + label).setEphemeral(true).queue();
        } else if (event.getModalId().equals("buttonSetupEmojiModal")) {
            String emoji = event.getValue("emoji").getAsString();
            FileConfiguration discordConfig = configManager.getDiscordConfig();

            int i = 0;
            try {
                String buttonEmoji = emoji;
                Button button = Button.primary("test", "test").withEmoji(Emoji.fromFormatted(buttonEmoji));
                ActionRow actionRow = ActionRow.of(button);
            } catch (Exception e) {
                i++;
            }

            if (i == 0) {
                discordConfig.set("button.emoji", emoji);
                configManager.saveDiscordConfig();
                event.reply("Successfully set button emoji to: " + emoji).setEphemeral(true).queue();
            } else {
                event.reply("Incorrect emoji!").setEphemeral(true).queue();
            }
        } else if (event.getModalId().equals("modalSetupTitleModal")) {
            String title = event.getValue("title").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("modal.title", title);
            configManager.saveDiscordConfig();

            event.reply("Successfully set modal title to: " + title).setEphemeral(true).queue();
        } else if (event.getModalId().equals("modalSetupLabelModal")) {
            String label = event.getValue("label").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("modal.label", label);
            configManager.saveDiscordConfig();

            event.reply("Successfully set modal label to: " + label).setEphemeral(true).queue();
        } else if (event.getModalId().equals("modalSetupPlaceholderModal")) {
            String placeholder = event.getValue("placeholder").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("modal.placeholder", placeholder);
            configManager.saveDiscordConfig();

            event.reply("Successfully set modal placeholder to: " + placeholder).setEphemeral(true).queue();
        } else if (event.getModalId().equals("botSetupMessageModal")) {
            String message = event.getValue("message").getAsString();

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("bot.activity.message", message);
            configManager.saveDiscordConfig();

            event.reply("Successfully set bot activity message to: " + message).setEphemeral(true).queue();
        } else if (event.getModalId().equals("nickModal")) {
            String nick = event.getValue("nick").getAsString();

            if (configManager.canGetReward(nick, event.getUser().getId())) {
                Player player = Bukkit.getPlayerExact(nick);

                if (player != null && player.isOnline()) {
                    if (player.hasPermission("mydiscordreward.getreward")) {
                        GiveReward.giveReward(nick);

                        FileConfiguration usersConfig = configManager.getUsersConfig();

                        List<String> usersList = usersConfig.getStringList("users");

                        usersList.add(nick);
                        usersList.add(event.getUser().getId());

                        usersConfig.set("users", usersList);

                        configManager.saveUsersConfig();

                        event.reply(getReward).setEphemeral(true).queue();
                    } else {
                        event.reply(noPermission).setEphemeral(true).queue();
                    }
                } else {
                    event.reply(mustBeOnline).setEphemeral(true).queue();
                }
            } else {
                event.reply(alreadyGetReward).setEphemeral(true).queue();
            }
        }
    }

}
