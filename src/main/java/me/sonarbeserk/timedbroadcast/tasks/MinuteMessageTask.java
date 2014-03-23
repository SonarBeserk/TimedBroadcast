package me.sonarbeserk.timedbroadcast.tasks;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***********************************************************************************************************************
 *
 * TimedBroadcast - Bukkit plugin to broadcast timed messages
 * ===========================================================================
 *
 * Copyright (C) 2012, 2013, 2014 by SonarBeserk
 * http://dev.bukkit.org/bukkit-plugins/timedbroadcast/
 *
 ***********************************************************************************************************************
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************************************************************/
public class MinuteMessageTask extends BukkitRunnable {

    private TimedBroadcast plugin = null;

    private Map<String, Integer> messageMap = null;

    public MinuteMessageTask(TimedBroadcast plugin) {

        this.plugin = plugin;

        buildMessageMap();
    }

    public void buildMessageMap() {

        messageMap = new HashMap<String, Integer>();

        if(plugin.getConfig().getBoolean("settings.resume-on-restart")) {

            if(plugin.getData().get("message-times") == null || ((List<String>) plugin.getData().get("message-times")).size() == 0) {return;}

            for(String timeString: ((List<String>) plugin.getData().get("message-times"))) {

                String split[] = timeString.split("\\|");

                if(split.length == 1) {continue;}

                if(split[1].replaceAll("[a-zA-Z]", "") == null) {continue;}

                /* Bound Checking */
                if(plugin.getConfig().getConfigurationSection("settings.messages") == null) {continue;}

                if(plugin.getConfig().get("settings.messages." + split[0]) == null || plugin.getConfig().get("settings.messages." + split[0] + ".enabled") == null || plugin.getConfig().get("settings.messages." + split[0] + ".message-text") == null || plugin.getConfig().get("settings.messages." + split[0] + ".time-unit") == null || plugin.getConfig().get("settings.messages." + split[0] + ".time-interval") == null) {continue;}

                if(!plugin.getConfig().getBoolean("settings.messages." + split[0] + ".enabled") || plugin.getConfig().getString("settings.messages." + split[0] + ".message-text").equalsIgnoreCase("") || plugin.getConfig().getString("settings.messages." + split[0] + ".message-text").equalsIgnoreCase("") || !plugin.getConfig().getString("settings.messages." + split[0] + ".time-unit").equalsIgnoreCase("minute") || plugin.getConfig().getInt("settings.messages." + split[0] + ".time-inverval") == 0) {continue;}
                /* Bound Checking */

                messageMap.put(split[0], Integer.parseInt(split[1]));
            }

            plugin.getData().set("message-times", null);
        }

        ConfigurationSection messageSection = plugin.getConfig().getConfigurationSection("settings.messages");

        if(messageSection.getKeys(false).size() == 0) {return;}

        for(String messageName: messageSection.getKeys(false)) {

             /* Bound Checking */
            if(plugin.getConfig().getConfigurationSection("settings.messages") == null) {continue;}

            if(plugin.getConfig().get("settings.messages." + messageName) == null || plugin.getConfig().get("settings.messages." + messageName + ".enabled") == null || plugin.getConfig().get("settings.messages." + messageName + ".message-text") == null || plugin.getConfig().get("settings.messages." + messageName + ".time-unit") == null || plugin.getConfig().get("settings.messages." + messageName + ".time-interval") == null) {continue;}

            if(!plugin.getConfig().getBoolean("settings.messages." + messageName + ".enabled") || plugin.getConfig().getString("settings.messages." + messageName + ".message-text").equalsIgnoreCase("") || plugin.getConfig().getString("settings.messages." + messageName + ".message-text").equalsIgnoreCase("") || !plugin.getConfig().getString("settings.messages." + messageName + ".time-unit").equalsIgnoreCase("minute") || plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval") == 0) {continue;}
            /* Bound Checking */

            if(!messageMap.keySet().contains(messageName)) {

                messageMap.put(messageName, plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval"));
            }
        }
    }

    public void run() {

        if(messageMap == null || messageMap.size() == 0) {return;}

        for(String messageName: messageMap.keySet()) {

            if(messageMap.get(messageName) == plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval")) {

                plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.messages." + messageName + ".message-text")));
                messageMap.put(messageName, 0);
                continue;
            }

            messageMap.put(messageName, messageMap.get(messageName) + 1);
            continue;
        }
    }
}
