package Main;

import Listeners.Commands;
import Listeners.userJoin;
import Listeners.userLeaves;
import Listeners.voteReaction;
import Utility.Timer;
import Utility.Utility;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main
{
    public static Timer timer = new Timer();
    public static Utility util = new Utility();
    public static JDA jda;
    public static String PREFIX = "*";
    private static MongoCollection<Document> userData;
    private static MongoCollection<Document> timeData;
    private static long trialLengthMilliseconds = 1209600000;
    private static long voteLengthMilliseconds = 86400000;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault("").enableIntents(GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).setActivity(Activity.playing("Morven SMP")).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);

        jda.addEventListener(new userJoin());
        jda.addEventListener(new userLeaves());
        jda.addEventListener(new voteReaction());
        jda.addEventListener(new Commands());

        MongoClient mongoClient = MongoClients.create("mongodb+srv://Cuft:Cuft@morvencosmetics.alnd1.mongodb.net/<dbname>?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MorvenCosmetics");
        userData = database.getCollection("discordBot");
        timeData = database.getCollection("discordBotTimes");

        updateLength();

        timer.startTimer();
    }

    public static Document getUserDoc(User user)
    {
        return userData.find(new Document("discordID", user.getId())).first();
    }

    public static void updateTrialBoolean(User user, boolean bool)
    {
        Document doc = getUserDoc(user);

        if(doc != null)
        {
            Bson updatedValue = new Document("isOnTrial", bool);
            Bson updatedOperation = new Document("$set", updatedValue);
            userData.updateOne(doc, updatedOperation);
        }
    }

    public static void updateVoteBoolean(User user, boolean bool)
    {
        Document doc = getUserDoc(user);

        if(doc != null)
        {
            Bson updatedValue = new Document("isOnVote", bool);
            Bson updatedOperation = new Document("$set", updatedValue);
            userData.updateOne(doc, updatedOperation);
        }
    }

    public static void yesVote(User user, boolean remove)
    {
        Document doc = getUserDoc(user);

        if(doc != null)
        {
            Bson updatedValue;
            if(remove)
            {
                updatedValue = new Document("voteYES", doc.getInteger("voteYES") - 1);
            }
            else
            {
                updatedValue = new Document("voteYES", doc.getInteger("voteYES") + 1);
            }

            Bson updatedOperation = new Document("$set", updatedValue);
            userData.updateOne(doc, updatedOperation);
        }
    }

    public static void noVote(User user, boolean remove)
    {
        Document doc = getUserDoc(user);

        if(doc != null)
        {
            Bson updatedValue;
            if(remove)
            {
                updatedValue = new Document("voteNO", doc.getInteger("voteNO") - 1);
            }
            else
            {
                updatedValue = new Document("voteNO", doc.getInteger("voteNO") + 1);
            }

            Bson updatedOperation = new Document("$set", updatedValue);
            userData.updateOne(doc, updatedOperation);
        }
    }

    public static void setLength(String trialOrVote, int time)
    {
        Document doc = new Document("timeData", "timeData");
        Bson updatedValue;

        if(trialOrVote.equalsIgnoreCase("trial"))
        {
            updatedValue = new Document("trialLengthMilliseconds", time);
            trialLengthMilliseconds = time;
        }
        else
        {
            updatedValue = new Document("voteLengthMilliseconds", time);
            voteLengthMilliseconds = time;
        }

        Bson updatedOperation = new Document("$set", updatedValue);
        timeData.updateOne(doc, updatedOperation);
    }

    public static void updateLength()
    {
        Document timeDoc = new Document("timeData", "timeData");
        Document fnd = timeData.find(timeDoc).first();
        trialLengthMilliseconds = fnd.getLong("trialLengthMilliseconds");
        voteLengthMilliseconds = fnd.getLong("voteLengthMilliseconds");
    }

    public static void createUserDocument(User user)
    {
        Document userDoc = new Document("discordID", user.getId());
        Document found = userData.find(userDoc).first();

        if(found == null)
        {
            userDoc.append("username", user.getName());
            userDoc.append("timeJoined", System.currentTimeMillis());
            userDoc.append("voteYES", 0);
            userDoc.append("voteNO", 0);
            userDoc.append("isOnTrial", true);
            userDoc.append("isOnVote", false);
            userData.insertOne(userDoc);
        }
    }

    public static void removeUserDocument(User user)
    {
        Document userDoc = new Document("discordID", user.getId());
        Document found = userData.find(userDoc).first();

        if(found != null)
        {
            userData.deleteOne(new Document("discordID", user.getId()));
        }
    }

    public static void startVote(User user, Guild guild)
    {
        guild.getTextChannelsByName("trial-members", true).get(0).sendMessage(user.getName() + " - " + user.getAsMention()).queue(new Consumer<Message>() {
            @Override
            public void accept(Message message) {
                //Check
                message.addReaction("👑").queue();
                //X
                message.addReaction("👢").queue();
            }
        });

        Document doc = getUserDoc(user);

        if(doc != null)
        {
            Bson updatedValue = new Document("voteStarted", System.currentTimeMillis());
            Bson updatedOperation = new Document("$set", updatedValue);
            userData.updateOne(doc, updatedOperation);
        }
    }

    public static void userAccepted(User user, Guild guild, int yes, int no)
    {
        EmbedBuilder accepted  = new EmbedBuilder();
        Member member = guild.getMember(user);
        guild.removeRoleFromMember(member, guild.getRolesByName("Trial Member", true).get(0)).queue();

        if(util.roleExists(guild.getRoles(), "Member"))
        {
            guild.addRoleToMember(member, guild.getRolesByName("Member", true).get(0)).queue();
        }
        else
        {
            guild.createRole().setName("Member").setColor(Color.MAGENTA).queue(role ->
                    guild.addRoleToMember(member, role).queue());
        }

        accepted.setDescription(member.getAsMention() + " **Accepted**");
        accepted.setColor(Color.green);
        accepted.addField("Yes", "" + yes, true);
        accepted.addField("No", "" + no, true);
        accepted.addField("Percentage", "" + ((double) yes / (yes+no)) * 100 + "%", false);

        guild.getTextChannelsByName("trial-results", true).get(0).sendMessage(accepted.build()).queue();
    }

    public static void userDenied(User user, Guild guild, int yes, int no)
    {
        EmbedBuilder denied  = new EmbedBuilder();
        Member member = guild.getMember(user);
        guild.removeRoleFromMember(member, guild.getRolesByName("Trial Member", true).get(0)).queue();

        denied.setDescription(member.getAsMention() + " **Denied**");
        denied.setColor(Color.red);
        denied.addField("Yes", "" + yes, true);
        denied.addField("No", "" + no, true);
        denied.addField("Percentage", "" + ((double) yes / (yes+no)) * 100 + "%", false);

        guild.getTextChannelsByName("trial-results", true).get(0).sendMessage(denied.build()).queue();
    }

    public static void compareTimes()
    {
        jda.getGuilds().get(0).loadMembers();
        List<Document> users = userData.find().into(new ArrayList<Document>());

        for(Document usr : users)
        {
            //Trial Time
            if((((System.currentTimeMillis() - usr.getLong("timeJoined"))) > trialLengthMilliseconds) && usr.getBoolean("isOnTrial"))
            {
                User user = jda.getUserById(usr.getString("discordID"));
                Guild guild = jda.getGuildById("753059606707830794");

                if(user != null)
                {
                    startVote(user, guild);
                    updateTrialBoolean(user, false);
                    updateVoteBoolean(user, true);
                }
            }

            if(usr.getBoolean("isOnVote"))
            {
                if(((System.currentTimeMillis() - usr.getLong("voteStarted"))) > voteLengthMilliseconds)
                {
                    User user = jda.getUserById(usr.getString("discordID"));
                    Guild guild = jda.getGuildById("753059606707830794");

                    if(user != null)
                    {
                        Document doc = getUserDoc(user);
                        int yes = doc.getInteger("voteYES");
                        int no = doc.getInteger("voteNO");
                        if(yes == 0 && no == 0)
                        {
                            no++;
                        }
                        if((double) yes / (yes+no) >= 0.5)
                        {
                            userAccepted(user, guild, yes, no);
                        }
                        else
                        {
                            userDenied(user, guild, yes, no);
                        }
                        userData.deleteOne(getUserDoc(user));
                    }
                }
            }
        }
    }
}
