package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SetNameTag extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".setnametag")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please specify user :(").queue();
                    break;
                case 2:
                    message.getChannel().sendMessage("Please specify a tag :(").queue();
                    break;
                case 3:
                    String user = args[1];
                    String tag = args[2];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User doesn't exist!").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.setNameTag(user, tag);
                    message.getChannel().sendMessage("Name tag has been set successfully... ✅").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided! :(").queue();
                    break;
            }
        }

    }

}
