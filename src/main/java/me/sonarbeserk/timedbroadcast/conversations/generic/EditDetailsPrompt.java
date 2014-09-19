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

package me.sonarbeserk.timedbroadcast.conversations.generic;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts.ChooseTimeUnitPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts.InputIntervalPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts.InputMessagePrompt;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts.RestrictedToWorldPrompt;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class EditDetailsPrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    public EditDetailsPrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptDetailsCorrection")
                        .replace("{termMessage}", plugin.getLanguage().getMessage("termMessage"))
                        .replace("{termInterval}", plugin.getLanguage().getMessage("termInterval"))
                        .replace("{termUnit}", plugin.getLanguage().getMessage("termUnit"))
                        .replace("{termRestrictions}", plugin.getLanguage().getMessage("termRestrictions"))
        );
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termMessage")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termMessage").toCharArray()[0]))) {
            conversationContext.setSessionData("correctionMode", "true");
            return new InputMessagePrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termInterval")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termInterval").toCharArray()[0]))) {
            conversationContext.setSessionData("correctionMode", "true");
            return new InputIntervalPrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termUnit")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termUnit").toCharArray()[0]))) {
            conversationContext.setSessionData("correctionMode", "true");
            return new ChooseTimeUnitPrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termRestrictions")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termRestrictions").toCharArray()[0]))) {
            conversationContext.setSessionData("correctionMode", "true");
            return new RestrictedToWorldPrompt(plugin);
        }

        return this;
    }
}
