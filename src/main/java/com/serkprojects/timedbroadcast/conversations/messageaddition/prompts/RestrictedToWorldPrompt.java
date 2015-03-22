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

package com.serkprojects.timedbroadcast.conversations.messageaddition.prompts;

import com.serkprojects.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class RestrictedToWorldPrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    public RestrictedToWorldPrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptWorldRestricted")
                        .replace("{termYes}", plugin.getLanguage().getMessage("termYes"))
                        .replace("{termNo}", plugin.getLanguage().getMessage("termNo"))
        );
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termYes")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termYes").toCharArray()[0]))) {
            return new ChooseWorldPrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termNo")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termNo").toCharArray()[0]))) {
            if (plugin.getPermissions() != null) {
                return new RestrictedToGroupPrompt(plugin);
            } else {
                return new DetailsCorrectPrompt(plugin);
            }
        }

        return this;
    }
}
