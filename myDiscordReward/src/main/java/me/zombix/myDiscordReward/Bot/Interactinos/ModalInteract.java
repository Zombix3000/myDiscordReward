package me.zombix.myDiscordReward.Bot.Interactinos;

import me.zombix.myDiscordReward.Actions.GiveReward;
import me.zombix.myDiscordReward.Managers.ConfigManager;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModalInteract extends ListenerAdapter {
    private static FileConfiguration mainConfig;
    private static String getReward;
    private static String mustBeOnline;
    private static String alreadyGetReward;
    private static String noPermission;

    public ModalInteract() {
        reloadValues();
    }

    public static void reloadValues() {
        FileConfiguration messagesConfig = ConfigManager.getMessagesConfig();

        mainConfig = ConfigManager.getMainConfig();
        getReward = messagesConfig.getString("successfully-get-reward");
        mustBeOnline = messagesConfig.getString("must-be-online");
        alreadyGetReward = messagesConfig.getString("already-get-reward");
        noPermission = messagesConfig.getString("no-permission-discord");
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("embedSetupTitleModal")) {
            String title = event.getValue("title").getAsString();

            mainConfig.set("embed.title", title);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set embed title to: " + title).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupDescriptionModal")) {
            String description = event.getValue("description").getAsString();

            mainConfig.set("embed.description", description);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set embed description to: " + description).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupColorModal")) {
            String color = event.getValue("color").getAsString();

            int i = 0;
            try {
                String embedColorHex = color;
                if (!embedColorHex.startsWith("#")) { embedColorHex = "#" + embedColorHex; }
                Color colorTest = embedColorHex != null ? Color.decode(embedColorHex) : null;
            } catch (Exception e) {
                i++;
            }

            if (i == 0) {
                mainConfig.set("embed.color", color);
                ConfigManager.saveMainConfig();
                event.reply("Successfully set embed color code to: " + color).setEphemeral(true).queue();
            } else {
                event.reply("Incorrect color code!").setEphemeral(true).queue();
            }
        } else if (event.getModalId().equals("embedSetupFooterModal")) {
            String footerText = event.getValue("footerText").getAsString();
            String footerLink = event.getValue("footerLink").getAsString();

            mainConfig.set("embed.footer.text", footerText);
            mainConfig.set("embed.footer.link", footerLink);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set embed footer link, and text to: " + footerText).setEphemeral(true).queue();
        } else if (event.getModalId().equals("embedSetupAuthorModal")) {
            String authorText = event.getValue("authorText").getAsString();
            String authorLink = event.getValue("authorLink").getAsString();

            mainConfig.set("embed.author.text", authorText);
            mainConfig.set("embed.author.link", authorLink);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set embed author link, and text to: " + authorText).setEphemeral(true).queue();
        } else if (event.getModalId().equals("buttonSetupLabelModal")) {
            String label = event.getValue("label").getAsString();

            mainConfig.set("button.label", label);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set button label to: " + label).setEphemeral(true).queue();
        } else if (event.getModalId().equals("buttonSetupEmojiModal")) {
            String emoji = event.getValue("emoji").getAsString();

            int i = 0;
            try {
                String buttonEmoji = emoji;
                Button button = Button.primary("test", "test").withEmoji(Emoji.fromFormatted(buttonEmoji));
                ActionRow actionRow = ActionRow.of(button);
            } catch (Exception e) {
                i++;
            }

            if (i == 0) {
                mainConfig.set("button.emoji", emoji);
                ConfigManager.saveMainConfig();
                event.reply("Successfully set button emoji to: " + emoji).setEphemeral(true).queue();
            } else {
                event.reply("Incorrect emoji!").setEphemeral(true).queue();
            }
        } else if (event.getModalId().equals("modalSetupTitleModal")) {
            String title = event.getValue("title").getAsString();

            mainConfig.set("modal.title", title);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set modal title to: " + title).setEphemeral(true).queue();
        } else if (event.getModalId().equals("modalSetupLabelModal")) {
            String label = event.getValue("label").getAsString();

            mainConfig.set("modal.label", label);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set modal label to: " + label).setEphemeral(true).queue();
        } else if (event.getModalId().equals("modalSetupPlaceholderModal")) {
            String placeholder = event.getValue("placeholder").getAsString();

            mainConfig.set("modal.placeholder", placeholder);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set modal placeholder to: " + placeholder).setEphemeral(true).queue();
        } else if (event.getModalId().equals("botSetupMessageModal")) {
            String message = event.getValue("message").getAsString();

            mainConfig.set("bot.activity.message", message);
            ConfigManager.saveMainConfig();

            event.reply("Successfully set bot activity message to: " + message).setEphemeral(true).queue();
        } else if (event.getModalId().equals("nickModal")) {
            String nick = event.getValue("nick").getAsString();
            Player player = Bukkit.getPlayerExact(nick);

            if (player != null && player.isOnline()) {
                String id = event.getUser().getId();
                String uuid = player.getUniqueId().toString();
                if (canGetReward(uuid, id)) {
                    if (player.hasPermission("mydiscordreward.getreward")) {
                        GiveReward.giveReward(nick);

                        List<List<Object>> listDiscord = (List<List<Object>>) mainConfig.getList("discord-list");
                        List<List<Object>> listMinecraft = (List<List<Object>>) mainConfig.getList("minecraft-list");

                        boolean foundInDiscord = false;
                        boolean foundInMinecraft = false;

                        if (mainConfig.getBoolean("block-discord")) {
                            if (listDiscord != null) {
                                for (List<Object> sublist : listDiscord) {
                                    if (sublist.get(0).equals(id)) {
                                        int rewardUses = (Integer) sublist.get(1);
                                        sublist.set(1, rewardUses + 1);
                                        foundInDiscord = true;
                                        break;
                                    }
                                }
                            } else {
                                listDiscord = new ArrayList<>();
                            }
                            if (!foundInDiscord) {
                                listDiscord.add(Arrays.asList(id, 1, event.getUser().getName()));
                            }
                            mainConfig.set("discord-list", listDiscord);
                        }

                        if (mainConfig.getBoolean("block-minecraft")) {
                            if (listMinecraft != null) {
                                for (List<Object> sublist : listMinecraft) {
                                    if (sublist.get(0).equals(uuid)) {
                                        int rewardUses = (Integer) sublist.get(1);
                                        sublist.set(1, rewardUses + 1);
                                        foundInMinecraft = true;
                                        break;
                                    }
                                }
                            } else {
                                listMinecraft = new ArrayList<>();
                            }
                            if (!foundInMinecraft) {
                                listMinecraft.add(Arrays.asList(uuid, 1, player.getName()));
                            }
                            mainConfig.set("minecraft-list", listMinecraft);
                        }

                        ConfigManager.saveMainConfig();

                        event.reply(getReward).setEphemeral(true).queue();
                    } else {
                        event.reply(noPermission).setEphemeral(true).queue();
                    }
                } else {
                    event.reply(alreadyGetReward).setEphemeral(true).queue();
                }
            } else {
                event.reply(mustBeOnline).setEphemeral(true).queue();
            }
        }
    }

    private boolean canGetReward(String uuid, String id) {
        List<List<Object>> listDiscord = (List<List<Object>>) mainConfig.getList("discord-list");
        List<List<Object>> listMinecraft = (List<List<Object>>) mainConfig.getList("minecraft-list");
        int maxUsage = mainConfig.getInt("max-usage");
        boolean canOrNot = true;

        canOrNot = checkLists(id, listDiscord, maxUsage, canOrNot);
        canOrNot = checkLists(uuid, listMinecraft, maxUsage, canOrNot);

        return canOrNot;
    }

    private boolean checkLists(String ID, List<List<Object>> list, int maxUsage, boolean canOrNot) {
        if (list != null) {
            for (List<Object> sublist : list) {
                if (sublist.get(0).equals(ID)) {
                    int rewardUses = (Integer) sublist.get(1);
                    if (rewardUses >= maxUsage) {
                        canOrNot = false;
                    }
                    break;
                }
            }
        }
        return canOrNot;
    }

}
