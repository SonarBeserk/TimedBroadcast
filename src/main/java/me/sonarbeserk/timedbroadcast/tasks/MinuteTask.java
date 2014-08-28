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

package me.sonarbeserk.timedbroadcast.tasks;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MinuteTask extends BukkitRunnable {
    private TimedBroadcast plugin = null;

    public MinuteTask(TimedBroadcast plugin) {
        this.plugin = plugin;
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

            if (message.getUnit() == TimeUnit.MINUTE) {
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
            if (message.getLocation() == MessageLocation.GLOBALLY) {
                if (plugin.getConfig().getBoolean("settings.broadcast.prefixEnabled")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.broadcast.prefix") + message.getMessage()));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
                }
            } else if (message.getLocation() == MessageLocation.WORLD) {
                if (message.getWorldName() == null) {
                    continue;
                }

                if (!player.getWorld().getName().equalsIgnoreCase(message.getWorldName())) {
                    continue;
                }

                if (plugin.getConfig().getBoolean("settings.broadcast.prefixEnabled")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.broadcast.prefix") + message.getMessage()));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
                }
            }
        }
    }
}
