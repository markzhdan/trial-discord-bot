package Listeners;

import Main.Main;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RoleAddEvent extends ListenerAdapter
{
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {
        if(event.getRoles().get(0).getName().equalsIgnoreCase("Trial Member"))
        {
            Main.createUserDocument(event.getUser());
        }
    }
}
