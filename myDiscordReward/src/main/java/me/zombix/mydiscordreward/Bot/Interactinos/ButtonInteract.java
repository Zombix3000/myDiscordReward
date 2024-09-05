package me.zombix.mydiscordreward.Bot.Interactinos;

import me.zombix.mydiscordreward.Config.ConfigManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.configuration.file.FileConfiguration;

public class ButtonInteract extends ListenerAdapter {
    private ConfigManager configManager;
    private String title;
    private String label;
    private String placeholder;

    public ButtonInteract(ConfigManager configManager) {
        FileConfiguration discordConfig = configManager.getDiscordConfig();

        this.configManager = configManager;
        if (discordConfig.getString("modal.title") != null) {
            this.title = discordConfig.getString("modal.title");
        } else {
            this.title = "Enter your minecraft nick";
        }
        if (discordConfig.getString("modal.label") != null) {
            this.label = discordConfig.getString("modal.label");
        } else {
            this.label = "Your nick";
        }
        if (discordConfig.getString("modal.placeholder") != null) {
            this.placeholder = discordConfig.getString("modal.placeholder");
        } else {
            this.placeholder = "Enter the nick";
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("colorSetButton")) {
            TextInput titleInput = TextInput.create("color", "Color code", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("Enter the title text")
                    .setMinLength(1)
                    .setMaxLength(10)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("embedSetupColorModal", "Enter color code").addActionRows(ActionRow.of(titleInput)).build();

            event.replyModal(modal).queue();
        } else if (event.getButton().getId().equals("getReward")) {
            TextInput nickInput = TextInput.create("nick", label, TextInputStyle.PARAGRAPH)
                    .setPlaceholder(placeholder)
                    .setMinLength(1)
                    .setMaxLength(30)
                    .setRequired(true)
                    .build();

            Modal modal = Modal.create("nickModal", title).addActionRows(ActionRow.of(nickInput)).build();

            event.replyModal(modal).queue();
        }
    }

}
