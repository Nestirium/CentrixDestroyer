package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class CheckPerm extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".checkperm")) {
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
                    message.getChannel().sendMessage("Please specify a permission node to check.").queue();
                    break;
                case 3:
                    message.getChannel().sendMessage("Please specify a boolean value, true or false.").queue();
                    break;
                case 4:
                    message.getChannel().sendMessage("Please specify a server, if globally, type global.").queue();
                    break;
                case 5:
                    String user = args[1];
                    String permission = args[2];
                    String val = args[3];
                    boolean value;
                    if (val.equalsIgnoreCase("true")
                    || val.equalsIgnoreCase("false")) {
                        value = Boolean.parseBoolean(val);
                    } else {
                        message.getChannel().sendMessage("Incorrect boolean value, make sure it is either true or false.").queue();
                        return;
                    }
                    String server = args[4];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User does not exist!").queue();
                        return;
                    }
                    if (StatementHandler.hasPermission(user, permission, server, value)) {
                        message.getChannel().sendMessage("User has this permission! ✅").queue();
                        return;
                    }
                    message.getChannel().sendMessage("User does not have this permission!").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided!").queue();
                    break;
            }
        }

    }

}
