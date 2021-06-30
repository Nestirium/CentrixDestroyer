package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SetPrimary extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".setprimary")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please specify a user :(").queue();
                    break;
                case 2:
                    message.getChannel().sendMessage("Please specify a group.").queue();
                    break;
                case 3:
                    String user = args[1];
                    String group = args[2];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User does not exist!").queue();
                        return;
                    }
                    if (!StatementHandler.doesGroupExist(group)) {
                        message.getChannel().sendMessage("This group does not exist!").queue();
                        return;
                    }
                    if (StatementHandler.hasPrimaryGroup(user, group)) {
                        message.getChannel().sendMessage("User already has this group!").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.setPrimaryGroup(user, group);
                    message.getChannel().sendMessage("Primary group has been modified! ✅").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided!").queue();
                    break;
            }
        }

    }

}
