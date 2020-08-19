package Listeners;

import Main.Main;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class voteReaction extends ListenerAdapter
{
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
    {
        if(event.getChannel().getName().equalsIgnoreCase("trial-members") && !event.getMember().getUser().equals(event.getJDA().getSelfUser()))
        {
            Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();

            //YES Vote
            if(event.getReactionEmote().getName().equals("👑"))
            {
                Main.yesVote(message.getMentionedUsers().get(0), false);
            }
            //NO Vote
            else if(event.getReactionEmote().getName().equals("👢"))
            {
                Main.noVote(message.getMentionedUsers().get(0), false);
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event)
    {
        if(event.getChannel().getName().equalsIgnoreCase("trial-members") && !event.getMember().getUser().equals(event.getJDA().getSelfUser()))
        {
            Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();

            //YES Vote
            if(event.getReactionEmote().getName().equals("👑"))
            {
                Main.yesVote(message.getMentionedUsers().get(0), true);
            }
            //NO Vote
            else if(event.getReactionEmote().getName().equals("👢"))
            {
                Main.noVote(message.getMentionedUsers().get(0), true);
            }
        }
    }
}
