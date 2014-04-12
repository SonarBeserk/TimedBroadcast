package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.commands.MainCmd;
import me.sonarbeserk.listeners.FileVersionListener;
import me.sonarbeserk.timedbroadcast.tasks.MinuteMessageTask;
import me.sonarbeserk.timedbroadcast.tasks.SecondMessageTask;
import me.sonarbeserk.updating.UpdateListener;
import me.sonarbeserk.updating.Updater;
import me.sonarbeserk.utils.Data;
import me.sonarbeserk.utils.Language;
import me.sonarbeserk.utils.Messaging;
import org.bukkit.plugin.java.JavaPlugin;

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
public class TimedBroadcast extends JavaPlugin {

    private Language language = null;

    private Data data = null;

    private Messaging messaging = null;

    public boolean running = true;

    public SecondMessageTask secondMessageTask = null;

    public MinuteMessageTask minuteMessageTask = null;

    private Updater updater = null;

    private boolean upToDate = false;

    public boolean updateFound = false;

    public boolean updateDownloaded = false;

    public void onEnable() {

        saveDefaultConfig();

        language = new Language(this);

        data = new Data(this);

        messaging = new Messaging(this);

        if(getConfig().getBoolean("settings.save-state")) {

            if (getData().get("updater-state") != null) {

                running = Boolean.parseBoolean(String.valueOf(getData().get("updater-state")));
            }
        }

        getCommand(getDescription().getName().toLowerCase()).setExecutor(new MainCmd(this));

        getServer().getPluginManager().registerEvents(new FileVersionListener(this), this);

        secondMessageTask = new SecondMessageTask(this);

        // 20 ticks = 1 sec 20x1 = 20
        secondMessageTask.runTaskTimer(this, 0, 20);

        minuteMessageTask = new MinuteMessageTask(this);

        // 20 ticks = 1 sec 60 sec = 1 min 20x60 = 1200
        minuteMessageTask.runTaskTimer(this, 0, 1200);

        checkForUpdates();
    }

    private void checkForUpdates() {

        if(getConfig().getBoolean("settings.updater.enabled")) {

            if(getConfig().getString("settings.updater.mode").equalsIgnoreCase("notify")) {

                updater = new Updater(this, 00000 /*replace with the proper id when distributing*/, getFile(), Updater.UpdateType.NO_DOWNLOAD, false);

                if(Double.parseDouble(getDescription().getVersion().replaceAll("[a-zA-Z]", "")) == Double.parseDouble(updater.getLatestName().replaceAll("[a-zA-Z]", ""))) {

                    getLogger().info(getLanguage().getMessage("updater-up-to-date"));
                    upToDate = true;
                }

                if(Double.parseDouble(getDescription().getVersion().replaceAll("[a-zA-Z]", "")) < Double.parseDouble(updater.getLatestName().replaceAll("[a-zA-Z]", "")) && updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE && !upToDate) {

                    getLogger().info(getLanguage().getMessage("updater-notify").replace("{new}", updater.getLatestName()).replace("{link}", updater.getLatestFileLink()));
                }
            } else if(getConfig().getString("settings.updater.mode").equalsIgnoreCase("update")) {

                updater = new Updater(this, 00000 /*replace with the proper id when distributing*/, getFile(), Updater.UpdateType.DEFAULT, getConfig().getBoolean("settings.updater.log-downloads"));

                if(Double.parseDouble(getDescription().getVersion().replaceAll("[a-zA-Z]", "")) == Double.parseDouble(updater.getLatestName().replaceAll("[a-zA-Z]", ""))) {

                    getLogger().info(getLanguage().getMessage("updater-up-to-date"));
                    upToDate = true;
                }

                if(Double.parseDouble(getDescription().getVersion().replaceAll("[a-zA-Z]", "")) < Double.parseDouble(updater.getLatestName().replaceAll("[a-zA-Z]", "")) && updater.getResult() == Updater.UpdateResult.SUCCESS && !upToDate) {

                    getLogger().info(getLanguage().getMessage("updater-updated").replace("{new}", updater.getLatestName()));
                }
            }

            getServer().getPluginManager().registerEvents(new UpdateListener(this), this);
        }
    }

    /**
     * Returns the plugin updater instance
     * @return the plugin updater instance
     */
    public Updater getUpdater()
    {
        return this.updater;
    }

    /**
     * Returns the language in use
     * @return the language in use
     */
    public Language getLanguage() {

        return language;
    }

    /**
     * Returns the data instance
     * @return the data instance
     */
    public Data getData() {

        return data;
    }

    /**
     * Returns the plugin messaging instance
     * @return the plugin messaging instance
     */
    public Messaging getMessaging() {

        return messaging;
    }

    public void onDisable() {

        secondMessageTask.persistData();

        minuteMessageTask.persistData();

        secondMessageTask.safeCancel();

        secondMessageTask = null;

        minuteMessageTask.safeCancel();

        minuteMessageTask = null;

        if(getConfig().getBoolean("settings.save-state")) {

            data.set("save-broadcast-state", running);
        }

        data.save();
        data = null;

        messaging = null;

        language = null;
    }
}
