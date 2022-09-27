package Listeners;

import Main.Main;
import Utility.Utility;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class Commands extends ListenerAdapter
{
    Utility util = new Utility();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        String[] args = event.getMessage().getContentRaw().split(" ");
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if(args[0].equalsIgnoreCase(Main.PREFIX + "triallength"))
        {
            if(member.isOwner())
            {
                Main.setLength("trial", ((Integer.parseInt(args[1])) * 86400000));
                event.getChannel().sendMessage("Trial length set to " + args[1] + " days").queue();
            }
            else
            {
                event.getChannel().sendMessage("You must be the server owner to use this command!").queue();
            }
        }
        else if(args[0].equalsIgnoreCase(Main.PREFIX + "votelength"))
        {
            if(member.isOwner())
            {
                Main.setLength("vote", ((Integer.parseInt(args[1])) * 86400000));
                event.getChannel().sendMessage("Vote length set to " + args[1] + " days").queue();
            }
            else
            {
                event.getChannel().sendMessage("You must be the server owner to use this command!").queue();
            }
        }
        else if(args[0].equalsIgnoreCase(Main.PREFIX + "amongus"))
        {
            if(util.findRole(member, "Among Us") == null)
            {
                if(util.roleExists(event.getGuild().getRoles(), "Among Us"))
                {
                    guild.addRoleToMember(member, event.getGuild().getRolesByName("Among Us", true).get(0)).queue();
                }
                else
                {
                    guild.createRole().setName("Among Us").setColor(Color.WHITE).setMentionable(true).queue(role ->
                            guild.addRoleToMember(member, role).queue());
                }
                event.getChannel().sendMessage("Among Us tag given").queue();
            }
            else
            {
                guild.removeRoleFromMember(member, event.getGuild().getRolesByName("Among Us", true).get(0)).queue();
                event.getChannel().sendMessage("Among Us tag removed").queue();
            }
        }
        else if(args[0].equalsIgnoreCase(Main.PREFIX + "vote"))
        {
            if((util.findRole(member, "Admin") != null) || (util.findRole(member, "HR: Mod") != null))
            {
                Member mentionedMember = event.getMessage().getMentionedMembers().get(0);
                Main.createUserDocument(mentionedMember.getUser());
                Main.updateJoinMilisecond(mentionedMember.getUser());
                event.getChannel().sendMessage("Vote started for **" + mentionedMember.getEffectiveName() + "**").queue();
            }
            else
            {
                event.getChannel().sendMessage("You don't have required permissions!").queue();
            }
        }
        else if(args[0].equalsIgnoreCase(Main.PREFIX + "joined"))
        {
            Member mentionedMember = event.getMessage().getMentionedMembers().get(0);
            OffsetDateTime time = mentionedMember.getTimeJoined();
            event.getChannel().sendMessage(mentionedMember.getEffectiveName() + " joined: " + time.getMonthValue() + "/" + time.getDayOfMonth() + "/" + time.getYear() + "\n*(" + time.until(OffsetDateTime.now(), ChronoUnit.DAYS) + " days ago)*").queue();
        }
        else if(args[0].equalsIgnoreCase(Main.PREFIX + "test"))
        {
            try {
                guild.removeRoleFromMember(member, guild.getRolesByName("Medieval", true).get(0)).queue();
            }
            catch(Exception e) {
                try {
                    guild.removeRoleFromMember(member, guild.getRolesByName("Dwarf", true).get(0)).queue();
                }
                catch(Exception e1) {
                    try {
                        guild.removeRoleFromMember(member, guild.getRolesByName("Modern", true).get(0)).queue();
                    }
                    catch(Exception e2) {
                        try {
                            guild.removeRoleFromMember(member, guild.getRolesByName("Oriental", true).get(0)).queue();
                        }
                        catch(Exception e3) {
                            try {
                                guild.removeRoleFromMember(member, guild.getRolesByName("Wild", true).get(0)).queue();
                            }
                            catch(Exception e4) {
                                try {
                                    guild.removeRoleFromMember(member, guild.getRolesByName("Viking", true).get(0)).queue();
                                }
                                catch(Exception e5) {
                                    System.out.println(e5);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
