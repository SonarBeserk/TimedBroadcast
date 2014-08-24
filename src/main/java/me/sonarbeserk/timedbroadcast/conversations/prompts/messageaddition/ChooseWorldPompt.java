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

package me.sonarbeserk.timedbroadcast.conversations.prompts.messageaddition;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseWorldPompt extends StringPrompt {
    private TimedBroadcast plugin = null;

    private ArrayList<String> worldNames = null;

    public ChooseWorldPompt(TimedBroadcast plugin) {
        this.plugin = plugin;

        worldNames = new ArrayList<String>();

        for(World world: plugin.getServer().getWorlds()) {
            worldNames.add(world.getName());
        }
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getLanguage().getMessage("promptWorld").replace("{worlds}", Arrays.toString(worldNames.toArray())));
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if(!worldNames.contains(s)) {
            return new ChooseWorldPompt(plugin);
        }

        conversationContext.setSessionData("world", s);

        return new ConfirmSettingsPrompt(plugin);
    }
}
