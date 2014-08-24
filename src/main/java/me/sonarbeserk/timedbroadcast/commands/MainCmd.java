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

package me.sonarbeserk.timedbroadcast.commands;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.conversations.prompts.messageaddition.AddingMessageStartPrompt;
import me.sonarbeserk.timedbroadcast.conversations.prompts.messageaddition.messageBuilder.MessageBuilderAbandonedListener;
import me.sonarbeserk.timedbroadcast.conversations.prompts.messageaddition.messageBuilder.MessageBuilderPrefix;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class MainCmd implements CommandExecutor {
    private TimedBroadcast plugin = null;

    public MainCmd(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
                return true;
            } else {
                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
                return true;
            }
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                helpSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                reloadSubCommand(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("add")) {
                addSubCommand(sender);
                return true;
            }

            if (sender instanceof Player) {
                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
                return true;
            } else {
                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
                return true;
            }
        }

        return true;
    }

    private void helpSubCommand(CommandSender sender) {
        if (sender instanceof Player) {
            plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
            return;
        } else {
            plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("usageMain").replace("{name}", plugin.getDescription().getName()));
            return;
        }
    }

    private void reloadSubCommand(CommandSender sender) {
        if (!sender.hasPermission(plugin.getPermissionPrefix() + ".commands.reload")) {
            if (sender instanceof Player) {
                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("noPermission"));
                return;
            } else {
                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("noPermission"));
                return;
            }
        }

        plugin.getLanguage().reload();
        plugin.reloadConfig();

        if (sender instanceof Player) {
            plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("reloaded"));
            return;
        } else {
            plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("reloaded"));
            return;
        }
    }

    private void addSubCommand(CommandSender sender) {
        if(!(sender instanceof Player)) {
            plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("commandPlayerRequired"));
            return;
        }

        if (!sender.hasPermission(plugin.getPermissionPrefix() + ".commands.add")) {
            if (sender instanceof Player) {
                plugin.getMessaging().sendMessage(sender, true, true, plugin.getLanguage().getMessage("noPermission"));
                return;
            } else {
                plugin.getMessaging().sendMessage(sender, false, false, plugin.getLanguage().getMessage("noPermission"));
                return;
            }
        }

        ConversationFactory conversationFactory = new ConversationFactory(plugin);

        Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withPrefix(new MessageBuilderPrefix(plugin)).withFirstPrompt(new AddingMessageStartPrompt(plugin)).withEscapeSequence(plugin.getLanguage().getMessage("termExit")).withTimeout(30).addConversationAbandonedListener(new MessageBuilderAbandonedListener(plugin)).buildConversation((Conversable) sender);
        conversation.begin();
    }
}
