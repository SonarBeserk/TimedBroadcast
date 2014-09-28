package me.sonarbeserk.timedbroadcast.conversations.messagedistribution.prompts;

import me.sonarbeserk.timedbroadcast.TimedBroadcast;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

/**
 * Created by SonarBeserk on 9/27/2014.
 */
public class DistributeMessageStartPrompt implements Prompt {

    public DistributeMessageStartPrompt(TimedBroadcast plugin) {

    }

    @Override
    public String getPromptText(ConversationContext context) {
        return null;
    }

    @Override
    public boolean blocksForInput(ConversationContext context) {
        return false;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String s) {
        return null;
    }
}
