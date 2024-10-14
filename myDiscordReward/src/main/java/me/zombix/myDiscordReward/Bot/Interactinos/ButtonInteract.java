package me.zombix.myDiscordReward.Bot.Interactinos;

import me.zombix.myDiscordReward.Managers.ConfigManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.configuration.file.FileConfiguration;

public class ButtonInteract extends ListenerAdapter {
    public ButtonInteract() {}

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
            FileConfiguration mainConfig = ConfigManager.getMainConfig();
            String title;
            String label;
            String placeholder;

            if (mainConfig.getString("modal.title") != null) {
                title = mainConfig.getString("modal.title");
            } else {
                title = "Enter your minecraft nick";
            }
            if (mainConfig.getString("modal.label") != null) {
                label = mainConfig.getString("modal.label");
            } else {
                label = "Your nick";
            }
            if (mainConfig.getString("modal.placeholder") != null) {
                placeholder = mainConfig.getString("modal.placeholder");
            } else {
                placeholder = "Enter the nick";
            }

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
