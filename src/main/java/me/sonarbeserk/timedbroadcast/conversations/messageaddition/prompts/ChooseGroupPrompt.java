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

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseGroupPrompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    private ArrayList<String> groupNames = null;

    public ChooseGroupPrompt(TimedBroadcast plugin) {
        this.plugin = plugin;

        groupNames = new ArrayList<String>();

        for (String groupName : plugin.getPermissions().getGroups()) {
            groupNames.add(groupName + ChatColor.RESET);
        }
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptGroup")
                .replace("{groups}", Arrays.toString(groupNames.toArray())));
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        for (String worldName : groupNames) {
            if (s.equalsIgnoreCase(worldName)) {
                conversationContext.setSessionData("group", s);

                return new DetailsCorrectPrompt(plugin);
            }
        }

        return this;
    }
}
