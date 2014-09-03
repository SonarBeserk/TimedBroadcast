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

package me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;


public class ChooseTimeUnitPrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    public ChooseTimeUnitPrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptTimeUnit")
                        .replace("{termSecond}", plugin.getLanguage().getMessage("termSecond"))
                        .replace("{termMinute}", plugin.getLanguage().getMessage("termMinute"))
        );
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termSecond")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termSecond").toCharArray()[0]))) {
            conversationContext.setSessionData("unit", plugin.getLanguage().getMessage("termSecond"));

            if (Boolean.parseBoolean(String.valueOf(conversationContext.getSessionData("correctionMode")))) {
                return new DetailsCorrectPrompt(plugin);
            }

            return new InputIntervalPrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termMinute")) || s.equalsIgnoreCase(String.valueOf(plugin.getLanguage().getMessage("termMinute").toCharArray()[0]))) {
            conversationContext.setSessionData("unit", plugin.getLanguage().getMessage("termMinute"));

            if (Boolean.parseBoolean(String.valueOf(conversationContext.getSessionData("correctionMode")))) {
                return new DetailsCorrectPrompt(plugin);
            }

            return new InputIntervalPrompt(plugin);
        }

        return this;
    }
}
