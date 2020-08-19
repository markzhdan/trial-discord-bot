package Listeners;

import Main.Main;
import Utility.Utility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class userJoin extends ListenerAdapter
{
    Utility util = new Utility();

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        Guild guild = event.getGuild();

        if(util.roleExists(event.getGuild().getRoles(), "Trial Member"))
        {
            guild.addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Trial Member", true).get(0)).queue();

        }
        else
        {
            guild.createRole().setName("Trial Member").setColor(Color.RED).queue(role ->
                    guild.addRoleToMember(event.getMember(), role).queue());
        }
        Main.createUserDocument(event.getUser());

    }
}
