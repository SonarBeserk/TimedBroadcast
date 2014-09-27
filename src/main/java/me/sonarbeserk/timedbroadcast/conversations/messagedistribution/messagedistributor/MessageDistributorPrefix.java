package me.sonarbeserk.timedbroadcast.conversations.messagedistribution.messagedistributor;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class MessageDistributorPrefix implements ConversationPrefix {
    private TimedBroadcast plugin = null;

    public MessageDistributorPrefix(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("messageDistributorPrefix"));
    }
}
