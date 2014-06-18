package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.beserkcore.commands.MainCmd;
import me.sonarbeserk.beserkcore.plugin.BeserkUpdatingJavaPlugin;
import me.sonarbeserk.timedbroadcast.tasks.MinuteMessageTask;
import me.sonarbeserk.timedbroadcast.tasks.SecondMessageTask;

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
public class TimedBroadcast extends BeserkUpdatingJavaPlugin {

    public boolean running = true;
    public SecondMessageTask secondMessageTask = null;
    public MinuteMessageTask minuteMessageTask = null;
    private boolean upToDate = false;

    public void onEnable() {

        super.onEnable();

        if (getConfig().getBoolean("settings.save-state")) {

            if (getData().get("updater-state") != null) {

                running = Boolean.parseBoolean(String.valueOf(getData().get("updater-state")));
            }
        }

        getCommand(getDescription().getName().toLowerCase()).setExecutor(new MainCmd(this));

        secondMessageTask = new SecondMessageTask(this);

        // 20 ticks = 1 sec 20x1 = 20
        secondMessageTask.runTaskTimer(this, 0, 20);

        minuteMessageTask = new MinuteMessageTask(this);

        // 20 ticks = 1 sec 60 sec = 1 min 20x60 = 1200
        minuteMessageTask.runTaskTimer(this, 0, 1200);
    }

    @Override
    public boolean shouldSaveData() {return true;}

    @Override
    public boolean checkFileVersions() {return true;}

    public void onDisable() {

        secondMessageTask.persistData();

        minuteMessageTask.persistData();

        secondMessageTask.safeCancel();

        secondMessageTask = null;

        minuteMessageTask.safeCancel();

        minuteMessageTask = null;

        if (getConfig().getBoolean("settings.save-state")) {

            getData().set("save-broadcast-state", running);
        }

        super.onDisable();
    }
}
