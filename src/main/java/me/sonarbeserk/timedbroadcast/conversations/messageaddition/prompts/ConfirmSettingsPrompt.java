/**
 * ********************************************************************************************************************
 * TimedBroadcast - Automatically broadcasts messages to the server
 * ====================================================================================================================
 *  Copyright (C) 2012-2014 by SonarBeserk
 * http://dev.bukkit.org/bukkit-plugins/timedbroadcast/
 * *********************************************************************************************************************
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * *********************************************************************************************************************
 * Please refer to LICENSE for the full license. If it is not there, see <http://www.gnu.org/licenses/>.
 * *********************************************************************************************************************
 */

package me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

public class ConfirmSettingsPrompt extends FixedSetPrompt {
    private TimedBroadcast plugin = null;

    public ConfirmSettingsPrompt(TimedBroadcast plugin) {
        super(plugin.getLanguage().getMessage("termConfirm"));

        this.plugin = plugin;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        // add new message

        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        if(String.valueOf(conversationContext.getSessionData("level")).equalsIgnoreCase(plugin.getLanguage().getMessage("termGlobally"))) {

            return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptConfirm")
                    .replace("{message}", String.valueOf(conversationContext.getSessionData("message")))
                    .replace("{interval}", String.valueOf(conversationContext.getSessionData("interval")))
                    .replace("{unit}", String.valueOf(conversationContext.getSessionData("unit")))
                    .replace("{world}", plugin.getLanguage().getMessage("termNone"))
                    + formatFixedSet());
        } else {
            return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptConfirm")
                    .replace("{message}", String.valueOf(conversationContext.getSessionData("message")))
                    .replace("{interval}", String.valueOf(conversationContext.getSessionData("interval")))
                    .replace("{unit}", String.valueOf(conversationContext.getSessionData("unit")))
                    .replace("{world}", String.valueOf(conversationContext.getSessionData("world")))
                    + formatFixedSet());
        }
    }
}
