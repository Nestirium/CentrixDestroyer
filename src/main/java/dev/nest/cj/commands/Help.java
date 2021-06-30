package dev.nest.cj.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Help extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".help")) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Commands");
            builder.setColor(Color.GREEN);
            builder.addField(".uinfo (name)", "Retrieves centrix player information", true);
            builder.addField(".listgroups", "Lists all the ranks on centrix", true);
            builder.addField(".ban (name) (reason)", "Bans a player on centrix by the name of BrotherHate", true);
            builder.addField(".mute (name) (reason)", "Mutes a player on centrix by the name of jooster11", true);
            builder.addField(".unban (name)", "Unbans a player on centrix by removing the whole ban without leaving a trace.", true);
            builder.addField(".unmute (name)", "Unmutes a player on centrix by removing the whole mute without leaving a trace.", true);
            builder.addField(".addtag (name) (tag)", "Gives a player on centrix a chat tag", true);
            builder.addField(".addnametag (name) (tag)", "Gives a player on centrix a name tag", true);
            builder.addField(".nuke (code)", "Nukes the whole database of centrix, and shuts it down, requires secret code to activate", true);
            builder.addField(".checkperm (user) (perm) (true/false) (server)", "Checks if a user has a specified permission on a specified server or global.", true);
            builder.addField(".addperm (user) (perm) (true/false) (server)", "Adds a permission to a user.", true);
            builder.addField(".removeperm (user) (perm) (server)", "Removes a permission from a user.", true);
            builder.addField(".setprimary (user) (group)", "Sets the primary rank for a user.", true);
            builder.addField(".cleartags (user)", "Removes all the chat tags for a user.", true);
            builder.addField(".clearnametags (user)", "Removes all the name tags for a user.", true);
            builder.addField(".settag (user) (tag)", "Sets the current chat tag for a user.", true);
            builder.addField(".setnametag (user) (tag)", "Sets the current name tag for a user.", true);
            builder.addField(".toggleadmin (user)", "Toggles admin bypass for a user. CCore will automatically ban someone with admin+ rank, but without database access.", true);
            builder.addField(".ipunban (ip) (true/false)", "Unbans an IP, useful when using VPNs with banned IP addresses.", true);
            builder.addField(".sql (centrixcore/LuckPerms) (statement)", "Executes a custom SQL query. (Advanced)", true);
            builder.addField(".connect", "Establishes a connection with the databases.", true);
            builder.addField(".dc", "Terminates all database connections.", true);
            builder.addField(".setvp (user) (amount)", "Sets vote points for a specified user.", true);
            builder.setFooter("(c) Nestirium - تم الدعس");
            message.getChannel().sendMessage(builder.build()).queue();
            builder.clear();
        }

    }

}

