package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Unban extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".unban")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please specify user to unfuck :(").queue();
                    break;
                case 2:
                    String user = args[1];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User doesn't exist!").queue();
                        return;
                    }
                    if (StatementHandler.isUserBanned(user)) {
                        message.getChannel().sendMessage("Please wait...").queue();
                        StatementHandler.unban(user);
                        message.getChannel().sendMessage("User has been unbanned... ✅").queue();
                    } else {
                        message.getChannel().sendMessage("User is not banned...").queue();
                    }
                    if (StatementHandler.isUserIPBanned(user)) {
                        message.getChannel().sendMessage("Detected IP Ban, attempting to remove it...").queue();
                        StatementHandler.unbanUserIP(user);
                        message.getChannel().sendMessage("User IP has been unbanned.. ✅").queue();
                    } else {
                        message.getChannel().sendMessage("User is not IP banned...").queue();
                    }
                    break;
                default:
                    message.getChannel().sendMessage("Excessive arguments provided! :(").queue();
                    break;
            }
        }

    }


}
