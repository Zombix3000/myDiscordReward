package me.zombix.mydiscordreward.Bot.Interactinos;

import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.configuration.file.FileConfiguration;

import static org.bukkit.Bukkit.getLogger;

public class StringSelectInteract extends ListenerAdapter {
    private ConfigManager configManager;

    public StringSelectInteract(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("setupEmbedMenu")) {
            String selected = event.getValues().get(0);

            if (selected.equals("title")) {
                TextInput titleInput = TextInput.create("title", "Title", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the title text")
                        .setMinLength(1)
                        .setMaxLength(256)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("embedSetupTitleModal", "Enter title").addActionRows(ActionRow.of(titleInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("description")) {
                TextInput descriptionInput = TextInput.create("description", "Description", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the description text")
                        .setMinLength(1)
                        .setMaxLength(4000)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("embedSetupDescriptionModal", "Enter description").addActionRows(ActionRow.of(descriptionInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("color")) {
                /*StringSelectMenu.Builder builder = StringSelectMenu.create("setupEmbedColor");
                builder.addOption("Title", "title");
                builder.addOption("Description", "description");
                builder.addOption("Color", "color");
                builder.addOption("Footer", "footer");
                builder.addOption("Author", "author");
                StringSelectMenu menu = builder.build();*/

                Button button = Button.secondary("colorSetButton", "Set color code");

                event.reply("Color setup").setEphemeral(true).addComponents(/*ActionRow.of(menu), */ActionRow.of(button)).queue();
            } else if (selected.equals("footer")) {
                TextInput footerTextInput = TextInput.create("footerText", "Footer - Text", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the footer text")
                        .setMinLength(1)
                        .setMaxLength(2048)
                        .setRequired(true)
                        .build();

                TextInput footerLinkInput = TextInput.create("footerLink", "Footer - Image Link", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the footer image link")
                        .setMinLength(1)
                        .setMaxLength(4000)
                        .setRequired(false)
                        .build();

                Modal modal = Modal.create("embedSetupFooterModal", "Enter footer").addActionRows(ActionRow.of(footerTextInput), ActionRow.of(footerLinkInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("author")) {
                TextInput authorTextInput = TextInput.create("authorText", "Author - Text", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the author text")
                        .setMinLength(1)
                        .setMaxLength(256)
                        .setRequired(true)
                        .build();

                TextInput authorLinkInput = TextInput.create("authorLink", "Author - Image Link", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the author image link")
                        .setMinLength(1)
                        .setMaxLength(4000)
                        .setRequired(false)
                        .build();

                Modal modal = Modal.create("embedSetupAuthorModal", "Enter author").addActionRows(ActionRow.of(authorTextInput), ActionRow.of(authorLinkInput)).build();

                event.replyModal(modal).queue();
            }
        } else if (event.getComponentId().equals("setupButtonMenu")) {
            String selected = event.getValues().get(0);

            if (selected.equals("label")) {
                TextInput titleInput = TextInput.create("label", "Label", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the label text")
                        .setMinLength(1)
                        .setMaxLength(80)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("buttonSetupLabelModal", "Enter label").addActionRows(ActionRow.of(titleInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("style")) {
                StringSelectMenu.Builder builder = StringSelectMenu.create("setupButtonStyle");
                builder.addOption("Primary", "primary");
                builder.addOption("Secondary", "secondary");
                builder.addOption("Danger", "danger");
                builder.addOption("Success", "success");
                StringSelectMenu menu = builder.build();

                event.reply("Style setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
            } else if (selected.equals("emoji")) {
                TextInput descriptionInput = TextInput.create("emoji", "Emoji", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the emoji")
                        .setMinLength(1)
                        .setMaxLength(2)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("buttonSetupEmojiModal", "Enter description").addActionRows(ActionRow.of(descriptionInput)).build();

                event.replyModal(modal).queue();
            }
        } else if (event.getComponentId().equals("setupButtonStyle")) {
            String selected = event.getValues().get(0);

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("button.style", selected);
            configManager.saveDiscordConfig();

            event.reply("Successfully set button style to: " + selected).setEphemeral(true).queue();
        } else if (event.getComponentId().equals("setupModalMenu")) {
            String selected = event.getValues().get(0);

            if (selected.equals("title")) {
                TextInput titleInput = TextInput.create("title", "Title", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the title text")
                        .setMinLength(1)
                        .setMaxLength(45)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("modalSetupTitleModal", "Enter title").addActionRows(ActionRow.of(titleInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("label")) {
                TextInput labelInput = TextInput.create("label", "Label", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the label text")
                        .setMinLength(1)
                        .setMaxLength(45)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("modalSetupLabelModal", "Enter label").addActionRows(ActionRow.of(labelInput)).build();

                event.replyModal(modal).queue();
            } else if (selected.equals("placeholder")) {
                TextInput placeholderInput = TextInput.create("placeholder", "Placeholder", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the placeholder text")
                        .setMinLength(1)
                        .setMaxLength(100)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("modalSetupPlaceholderModal", "Enter placeholder").addActionRows(ActionRow.of(placeholderInput)).build();

                event.replyModal(modal).queue();
            }
        } else if (event.getComponentId().equals("setupBotMenu")) {
            String selected = event.getValues().get(0);

            if (selected.equals("activity")) {
                StringSelectMenu.Builder builder = StringSelectMenu.create("setupBotActivity");
                builder.addOption("Activity Type", "activityType");
                builder.addOption("Activity Message", "activityMessage");
                StringSelectMenu menu = builder.build();

                event.reply("Activity setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
            } else if (selected.equals("status")) {
                StringSelectMenu.Builder builder = StringSelectMenu.create("setupBotStatus");
                builder.addOption("Online", "online");
                builder.addOption("Idle", "idle");
                builder.addOption("Do Not Disturb", "do not disturb");
                builder.addOption("Invisible", "invisible");
                builder.addOption("Offline", "offline");
                StringSelectMenu menu = builder.build();

                event.reply("Status setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
            }
        } else if (event.getComponentId().equals("setupBotStatus")) {
            String selected = event.getValues().get(0);

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("bot.status", selected);
            configManager.saveDiscordConfig();

            event.reply("Successfully set bot status to: " + selected).setEphemeral(true).queue();
        } else if (event.getComponentId().equals("setupBotActivity")) {
            String selected = event.getValues().get(0);

            if (selected.equals("activityType")) {
                StringSelectMenu.Builder builder = StringSelectMenu.create("setupBotActivityType");
                builder.addOption("Playing", "playing");
                builder.addOption("Streaming", "streaming");
                builder.addOption("Listening", "listening");
                builder.addOption("Watching", "watching");
                StringSelectMenu menu = builder.build();

                event.reply("Activity Type setup").setEphemeral(true).addComponents(ActionRow.of(menu)).queue();
            } else if (selected.equals("activityMessage")) {
                TextInput messageInput = TextInput.create("message", "Message", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Enter the message text")
                        .setMinLength(1)
                        .setMaxLength(128)
                        .setRequired(true)
                        .build();

                Modal modal = Modal.create("botSetupMessageModal", "Enter message").addActionRows(ActionRow.of(messageInput)).build();

                event.replyModal(modal).queue();
            }
        } else if (event.getComponentId().equals("setupBotActivityType")) {
            String selected = event.getValues().get(0);

            FileConfiguration discordConfig = configManager.getDiscordConfig();
            discordConfig.set("bot.activity.type", selected);
            configManager.saveDiscordConfig();

            event.reply("Successfully set bot activity type to: " + selected).setEphemeral(true).queue();
        }
    }

}
