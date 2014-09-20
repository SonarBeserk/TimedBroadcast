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

package me.sonarbeserk.timedbroadcast.wrapper;

import me.sonarbeserk.timedbroadcast.enums.TimeUnit;

public class Message {
    private String message = null;

    private TimeUnit unit = null;

    private int interval;

    private String worldName = null;

    private String groupName = null;

    private int counter = 0;

    public Message(String message, TimeUnit unit, int interval, String worldName, String groupName) {
        this.message = message;

        this.unit = unit;

        this.interval = interval;

        this.worldName = worldName;

        this.groupName = groupName;
    }

    /**
     * Returns the message of the message
     *
     * @return the message of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the time unit of the message
     *
     * @return the time unit of the message
     */
    public TimeUnit getUnit() {
        return unit;
    }

    /**
     * Returns the message interval
     *
     * @return the message interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Returns the name of the world the message should show in
     *
     * @return the name of the world the message should show in
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Returns the name of the group that this message should be restricted to
     * @return the name of the group that this message should be restricted to
     */
    public String getGroupName() {return groupName;}

    /**
     * Returns the counter for the message
     *
     * @return the counter for the message
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the message's counter
     *
     * @param counter the counter of the message
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
}
