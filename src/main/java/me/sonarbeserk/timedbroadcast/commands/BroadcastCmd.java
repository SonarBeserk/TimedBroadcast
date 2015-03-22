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

package me.sonarbeserk.timedbroadcast.commands;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import me.sonarbeserk.timedbroadcast.conversations.generic.NoMessagesPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.messagebuilder.MessageBuilderAbandonedListener;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.messagebuilder.MessageBuilderPrefix;
import me.sonarbeserk.timedbroadcast.conversations.messageaddition.prompts.AddingMessageStartPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messagedistribution.messagedistributor.MessageDistributorAbandonedListener;
import me.sonarbeserk.timedbroadcast.conversations.messagedistribution.messagedistributor.MessageDistributorPrefix;
import me.sonarbeserk.timedbroadcast.conversations.messagedistribution.prompts.DistributeMessageStartPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messagelisting.messagelister.MessageListerAbandonedListener;
import me.sonarbeserk.timedbroadcast.conversations.messagelisting.messagelister.MessageListerPrefix;
import me.sonarbeserk.timedbroadcast.conversations.messagelisting.prompts.ListMessageStartPrompt;
import me.sonarbeserk.timedbroadcast.conversations.messageremoval.messageremover.MessageRemoverAbandonedListener;
import me.sonarbeserk.timedbroadcast.conversations.messageremoval.messageremover.MessageRemoverPrefix;
import me.sonarbeserk.timedbroadcast.conversations.messageremoval.prompts.RemoveMessageStartPrompt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;

public class BroadcastCmd implements CommandExecutor {
    private TimedBroadcast plugin = null;

    public BroadcastCmd(TimedBroadcast plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("usageBroadcast"));
            return true;
        }

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                helpSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                addSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                listSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                removeSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("stop")) {
                stopSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {
                startSubCommand(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("distribute")) {
                distributeSubCommand(sender);
                return true;
            }

            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("usageBroadcast"));
            return true;
        }

        return true;
    }

    private boolean permissionCheck(CommandSender sender, String permission, boolean autoMessage) {
        if (!sender.hasPermission(permission)) {
            if (autoMessage) {
                plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("noPermission"));
                return false;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void helpSubCommand(CommandSender sender) {
        plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("usageBroadcast"));
    }

    private void addSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.add", true)) {
            return;
        }

        ConversationFactory conversationFactory = new ConversationFactory(plugin);

        Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withPrefix(new MessageBuilderPrefix(plugin)).withFirstPrompt(new AddingMessageStartPrompt(plugin)).withEscapeSequence(plugin.getLanguage().getMessage("termExit")).withTimeout(plugin.getConfig().getInt("settings.timeout.messageAddition")).addConversationAbandonedListener(new MessageBuilderAbandonedListener(plugin)).buildConversation((Conversable) sender);
        conversation.begin();
    }

    private void listSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.list", true)) {
            return;
        }

        ConversationFactory conversationFactory = new ConversationFactory(plugin);

        Prompt startPrompt = null;

        switch (plugin.getMessages().size()) {
            case 0:
                startPrompt = new NoMessagesPrompt(plugin);
                break;
            default:
                startPrompt = new ListMessageStartPrompt(plugin);
                break;
        }

        Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withPrefix(new MessageListerPrefix(plugin)).withFirstPrompt(startPrompt).withEscapeSequence(plugin.getLanguage().getMessage("termExit")).withTimeout(plugin.getConfig().getInt("settings.timeout.messageRemoval")).addConversationAbandonedListener(new MessageListerAbandonedListener(plugin)).buildConversation((Conversable) sender);
        conversation.begin();
    }

    private void removeSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.remove", true)) {
            return;
        }

        ConversationFactory conversationFactory = new ConversationFactory(plugin);

        Prompt startPrompt = null;

        switch (plugin.getMessages().size()) {
            case 0:
                startPrompt = new NoMessagesPrompt(plugin);
                break;
            default:
                startPrompt = new RemoveMessageStartPrompt(plugin);
                break;
        }

        Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withPrefix(new MessageRemoverPrefix(plugin)).withFirstPrompt(startPrompt).withEscapeSequence(plugin.getLanguage().getMessage("termExit")).withTimeout(plugin.getConfig().getInt("settings.timeout.messageRemoval")).addConversationAbandonedListener(new MessageRemoverAbandonedListener(plugin)).buildConversation((Conversable) sender);
        conversation.begin();
    }

    private void stopSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.stop", true)) {
            return;
        }

        if (plugin.shouldBroadcast()) {
            plugin.shouldBroadcast(false);
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("broadcastsStopped"));
        } else {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("broadcastsAlreadyStopped"));
        }
    }

    private void startSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.start", true)) {
            return;
        }

        if (!plugin.shouldBroadcast()) {
            plugin.shouldBroadcast(true);
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("broadcastsStarted"));
        } else {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("broadcastsAlreadyStarted"));
        }
    }

    private void distributeSubCommand(CommandSender sender) {
        if (!permissionCheck(sender, plugin.getPermissionPrefix() + ".commands.distribute", true)) {
            return;
        }

        if(plugin.getLilypadConnection() == null) {
            plugin.getMessaging().sendMessage(sender, true, plugin.getLanguage().getMessage("lilypadNotFoundCanNotUse"));
            return;
        }

        ConversationFactory conversationFactory = new ConversationFactory(plugin);

        Prompt startPrompt = null;

        switch (plugin.getMessages().size()) {
            case 0:
                startPrompt = new NoMessagesPrompt(plugin);
                break;
            default:
                startPrompt = new DistributeMessageStartPrompt(plugin);
                break;
        }

        Conversation conversation = conversationFactory.withModality(true).withLocalEcho(false).withPrefix(new MessageDistributorPrefix(plugin)).withFirstPrompt(startPrompt).withEscapeSequence(plugin.getLanguage().getMessage("termExit")).withTimeout(plugin.getConfig().getInt("settings.timeout.messageDistribution")).addConversationAbandonedListener(new MessageDistributorAbandonedListener(plugin)).buildConversation((Conversable) sender);
        conversation.begin();
    }
}
