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

public class ChooseMessageLocationPrompt extends FixedSetPrompt {
    private TimedBroadcast plugin = null;

    public ChooseMessageLocationPrompt(TimedBroadcast plugin) {
        super(plugin.getLanguage().getMessage("termGlobally"), plugin.getLanguage().getMessage("termWorld"));
        this.plugin = plugin;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        conversationContext.setSessionData("where", s);

        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termGlobally"))) {
            return new ConfirmSettingsPrompt(plugin);
        } else if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termWorld"))) {
            return new ChooseWorldPompt(plugin);
        }

        return null;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptWhere")) + " " + formatFixedSet();
    }
}
