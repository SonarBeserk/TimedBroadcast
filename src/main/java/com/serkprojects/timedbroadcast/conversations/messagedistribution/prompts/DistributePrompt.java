/**
 * *********************************************************************************************************************
 * TimedBroadcast - Automatically broadcasts messages to the server
 * =====================================================================================================================
 *  Copyright (C) 2012-2015 by SonarBeserk
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

package com.serkprojects.timedbroadcast.conversations.messagedistribution.prompts;

import com.serkprojects.timedbroadcast.TimedBroadcast;
import com.serkprojects.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class DistributePrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    public DistributePrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Message message = plugin.getMessages().get(Integer.valueOf(String.valueOf(conversationContext.getSessionData("messageID"))) - 1);

        String timeUnit = null;

        switch (message.getUnit()) {
            case SECOND:
                timeUnit = plugin.getLanguage().getMessage("termSecond");
                break;
            case MINUTE:
                timeUnit = plugin.getLanguage().getMessage("termMinute");
                break;
        }

        return ChatColor.translateAlternateColorCodes('&', replaceRestrictions(plugin.getLanguage().getMessage("promptDetailsDistribution"), conversationContext)
                        .replace("{message}", message.getMessage())
                        .replace("{interval}", String.valueOf(message.getInterval()))
                        .replace("{unit}", timeUnit)
                        .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                        .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
        );
    }

    private String replaceRestrictions(String input, ConversationContext context) {
        String newString = input;

        if (context.getSessionData("world") == null) {
            newString = newString.replace("{world}", plugin.getLanguage().getMessage("termNo"));
        } else {
            newString = newString.replace("{world", String.valueOf(context.getSessionData("world")));
        }

        if (context.getSessionData("group") == null) {
            newString = newString.replace("{group}", plugin.getLanguage().getMessage("termNo"));
        } else {
            newString = newString.replace("{group}", String.valueOf(context.getSessionData("group")));
        }

        return newString;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termNo")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termNo").toCharArray()[0]))) {
            return Prompt.END_OF_CONVERSATION;
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termYes")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termYes").toCharArray()[0]))) {
            plugin.getLilyPadMessenger().distributeMessage(plugin.getMessages().get(Integer.valueOf(String.valueOf(conversationContext.getSessionData("messageID"))) - 1));
        }

        return this;
    }
}
