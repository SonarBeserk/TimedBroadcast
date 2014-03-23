package me.sonarbeserk.commands;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/***********************************************************************************************************************
 *
 * BeserkUtils - Premade classes for use in my bukkit plugins
 * ===========================================================================
 *
 * Copyright (C) 2014 by SonarBeserk
 * https://github.com/SonarBeserk/BeserkUtils
 *
 ***********************************************************************************************************************
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ***********************************************************************************************************************/
public class MainCmd implements CommandExecutor {

    private TimedBroadcast plugin = null;

    public MainCmd(TimedBroadcast plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {

            if(sender instanceof Player) {

                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                return true;
            } else {

                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                return true;
            }
        }

        if(args.length > 0) {

            if(args[0].equalsIgnoreCase("help")) {

                if(sender instanceof Player) {

                    plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                    return true;
                } else {

                    plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                    return true;
                }
            }

            if(args[0].equalsIgnoreCase("reload")) {

                if(!sender.hasPermission("timedbroadcast.commands.reload")) {

                    if(sender instanceof Player) {

                        plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("no-permission"));
                        return true;
                    } else {

                        plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("no-permission"));
                        return true;
                    }
                }

                plugin.getLanguage().reload();
                plugin.reloadConfig();

                if(sender instanceof Player) {

                    plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("reloaded"));
                    return true;
                } else {

                    plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("reloaded"));
                    return true;
                }
            }

            if(sender instanceof Player) {

                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                return true;
            } else {

                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usage-timedbroadcast").replace("{name}", plugin.getDescription().getName()));
                return true;
            }
        }

        return false;
    }
}
