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

package com.serkprojects.timedbroadcast.tasks;

import com.serkprojects.timedbroadcast.TimedBroadcast;
import com.serkprojects.timedbroadcast.enums.TimeUnit;
import com.serkprojects.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTask extends BukkitRunnable {
    private TimedBroadcast plugin = null;

    private TimeUnit timeUnit = null;

    public MessageTask(TimedBroadcast plugin, TimeUnit timeUnit) {
        this.plugin = plugin;

        this.timeUnit = timeUnit;
    }

    @Override
    public void run() {
        if (!plugin.shouldBroadcast()) {
            return;
        }

        for (Message message : plugin.getMessages()) {
            if (!plugin.getConfig().getBoolean("settings.broadcast.counterIgnoresPlayerCount") && plugin.getServer().getOnlinePlayers().size() < plugin.getConfig().getInt("settings.broadcast.requiredPlayers")) {
                return;
            }

            if (message.getUnit() == timeUnit) {
                if (message.getCounter() >= message.getInterval()) {
                    broadcastMessage(message);

                    message.setCounter(0);
                    continue;
                }

                message.setCounter(message.getCounter() + 1);
            }
        }
    }

    private void broadcastMessage(Message message) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (message.getWorldName() != null) {
                if (!worldCheckMessage(message, player)) {
                    continue;
                }
            }

            if (message.getGroupName() != null && plugin.getPermissions() != null) {
                if (!groupCheckMessage(message, player)) {
                    continue;
                }
            }

            if (plugin.getConfig().getBoolean("settings.broadcast.prefixEnabled")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.broadcast.prefix") + message.getMessage()));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
            }
        }
    }

    private boolean worldCheckMessage(Message message, Player player) {
        if (message == null || player == null || player.getWorld() == null || !player.getWorld().getName().equalsIgnoreCase(message.getWorldName())) {
            return false;
        }

        return true;
    }

    private boolean groupCheckMessage(Message message, Player player) {
        if (message == null || player == null || plugin.getPermissions().getPrimaryGroup(player) == null || !plugin.getPermissions().getPrimaryGroup(player).equalsIgnoreCase(message.getGroupName())) {
            return false;
        }

        return true;
    }
}
