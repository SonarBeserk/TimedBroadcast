/**
 * ********************************************************************************************************************
 * TimedBroadcast - Automatically broadcasts messages to the server
 * ====================================================================================================================
 *  Copyright (C) 2012-2014 by SonarBeserk
 * http://dev.bukkit.org/bukkit-plugins/timedbroadcast/
 * *********************************************************************************************************************
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * *********************************************************************************************************************
 * Please refer to LICENSE for the full license. If it is not there, see <http://www.gnu.org/licenses/>.
 * *********************************************************************************************************************
 */

package me.sonarbeserk.timedbroadcast.wrapper;

import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;

public class Message {
    private String message = null;

    private TimeUnit unit = null;

    private int interval;

    private MessageLocation location = null;

    private String worldName = null;

    public Message(String message, TimeUnit unit, int interval, MessageLocation location, String worldName) {
        this.message = message;

        this.unit = unit;

        this.interval = interval;

        this.location = location;

        this.worldName = worldName;
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
     * Returns the location of the message
     *
     * @return the location of the message
     */
    public MessageLocation getLocation() {
        return location;
    }

    /**
     * Returns the name of the world the message should show in
     *
     * @return the name of the world the message should show in
     */
    public String getWorldName() {
        return worldName;
    }
}
