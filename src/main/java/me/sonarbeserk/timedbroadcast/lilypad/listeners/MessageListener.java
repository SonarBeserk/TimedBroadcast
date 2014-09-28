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

package me.sonarbeserk.timedbroadcast.lilypad.listeners;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;

import java.io.UnsupportedEncodingException;

public class MessageListener {
    private TimedBroadcast plugin = null;

    public MessageListener(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @EventListener()
    public void messageEvent(MessageEvent e) {
        if (!e.getChannel().startsWith("tb")) {
            return;
        }

        if (e.getChannel().equalsIgnoreCase("tb.request.lilypad.distribute")) {
            try {
                String[] splitMessage = e.getMessageAsString().split("\\|");

                if (splitMessage.length < 6) {
                    return;
                }

                String worldName = null, groupName = null;

                if (!splitMessage[3].equalsIgnoreCase("null")) {
                    worldName = splitMessage[3];
                }

                if (!splitMessage[4].equalsIgnoreCase("null")) {
                    groupName = splitMessage[4];
                }

                Message message = new Message(splitMessage[0], TimeUnit.valueOf(splitMessage[1]), Integer.parseInt(splitMessage[2]), worldName, groupName);

                message.setCounter(Integer.parseInt(splitMessage[5]));

                plugin.addMessage(message);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
    }
}
