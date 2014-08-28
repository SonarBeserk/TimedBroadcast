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

package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.beserkcore.plugin.JavaPlugin;
import me.sonarbeserk.timedbroadcast.commands.MainCmd;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.tasks.MinuteTask;
import me.sonarbeserk.timedbroadcast.tasks.SecondTask;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;

public class TimedBroadcast extends JavaPlugin {
    public static Permission permission = null;

    private ArrayList<Message> messages = null;

    private SecondTask secondTask = null;

    private MinuteTask minuteTask = null;

    private boolean broadcast = true;

    public void onEnable() {
        super.onEnable();

        if(getServer().getPluginManager().getPlugin("Vault") != null && getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            setupPermissions();

            if(permission != null) {
                getLogger().info(getLanguage().getMessage("vaultHooked"));
            } else {
                getLogger().warning(getLanguage().getMessage("vaultNoPermissionPlugin"));
            }
        } else {
            getLogger().warning(getLanguage().getMessage("vaultHookFailed"));
        }

        getCommand(getName().toLowerCase()).setExecutor(new MainCmd(this));

        messages = new ArrayList<Message>();

        if (getData().get("broadcastsEnabled") != null) {
            broadcast = Boolean.parseBoolean(String.valueOf(getData().get("broadcastsEnabled")));
        }

        if (getData().getConfigurationSection("messages") != null) {
            for (String entry : getData().getConfigurationSection("messages").getKeys(false)) {
                if (getData().get("messages." + entry + ".message") == null || getData().get("messages." + entry + ".unit") == null || getData().get("messages." + entry + ".interval") == null || getData().get("messages." + entry + ".location") == null || getData().get("messages." + entry + ".worldName") == null) {
                    continue;
                }

                String message = String.valueOf(getData().get("messages." + entry + ".message"));

                TimeUnit timeUnit = TimeUnit.valueOf(String.valueOf(getData().get("messages." + entry + ".unit")));

                int interval = Integer.parseInt(String.valueOf(getData().get("messages." + entry + ".interval")));

                MessageLocation location = null;

                location = MessageLocation.valueOf(String.valueOf(getData().get("messages." + entry + ".location")));

                String worldName = null;

                if (getData().get("messages." + entry + ".worldName") != null) {
                    worldName = String.valueOf(getData().get("messages." + entry + ".worldName"));
                }

                Message messageWrapper = new Message(message, timeUnit, interval, location, worldName);

                if (getData().get("messages." + entry + ".counter") != null) {
                    messageWrapper.setCounter(Integer.parseInt(String.valueOf(getData().get("messages." + entry + ".counter"))));
                }

                addMessage(messageWrapper);
            }
        }

        secondTask = new SecondTask(this);
        secondTask.runTaskTimer(this, 0, 20);

        minuteTask = new MinuteTask(this);
        minuteTask.runTaskTimer(this, 0, 1200);
    }

    @Override
    public boolean shouldSaveData() {
        return true;
    }

    @Override
    public boolean registerPremadeMainCMD() {
        return false;
    }

    @Override
    public String getPermissionPrefix() {
        return getName().toLowerCase();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        return (permission != null);
    }

    /**
     * Returns if the server should broadcast messages
     *
     * @return if the server should broadcast messages
     */
    public boolean shouldBroadcast() {
        return broadcast;
    }

    /**
     * Sets if broadcasts should happen
     *
     * @param broadcast if broadcasts should happen
     */
    public void shouldBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    /**
     * Returns the list of messages
     *
     * @return the list of messages
     */
    public ArrayList<Message> getMessages() {
        return messages;
    }

    /**
     * Adds a message to be broadcasted
     *
     * @param message the message to add
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Removes a message from broadcasting
     *
     * @param message the message to remove
     */
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public void onDisable() {
        secondTask.cancel();

        minuteTask.cancel();

        getData().set("broadcastsEnabled", String.valueOf(broadcast));

        for (int i = 0; i < getMessages().size(); i++) {
            if (getMessages().get(i).getLocation() == MessageLocation.GLOBALLY) {
                getData().set("messages." + i + ".message", getMessages().get(i).getMessage());
                getData().set("messages." + i + ".unit", getMessages().get(i).getUnit().name());
                getData().set("messages." + i + ".interval", getMessages().get(i).getInterval());
                getData().set("messages." + i + ".location", getMessages().get(i).getLocation().name());
                getData().set("messages." + i + ".worldName", getLanguage().getMessage("termNone"));
                getData().set("messages." + i + ".counter", getMessages().get(i).getCounter());
            } else if (getMessages().get(i).getLocation() == MessageLocation.WORLD) {
                getData().set("messages." + i + ".message", getMessages().get(i).getMessage());
                getData().set("messages." + i + ".unit", getMessages().get(i).getUnit().name());
                getData().set("messages." + i + ".interval", getMessages().get(i).getInterval());
                getData().set("messages." + i + ".location", getMessages().get(i).getLocation().name());
                getData().set("messages." + i + ".worldName", getMessages().get(i).getWorldName());
                getData().set("messages." + i + ".counter", getMessages().get(i).getCounter());
            }
        }

        messages = null;

        super.onDisable();
    }
}
