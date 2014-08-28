/**
 * *********************************************************************************************************************
 * TimedBroadcast - Automatically broadcasts messages to the server
 * =====================================================================================================================
 *  Copyright (C) 2012-2014 by SonarBeserk
 * http://dev.bukkit.org/bukkit-plugins/timedbroadcast/
 * *********************************************************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * *********************************************************************************************************************
 * Please refer to LICENSE for the full license. If it is not there, see <http://www.gnu.org/licenses/>.
 * *********************************************************************************************************************
 */

package me.sonarbeserk.timedbroadcast.conversations.messageaddition.messagebuilder;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

public class MessageBuilderAbandonedListener implements ConversationAbandonedListener {
    private TimedBroadcast plugin = null;

    public MessageBuilderAbandonedListener(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent) {
        String prefix = plugin.getLanguage().getMessage("messageBuilderPrefix");

        if (conversationAbandonedEvent.gracefulExit()) {
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getLanguage().getMessage("messageAdded")));

            String message = String.valueOf(conversationAbandonedEvent.getContext().getSessionData("message"));

            TimeUnit timeUnit = null;

            if (String.valueOf(conversationAbandonedEvent.getContext().getSessionData("unit")).equalsIgnoreCase(plugin.getLanguage().getMessage("termSecond"))) {
                timeUnit = TimeUnit.SECOND;
            } else if (String.valueOf(conversationAbandonedEvent.getContext().getSessionData("unit")).equalsIgnoreCase(plugin.getLanguage().getMessage("termMinute"))) {
                timeUnit = TimeUnit.MINUTE;
            }

            int interval = Integer.parseInt(String.valueOf(conversationAbandonedEvent.getContext().getSessionData("interval")));

            MessageLocation location = null;

            if (String.valueOf(conversationAbandonedEvent.getContext().getSessionData("where")).equalsIgnoreCase(plugin.getLanguage().getMessage("termGlobally"))) {
                location = MessageLocation.GLOBALLY;
            } else if (String.valueOf(conversationAbandonedEvent.getContext().getSessionData("where")).equalsIgnoreCase(plugin.getLanguage().getMessage("termWorld"))) {
                location = MessageLocation.WORLD;
            }

            String worldName = null;

            if (conversationAbandonedEvent.getContext().getSessionData("world") != null) {
                worldName = String.valueOf(conversationAbandonedEvent.getContext().getSessionData("world"));
            }

            Message messageWrapper = new Message(message, timeUnit, interval, location, worldName);

            plugin.addMessage(messageWrapper);
        } else {
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getLanguage().getMessage("messageAdditionCancelled")));
        }
    }
}
