package me.sonarbeserk.listeners;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.utils.LatestVersionsFile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

/**
 * ********************************************************************************************************************
 * <p/>
 * BeserkUtils - Premade classes for use in my bukkit plugins
 * ===========================================================================
 * <p/>
 * Copyright (C) 2014 by SonarBeserk
 * https://github.com/SonarBeserk/BeserkUtils
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
public class FileVersionListener implements Listener {

    private TimedBroadcast plugin = null;

    private LatestVersionsFile latestVersionsFile = null;

    public FileVersionListener(TimedBroadcast plugin) {

        this.plugin = plugin;

        latestVersionsFile = new LatestVersionsFile(plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void pluginEnable(PluginEnableEvent e) {

        if (!e.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
            return;
        }

        if (latestVersionsFile == null) {
            return;
        }

        boolean configOutOfDate = false;
        boolean localeOutOfDate = false;

        if (Double.parseDouble((String) latestVersionsFile.get("config")) > plugin.getConfig().getDouble("version")) {

            configOutOfDate = true;
        }

        if (Double.parseDouble((String) latestVersionsFile.get("locale")) > Double.parseDouble(plugin.getLanguage().getMessage("version"))) {

            localeOutOfDate = true;
        }

        if (configOutOfDate) {

            plugin.getMessaging().sendMessage(plugin.getServer().getConsoleSender(), false, false, plugin.getLanguage().getMessage("out-of-date-config"));
        }

        if (localeOutOfDate) {

            plugin.getMessaging().sendMessage(plugin.getServer().getConsoleSender(), false, false, plugin.getLanguage().getMessage("out-of-date-locale"));
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {

            if (player.hasPermission("timedbroadcast.notify.files")) {

                if (configOutOfDate) {

                    plugin.getMessaging().sendMessage(player, true, true, plugin.getLanguage().getMessage("out-of-date-config"));
                }

                if (localeOutOfDate) {

                    plugin.getMessaging().sendMessage(player, true, true, plugin.getLanguage().getMessage("out-of-date-locale"));
                }

                continue;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void pluginDisable(PluginDisableEvent e) {

        if (!e.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
            return;
        }

        if (latestVersionsFile == null) {
            return;
        }

        latestVersionsFile = null;
    }
}
