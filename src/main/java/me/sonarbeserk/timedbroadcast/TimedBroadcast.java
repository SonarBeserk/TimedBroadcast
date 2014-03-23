package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.commands.MainCmd;
import me.sonarbeserk.listeners.FileVersionListener;
import me.sonarbeserk.timedbroadcast.tasks.MinuteMessageTask;
import me.sonarbeserk.timedbroadcast.tasks.SecondMessageTask;
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

    public SecondMessageTask secondMessageTask = null;

    public MinuteMessageTask minuteMessageTask = null;

    public void onEnable() {

        saveDefaultConfig();

        language = new Language(this);

        data = new Data(this);

        messaging = new Messaging(this);

        getCommand(getDescription().getName().toLowerCase()).setExecutor(new MainCmd(this));

        getServer().getPluginManager().registerEvents(new FileVersionListener(this), this);

        secondMessageTask = new SecondMessageTask(this);

        // 20 ticks = 1 sec 20x1 = 20
        secondMessageTask.runTaskTimer(this, 0, 20);

        minuteMessageTask = new MinuteMessageTask(this);

        // 20 ticks = 1 sec 60 sec = 1 min 20x60 = 1200
        minuteMessageTask.runTaskTimer(this, 0, 1200);
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

        data.save();
        data = null;

        messaging = null;

        language = null;
    }
}
