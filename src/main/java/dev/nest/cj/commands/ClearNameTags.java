package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ClearNameTags extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".clearnametags")) {
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
                    String user = args[1];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User does not exist!").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.removeCurrentNameTag(user);
                    StatementHandler.clearNameTags(user);
                    message.getChannel().sendMessage("All name tags for this user are now cleared! ✅").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided!").queue();
                    break;
            }
        }

    }

}
