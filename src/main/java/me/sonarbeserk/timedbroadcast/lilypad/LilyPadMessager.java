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

package me.sonarbeserk.timedbroadcast.lilypad;

import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.wrapper.Message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class LilyPadMessager {
    private TimedBroadcast plugin = null;

    public LilyPadMessager(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    /**
     * Distributes a message to all current servers
     * @param message the message to distribute to all servers
     */
    public void distributeMessage(Message message) {
        try {
            plugin.getLilypadConnection().request(new MessageRequest(new ArrayList<String>(), "tb.request.lilypad.distribute", message.getMessage().trim() + "|" + String.valueOf(message.getUnit()).trim() + "|" + String.valueOf(message.getInterval()).trim() + "|" + String.valueOf(message.getWorldName()).trim() + "|" + String.valueOf(message.getGroupName()).trim() + "|" + String.valueOf(message.getCounter())));
        } catch (RequestException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
