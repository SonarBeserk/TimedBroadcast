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

package me.sonarbeserk.timedbroadcast.conversations.messageremoval.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class RemoveMessagePrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    public RemoveMessagePrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Message message = plugin.getMessages().get(Integer.valueOf(String.valueOf(conversationContext.getSessionData("messageID"))) - 1);

        if (message.getLocation() == MessageLocation.GLOBALLY) {
            if (message.getUnit() == TimeUnit.SECOND) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptRemoveMessage")
                                .replace("{message}", message.getMessage())
                                .replace("{interval}", String.valueOf(message.getInterval()))
                                .replace("{unit}", plugin.getLanguage().getMessage("termSecond"))
                                .replace("{world}", plugin.getLanguage().getMessage("termNone"))
                                .replace("{termBack}", plugin.getLanguage().getMessage("termBack"))
                                .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                                .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
                );
            } else if (message.getUnit() == TimeUnit.MINUTE) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptRemoveMessage")
                                .replace("{message}", message.getMessage())
                                .replace("{interval}", String.valueOf(message.getInterval()))
                                .replace("{unit}", plugin.getLanguage().getMessage("termMinute"))
                                .replace("{world}", plugin.getLanguage().getMessage("termNone"))
                                .replace("{termBack}", plugin.getLanguage().getMessage("termBack"))
                                .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                                .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
                );
            }
        } else {
            if (message.getUnit() == TimeUnit.SECOND) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptRemoveMessage")
                                .replace("{message}", message.getMessage())
                                .replace("{interval}", String.valueOf(message.getInterval()))
                                .replace("{unit}", plugin.getLanguage().getMessage("termSecond"))
                                .replace("{world}", message.getWorldName())
                                .replace("{termBack}", plugin.getLanguage().getMessage("termBack"))
                                .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                                .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
                );
            } else if (message.getUnit() == TimeUnit.MINUTE) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptRemoveMessage")
                                .replace("{message}", message.getMessage())
                                .replace("{interval}", String.valueOf(message.getInterval()))
                                .replace("{unit}", plugin.getLanguage().getMessage("termMinute"))
                                .replace("{world}", message.getWorldName())
                                .replace("{termBack}", plugin.getLanguage().getMessage("termBack"))
                                .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                                .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
                );
            }
        }

        return null;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termYes")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termYes").toCharArray()[0]))) {
            conversationContext.setSessionData("message", plugin.getMessages().get(Integer.valueOf(String.valueOf(conversationContext.getSessionData("messageID"))) - 1));
            return Prompt.END_OF_CONVERSATION;
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termNo")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termNo").toCharArray()[0]))) {
            return new ChooseMessagePrompt(plugin);
        }

        return this;
    }
}
