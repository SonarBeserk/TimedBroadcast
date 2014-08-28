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

package me.sonarbeserk.timedbroadcast.conversations.messagelisting.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

public class ConfirmRemovalPrompt extends FixedSetPrompt {
    private TimedBroadcast plugin = null;

    public ConfirmRemovalPrompt(TimedBroadcast plugin) {
        super(plugin.getLanguage().getMessage("termDone"), plugin.getLanguage().getMessage("termBack"));

        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Message message = plugin.getMessages().get(Integer.valueOf(String.valueOf(conversationContext.getSessionData("messageID"))) - 1);

        if (message.getLocation() == MessageLocation.GLOBALLY) {
            if (message.getUnit() == TimeUnit.SECOND) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptDone")
                        .replace("{message}", message.getMessage())
                        .replace("{interval}", String.valueOf(message.getInterval()))
                        .replace("{unit}", plugin.getLanguage().getMessage("termSecond"))
                        .replace("{world}", plugin.getLanguage().getMessage("termNone"))
                        + formatFixedSet());
            } else if (message.getUnit() == TimeUnit.MINUTE) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptDone")
                        .replace("{message}", message.getMessage())
                        .replace("{interval}", String.valueOf(message.getInterval()))
                        .replace("{unit}", plugin.getLanguage().getMessage("termMinute"))
                        .replace("{world}", plugin.getLanguage().getMessage("termNone"))
                        + formatFixedSet());
            }
        } else {
            if (message.getUnit() == TimeUnit.SECOND) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptDone")
                        .replace("{message}", message.getMessage())
                        .replace("{interval}", String.valueOf(message.getInterval()))
                        .replace("{unit}", plugin.getLanguage().getMessage("termSecond"))
                        .replace("{world}", message.getWorldName())
                        + formatFixedSet());
            } else if (message.getUnit() == TimeUnit.MINUTE) {
                return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptDone")
                        .replace("{message}", message.getMessage())
                        .replace("{interval}", String.valueOf(message.getInterval()))
                        .replace("{unit}", plugin.getLanguage().getMessage("termMinute"))
                        .replace("{world}", message.getWorldName())
                        + formatFixedSet());
            }
        }

        return null;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(plugin.getLanguage().getMessage("termBack"))) {
            return new ChooseMessagePrompt(plugin);
        }

        return Prompt.END_OF_CONVERSATION;
    }
}
