package Listeners;

import Main.Main;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleRemoveEvent extends ListenerAdapter
{
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {
        if(event.getRoles().get(0).getName().equalsIgnoreCase("Trial Member"))
        {
            Main.removeUserDocument(event.getUser());
        }
    }
}
