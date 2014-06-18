package me.sonarbeserk.updating;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

    private TimedBroadcast plugin = null;

    public UpdateListener(TimedBroadcast plugin) {

        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void playerJoin(PlayerJoinEvent e) {

        if (!e.getPlayer().hasPermission("timedbroadcast.notify.updates")) return;

        if (plugin.updateFound) {

            plugin.getMessaging().sendMessage(e.getPlayer(), true, true, plugin.getLanguage().getMessage("update-found").replace("{new}", plugin.getUpdater().getLatestName()).replace("{link}", plugin.getUpdater().getLatestFileLink()));
            return;
        }
        if (plugin.updateDownloaded) {

            plugin.getMessaging().sendMessage(e.getPlayer(), true, true, plugin.getLanguage().getMessage("update-downloaded").replace("{new}", plugin.getUpdater().getLatestName()));
            return;
        }
    }
}
