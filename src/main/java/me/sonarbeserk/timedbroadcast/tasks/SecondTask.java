package me.sonarbeserk.timedbroadcast.tasks;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.enums.MessageLocation;
import me.sonarbeserk.timedbroadcast.enums.TimeUnit;
import me.sonarbeserk.timedbroadcast.wrapper.Message;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SecondTask extends BukkitRunnable {
    private TimedBroadcast plugin = null;

    public SecondTask(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Message message: plugin.getMessages()) {
            if(!plugin.getConfig().getBoolean("settings.broadcast.counterIgnoresPlayerCount") && plugin.getServer().getOnlinePlayers().size() < plugin.getConfig().getInt("settings.broadcast.requiredPlayers")) {return;}

            if(message.getUnit() == TimeUnit.SECOND) {
                if(message.getCounter() >= message.getInterval()) {
                    broadcastMessage(message);

                    message.setCounter(0);
                    continue;
                }

                message.setCounter(message.getCounter() + 1);
            }
        }
    }

    private void broadcastMessage(Message message) {
        for(Player player: plugin.getServer().getOnlinePlayers()) {
            if(message.getLocation() == MessageLocation.GLOBALLY) {
                if(plugin.getConfig().getBoolean("settings.broadcast.prefixEnabled")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.broadcast.prefix") + message.getMessage()));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
                }
            } else if(message.getLocation() == MessageLocation.WORLD) {
                if(message.getWorldName() == null) {continue;}

                if(!player.getWorld().getName().equalsIgnoreCase(message.getWorldName())) {continue;}

                if(plugin.getConfig().getBoolean("settings.broadcast.prefixEnabled")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("settings.broadcast.prefix") + message.getMessage()));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.getMessage()));
                }
            }
        }
    }
}
