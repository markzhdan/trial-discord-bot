package Listeners;

import Main.Main;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class userLeaves extends ListenerAdapter
{
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {
        Main.removeUserDocument(event.getUser());
    }
}
