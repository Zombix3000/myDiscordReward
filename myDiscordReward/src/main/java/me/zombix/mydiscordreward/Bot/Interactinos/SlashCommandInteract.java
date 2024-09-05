package me.zombix.mydiscordreward.Bot.Interactinos;

import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.Color;

import static org.bukkit.Bukkit.getLogger;

public class SlashCommandInteract extends ListenerAdapter {
    private ConfigManager configManager;

    public SlashCommandInteract(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup-embed")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("setupEmbedMenu");
            builder.addOption("Title", "title");
            builder.addOption("Description", "description");
            builder.addOption("Color", "color");
            builder.addOption("Footer", "footer");
            builder.addOption("Author", "author");
            StringSelectMenu menu = builder.build();

            event.reply("Embed Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
        } else if (event.getName().equals("setup-button")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("setupButtonMenu");
            builder.addOption("Label", "label");
            builder.addOption("Style", "style");
            builder.addOption("Emoji", "emoji");
            StringSelectMenu menu = builder.build();

            event.reply("Button Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
        } else if (event.getName().equals("setup-modal")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("setupModalMenu");
            builder.addOption("Title", "title");
            builder.addOption("Label", "label");
            builder.addOption("Placeholder", "placeholder");
            StringSelectMenu menu = builder.build();

            event.reply("Modal Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
        } else if (event.getName().equals("setup-bot")) {
            StringSelectMenu.Builder builder = StringSelectMenu.create("setupBotMenu");
            builder.addOption("Activity", "activity");
            builder.addOption("Status", "status");
            StringSelectMenu menu = builder.build();

            event.reply("Bot Setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
        } else if (event.getName().equals("send-embed")) {
            FileConfiguration discordConfig = configManager.getDiscordConfig();

            String embedTitle = discordConfig.getString("embed.title");
            String embedDescription = discordConfig.getString("embed.description");
            String embedColorHex = discordConfig.getString("embed.color");
            if (embedColorHex != null) { if (!embedColorHex.startsWith("#")) { embedColorHex = "#" + embedColorHex; } }
            Color color = embedColorHex != null ? Color.decode(embedColorHex) : null;
            String embedFooterText = discordConfig.getString("embed.footer.text");
            String embedFooterLink = discordConfig.getString("embed.footer.link");
            String embedAuthorText = discordConfig.getString("embed.author.text");
            String embedAuthorLink = discordConfig.getString("embed.author.link");
            String buttonLabel = discordConfig.getString("button.label");
            String buttonStyle = discordConfig.getString("button.style");
            String buttonEmoji = discordConfig.getString("button.emoji");

            EmbedBuilder embed = new EmbedBuilder();

            if (embedTitle != null) {
                embed.setTitle(embedTitle);
            }
            if (embedDescription != null) {
                embed.setDescription(embedDescription);
            }
            if (color != null) {
                embed.setColor(color);
            }
            if (embedAuthorText != null) {
                if (embedAuthorLink != null) {
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
            if (embedFooterText != null) {
                if (embedFooterLink != null) {
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
            if (buttonLabel != null) {
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

                if (buttonEmoji != null) {
                    button = button.withEmoji(Emoji.fromFormatted(buttonEmoji));
                }
            }

            if (button != null) {
                event.getChannel().sendMessageEmbeds(embed.build())
                        .addActionRow(button)
                        .queue();
            } else {
                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }

            event.reply("Successfully sent message").setEphemeral(true).queue();
        }
    }

}
