package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class Unmute extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".unmute")) {
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
                    if (StatementHandler.isUserMuted(user)) {
                        message.getChannel().sendMessage("Please wait...").queue();
                        StatementHandler.unmute(user);
                        message.getChannel().sendMessage("User has been unmuted... ✅").queue();
                    } else {
                        message.getChannel().sendMessage("User is not muted...").queue();
                    }
                    break;
                default:
                    message.getChannel().sendMessage("Excessive arguments provided! :(").queue();
                    break;
            }
        }
    }
}
