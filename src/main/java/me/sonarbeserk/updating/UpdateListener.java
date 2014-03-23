package me.sonarbeserk.updating;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateListener implements Listener {

    // needs to be replaced with a plugin with the proper methods
    private JavaPlugin plugin = null;

    public UpdateListener(JavaPlugin plugin) {

        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void playerJoin(PlayerJoinEvent e) {

        // perm needs to be updated
        if(!e.getPlayer().hasPermission("pluginname.notify.update")) return;

        if(plugin.updateFound) {

            plugin.getMessaging().sendMessage(e.getPlayer(), true, true, plugin.getLanguage().getMessage("update-found").replace("{new}", plugin.getUpdater().getLatestName()).replace("{link}", plugin.getUpdater().getLatestFileLink()));
            return;
        } if(plugin.updateDownloaded) {

            plugin.getMessaging().sendMessage(e.getPlayer(), true, true, plugin.getLanguage().getMessage("update-downloaded").replace("{new}", plugin.getUpdater().getLatestName()));
            return;
        }
    }
}
