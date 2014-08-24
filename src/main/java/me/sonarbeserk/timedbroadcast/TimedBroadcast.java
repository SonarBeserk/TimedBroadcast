package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.beserkcore.plugin.JavaPlugin;
import me.sonarbeserk.timedbroadcast.commands.MainCmd;

public class TimedBroadcast extends JavaPlugin {

    public void onEnable() {
        super.onEnable();

        getCommand(getName().toLowerCase()).setExecutor(new MainCmd(this));
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

    public void onDisable() {
        super.onDisable();
    }
}
