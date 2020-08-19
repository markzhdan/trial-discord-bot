package Listeners;

import Main.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter
{
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
    }
}
