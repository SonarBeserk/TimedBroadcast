package me.sonarbeserk.timedbroadcast.tasks;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ********************************************************************************************************************
 * <p/>
 * TimedBroadcast - Bukkit plugin to broadcast timed messages
 * ===========================================================================
 * <p/>
 * Copyright (C) 2012, 2013, 2014 by SonarBeserk
 * http://dev.bukkit.org/bukkit-plugins/timedbroadcast/
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * *********************************************************************************************************************
 */
public class SecondMessageTask extends BukkitRunnable {

    private TimedBroadcast plugin = null;

    private Map<String, Integer> messageMap = null;

    private boolean running = false;

    public SecondMessageTask(TimedBroadcast plugin) {

        this.plugin = plugin;

        buildMessageMap();

        addPersistedData();
    }

    public void addPersistedData() {
        List<String> messageTimes = (ArrayList<String>) plugin.getData().get("message-times");

        if (messageTimes == null || messageTimes.size() == 0) {
            return;
        }

        for (String timeString : messageTimes) {

            if (timeString == null || timeString.trim().equalsIgnoreCase("")) {
                continue;
            }

            String split[] = timeString.split("\\|");

            if (split.length == 1 || split.length == 2) {
                continue;
            }

            if (split[1].replaceAll("[a-zA-Z]", "") == null) {
                continue;
            }

            /* Validity Checking */
            if (plugin.getConfig().getConfigurationSection("settings.messages") == null) {
                continue;
            }

            if (plugin.getConfig().get("settings.messages." + split[0]) == null || plugin.getConfig().get("settings.messages." + split[0] + ".enabled") == null || plugin.getConfig().get("settings.messages." + split[0] + ".message-text") == null || plugin.getConfig().get("settings.messages." + split[0] + ".time-unit") == null || plugin.getConfig().get("settings.messages." + split[0] + ".time-interval") == null) {
                continue;
            }

            if (!plugin.getConfig().getBoolean("settings.messages." + split[0] + ".enabled") || plugin.getConfig().getString("settings.messages." + split[0] + ".message-text").trim().equalsIgnoreCase("") || !plugin.getConfig().getString("settings.messages." + split[0] + ".time-unit").equalsIgnoreCase("second") || plugin.getConfig().getInt("settings.messages." + split[0] + ".time-interval") == 0) {
                continue;
            }
            /* Bound Checking */

            if (!split[1].equalsIgnoreCase("second")) {
                continue;
            }

            messageMap.put(split[0], Integer.parseInt(split[2]));
        }

        plugin.getData().set("message-times", null);
    }

    public void buildMessageMap() {

        if (messageMap != null) {

            messageMap = null;
        }

        messageMap = new HashMap<String, Integer>();

        ConfigurationSection messageSection = plugin.getConfig().getConfigurationSection("settings.messages");

        if (messageSection.getKeys(false).size() == 0) {
            return;
        }

        for (String messageName : messageSection.getKeys(false)) {

             /* Bound Checking */
            if (plugin.getConfig().getConfigurationSection("settings.messages") == null) {
                continue;
            }

            if (plugin.getConfig().get("settings.messages." + messageName) == null || plugin.getConfig().get("settings.messages." + messageName + ".enabled") == null || plugin.getConfig().get("settings.messages." + messageName + ".message-text") == null || plugin.getConfig().get("settings.messages." + messageName + ".time-unit") == null || plugin.getConfig().get("settings.messages." + messageName + ".time-interval") == null) {
                continue;
            }

            if (!plugin.getConfig().getBoolean("settings.messages." + messageName + ".enabled") || plugin.getConfig().getString("settings.messages." + messageName + ".message-text").equalsIgnoreCase("") || plugin.getConfig().getString("settings.messages." + messageName + ".message-text").equalsIgnoreCase("") || !plugin.getConfig().getString("settings.messages." + messageName + ".time-unit").equalsIgnoreCase("second") || plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval") == 0) {
                continue;
            }
            /* Bound Checking */

            if (!messageMap.keySet().contains(messageName)) {

                messageMap.put(messageName, plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval"));
            }
        }
    }

    public void run() {

        if (!plugin.running || messageMap == null || messageMap.size() == 0) {
            return;
        }

        if (!running) {
            running = true;
        }

        for (String messageName : messageMap.keySet()) {

            messageMap.put(messageName, messageMap.get(messageName) + 1);

            if (messageMap.get(messageName) > plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval")) {

                messageMap.put(messageName, plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval"));
            }

            if (plugin.getServer().getOnlinePlayers().length < plugin.getConfig().getInt("settings.minimum-player-count")) {
                return;
            }

            if (messageMap.get(messageName) == plugin.getConfig().getInt("settings.messages." + messageName + ".time-interval")) {

                if (plugin.getConfig().getBoolean("settings.prefix-messages")) {

                    String prefix = plugin.getConfig().getString("settings.messenger-prefix").replace("{name}", plugin.getName());

                    plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("settings.messages." + messageName + ".message-text")));
                } else {

                    plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.messages." + messageName + ".message-text")));
                }

                messageMap.put(messageName, 0);
                continue;
            }
        }
    }

    public void persistData() {
        List<String> persistenceStringList = null;

        if (plugin.getData().get("message-times") == null) {

            persistenceStringList = new ArrayList<String>();
        } else {

            persistenceStringList = (ArrayList<String>) plugin.getData().get("message-times");
        }

        for (String messageName : messageMap.keySet()) {

            persistenceStringList.add(messageName + "|" + plugin.getConfig().getString("settings.messages." + messageName + ".time-unit") + "|" + messageMap.get(messageName));
        }

        plugin.getData().set("message-times", persistenceStringList);
    }
}
