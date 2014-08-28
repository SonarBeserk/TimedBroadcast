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
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class ChooseMessagePrompt extends NumericPrompt {
    private TimedBroadcast plugin = null;

    public ChooseMessagePrompt(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptChooseMessage").replace("{count}", String.valueOf(plugin.getMessages().size())));
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        if (!(number instanceof Integer)) {
            return new ChooseMessagePrompt(plugin);
        }

        if (number.intValue() > plugin.getMessages().size()) {
            return new ChooseMessagePrompt(plugin);
        }

        conversationContext.setSessionData("messageID", number.intValue());

        return new ConfirmRemovalPrompt(plugin);
    }
}
