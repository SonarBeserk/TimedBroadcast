package me.sonarbeserk.timedbroadcast;

import me.sonarbeserk.beserkcore.plugin.JavaPlugin;

public class TimedBroadcast extends JavaPlugin {

    public void onEnable() {
        super.onEnable();
    }

    @Override
    public boolean shouldSaveData() {
        return false;
    }

    @Override
    public boolean registerPremadeMainCMD() {
        return true;
    }

    @Override
    public String getPermissionPrefix() {
        return getName().toLowerCase();
    }

    public void onDisable() {
        super.onDisable();
    }
}
