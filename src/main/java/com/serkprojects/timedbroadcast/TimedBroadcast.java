/**
 * *********************************************************************************************************************
 * TimedBroadcast - Automatically broadcasts messages to the server
 * =====================================================================================================================
 *  Copyright (C) 2012-2015 by SonarBeserk
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

package com.serkprojects.timedbroadcast;

import com.serkprojects.serkcore.plugin.JavaPlugin;
import lilypad.client.connect.api.Connect;
import com.serkprojects.timedbroadcast.commands.BroadcastCmd;
import com.serkprojects.timedbroadcast.enums.TimeUnit;
import com.serkprojects.timedbroadcast.lilypad.LilyPadMessenger;
import com.serkprojects.timedbroadcast.lilypad.listeners.MessageListener;
import com.serkprojects.timedbroadcast.tasks.MessageTask;
import com.serkprojects.timedbroadcast.wrapper.Message;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;

public class TimedBroadcast extends JavaPlugin {
    private Connect lilypadConnection = null;

    private LilyPadMessenger lilyPadMessenger = null;

    private Permission permission = null;

    private ArrayList<Message> messages = null;

    private MessageTask secondTask = null;

    private MessageTask minuteTask = null;

    private boolean broadcast = true;

    public void onEnable() {
        super.onEnable();

        if (getServer().getPluginManager().getPlugin("LilyPad-Connect") != null && getServer().getPluginManager().getPlugin("LilyPad-Connect").isEnabled()) {
            lilypadConnection = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
            lilypadConnection.registerEvents(new MessageListener(this));

            lilyPadMessenger = new LilyPadMessenger(this);

            getLogger().info(getLanguage().getMessage("lilypadSupportEnabled"));
        }

        if (getServer().getPluginManager().getPlugin("Vault") != null && getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            setupPermissions();

            if (permission != null) {
                getLogger().info(getLanguage().getMessage("vaultHooked"));
            } else {
                getLogger().warning(getLanguage().getMessage("vaultNoPermissionPlugin"));
            }
        } else {
            getLogger().warning(getLanguage().getMessage("vaultHookFailed"));
        }

        messages = new ArrayList<Message>();

        if (getData().get("broadcastsEnabled") != null) {
            broadcast = Boolean.parseBoolean(String.valueOf(getData().get("broadcastsEnabled")));
        }

        if (getData().getConfigurationSection("messages") != null) {
            for (String entry : getData().getConfigurationSection("messages").getKeys(false)) {
                if (getData().get("messages." + entry + ".message") == null || getData().get("messages." + entry + ".unit") == null || getData().get("messages." + entry + ".interval") == null || getData().get("messages." + entry + ".location") == null || getData().get("messages." + entry + ".worldName") == null || getData().get("messages." + entry + ".groupName") == null) {
                    continue;
                }

                String message = String.valueOf(getData().get("messages." + entry + ".message"));

                TimeUnit timeUnit = TimeUnit.valueOf(String.valueOf(getData().get("messages." + entry + ".unit")));

                int interval = Integer.parseInt(String.valueOf(getData().get("messages." + entry + ".interval")));

                String worldName = String.valueOf(getData().get("messages." + entry + ".worldName"));

                String groupName = String.valueOf(getData().get("messages." + entry + ".groupName"));

                Message messageWrapper = new Message(message, timeUnit, interval, worldName, groupName);

                if (getData().get("messages." + entry + ".counter") != null) {
                    messageWrapper.setCounter(Integer.parseInt(String.valueOf(getData().get("messages." + entry + ".counter"))));
                }

                addMessage(messageWrapper);
            }
        }

        secondTask = new MessageTask(this, TimeUnit.SECOND);
        secondTask.runTaskTimer(this, 0, 20);

        minuteTask = new MessageTask(this, TimeUnit.MINUTE);
        minuteTask.runTaskTimer(this, 0, 1200);

        getCommand(getName().toLowerCase()).setExecutor(new BroadcastCmd(this));
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

    @Override
    public void onReload() {
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        return (permission != null);
    }

    /**
     * Returns the lilypad connection in use
     *
     * @return the lilypad connection in use
     */
    public Connect getLilypadConnection() {
        return lilypadConnection;
    }

    /**
     * Returns the lilypad messenger in use
     *
     * @return the lilypad messenger in use
     */
    public LilyPadMessenger getLilyPadMessenger() {
        return lilyPadMessenger;
    }

    /**
     * Returns the permission instance
     *
     * @return the permission instance
     */
    public Permission getPermissions() {
        return permission;
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
            getData().set("messages." + i + ".message", getMessages().get(i).getMessage());
            getData().set("messages." + i + ".unit", getMessages().get(i).getUnit().name());
            getData().set("messages." + i + ".interval", getMessages().get(i).getInterval());
            getData().set("messages." + i + ".worldName", String.valueOf(getMessages().get(i).getWorldName()));
            getData().set("messages." + i + ".groupName", String.valueOf(getMessages().get(i).getGroupName()));
            getData().set("messages." + i + ".counter", getMessages().get(i).getCounter());
        }

        super.onDisable();

        messages = null;

        permission = null;
    }
}
