package me.sonarbeserk.timedbroadcast.conversations.messagedistribution.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class DistributeMessageStartPrompt extends MessagePrompt {
    private TimedBroadcast plugin = null;

    public DistributeMessageStartPrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptMessageDistributeStart")
                .replace("{timeout}", plugin.getConfig().getString("settings.timeout.messageDistribute"))
                .replace("{termExit}", plugin.getLanguage().getMessage("termExit")));
    }

    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        return new ChooseMessagePrompt(plugin);
    }
}
