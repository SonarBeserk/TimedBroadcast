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

package com.serkprojects.timedbroadcast.conversations.messagedistribution.messagedistributor;

import com.serkprojects.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

public class MessageDistributorAbandonedListener implements ConversationAbandonedListener {
    private TimedBroadcast plugin = null;

    public MessageDistributorAbandonedListener(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent conversationAbandonedEvent) {
        // Prevent odd npe
        if (plugin.getLanguage() == null) {
            return;
        }

        if (conversationAbandonedEvent.getContext().getSessionData("noMessages") != null) {
            return;
        }

        String prefix = plugin.getLanguage().getMessage("messageDistributorPrefix");

        if (conversationAbandonedEvent.gracefulExit()) {
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getLanguage().getMessage("messageDistributed")));
        } else {
            conversationAbandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getLanguage().getMessage("messageDistributionCancelled")));
        }
    }
}
