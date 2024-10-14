package me.zombix.myDiscordReward.Bot.Interactinos;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;

import static org.bukkit.Bukkit.getLogger;

public class SlashCommandInteract extends ListenerAdapter {
    public SlashCommandInteract() {}

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup-reward")) {
            String setupElement = event.getOption("element").getAsString();
            StringSelectMenu.Builder builder;
            StringSelectMenu menu;

            switch (setupElement) {
                case "embed":
                    builder = StringSelectMenu.create("setupEmbedMenu");
                    builder.addOption("Title", "title");
                    builder.addOption("Description", "description");
                    builder.addOption("Color", "color");
                    builder.addOption("Footer", "footer");
                    builder.addOption("Author", "author");
                    menu = builder.build();

                    event.reply("Embed Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
                    break;
                case "button":
                    builder = StringSelectMenu.create("setupButtonMenu");
                    builder.addOption("Label", "label");
                    builder.addOption("Style", "style");
                    builder.addOption("Emoji", "emoji");
                    menu = builder.build();

                    event.reply("Button Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
                case "modal":
                    builder = StringSelectMenu.create("setupModalMenu");
                    builder.addOption("Title", "title");
                    builder.addOption("Label", "label");
                    builder.addOption("Placeholder", "placeholder");
                    menu = builder.build();

                    event.reply("Modal Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
                case "bot":
                    builder = StringSelectMenu.create("setupBotMenu");
                    builder.addOption("Activity", "activity");
                    builder.addOption("Status", "status");
                    menu = builder.build();

                    event.reply("Bot Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
            }
        } else if (event.getName().equals("send-reward-message")) {
            FileConfiguration mainConfig = ConfigManager.getMainConfig();

            String embedTitle = mainConfig.getString("embed.title");
            String embedDescription = mainConfig.getString("embed.description");
            String embedColorHex = mainConfig.getString("embed.color");
            if (embedColorHex != null) { if (!embedColorHex.startsWith("#")) { embedColorHex = "#" + embedColorHex; } }
            Color color = embedColorHex != null ? Color.decode(embedColorHex) : null;
            String embedFooterText = mainConfig.getString("embed.footer.text");
            String embedFooterLink = mainConfig.getString("embed.footer.link");
            String embedAuthorText = mainConfig.getString("embed.author.text");
            String embedAuthorLink = mainConfig.getString("embed.author.link");
            String buttonLabel = mainConfig.getString("button.label");
            String buttonStyle = mainConfig.getString("button.style");
            String buttonEmoji = mainConfig.getString("button.emoji");

            EmbedBuilder embed = new EmbedBuilder();

            if (embedTitle != null && !embedTitle.isEmpty()) {
                embed.setTitle(embedTitle);
            }
            if (embedDescription != null && !embedDescription.isEmpty()) {
                embed.setDescription(embedDescription);
            }
            if (color != null) {
                embed.setColor(color);
            }
            if (embedAuthorText != null && !embedAuthorText.isEmpty()) {
                if (embedAuthorLink != null && !embedAuthorLink.isEmpty()) {
                    try {
                        embed.setAuthor(embedAuthorText, null, embedAuthorLink);
                    } catch (Exception e) {
                        embed.setAuthor(embedAuthorText, null, null);
                        getLogger().severe("The link of icon in author is incorrect.");
                    }
                } else {
                    embed.setAuthor(embedAuthorText, null, null);
                }
            }
            if (embedFooterText != null && !embedFooterText.isEmpty()) {
                if (embedFooterLink != null && !embedFooterLink.isEmpty()) {
                    try {
                        embed.setFooter(embedFooterText, embedFooterLink);
                    } catch (Exception e) {
                        embed.setFooter(embedFooterText, null);
                        getLogger().severe("The link of icon in footer is incorrect.");
                    }
                } else {
                    embed.setFooter(embedFooterText, null);
                }
            }

            Button button = null;
            if (buttonLabel != null && !buttonLabel.isEmpty()) {
                if (buttonStyle.equalsIgnoreCase("primary")) {
                    button = Button.primary("getReward", buttonLabel);
                } else if (buttonStyle.equalsIgnoreCase("secondary")) {
                    button = Button.secondary("getReward", buttonLabel);
                } else if (buttonStyle.equalsIgnoreCase("success")) {
                    button = Button.success("getReward", buttonLabel);
                } else if (buttonStyle.equalsIgnoreCase("danger")) {
                    button = Button.danger("getReward", buttonLabel);
                } else {
                    button = Button.secondary("getReward", buttonLabel);
                }

                if (buttonEmoji != null && !buttonEmoji.isEmpty()) {
                    button = button.withEmoji(Emoji.fromFormatted(buttonEmoji));
                }
            }

            try {
                embed.build();
            } catch (Exception e) {
                if (e.getMessage().contains("Cannot build an empty embed!")) {
                    embed.setDescription("Embed is empty.");
                }
            }

            if (button != null) {
                event.getChannel().sendMessageEmbeds(embed.build())
                        .addActionRow(button)
                        .queue();
            } else {
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }

            event.reply("Successfully sent message.").setEphemeral(true).queue();
        }
    }

}
