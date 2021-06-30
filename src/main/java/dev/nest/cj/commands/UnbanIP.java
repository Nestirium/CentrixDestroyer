package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class UnbanIP extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".ipunban")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please specify an IP to unban :(").queue();
                    break;
                case 2:
                    message.getChannel().sendMessage("Please specify true if you want a force IP unban, or false for a safe IP unban.").queue();
                    message.getChannel().sendMessage("The difference is, with a force IP unban, all rows are checked matching the specified IP address, " +
                            "and are deleted, regardless of their active state, type, and expiration, it will also delete the IP from the user database. With a safe IP unban, all rows are checked matching the specified IP address," +
                            " making sure it is an IPBAN with an active state and not expired.").queue();
                    message.getChannel().sendMessage("If the safe IP unban did not work, you might want to go with a force IP unban.").queue();
                    break;
                case 3:
                    String ip = args[1];
                    String val = args[2];
                    boolean value;
                    if (val.equalsIgnoreCase("true")
                            || val.equalsIgnoreCase("false")) {
                        value = Boolean.parseBoolean(val);
                    } else {
                        message.getChannel().sendMessage("Incorrect boolean value, make sure it is either true or false.").queue();
                        return;
                    }
                    if (!StatementHandler.ipExists(ip)) {
                        message.getChannel().sendMessage("This IP does not exist in the database!").queue();
                        return;
                    }
                    if (value) {
                        message.getChannel().sendMessage("Please wait...").queue();
                        StatementHandler.unbanIP(ip, true);
                        message.getChannel().sendMessage("IP has been force unbanned... ✅").queue();
                    } else {
                        if (StatementHandler.isIPBanned(ip)) {
                            message.getChannel().sendMessage("Please wait...").queue();
                            StatementHandler.unbanIP(ip, false);
                            message.getChannel().sendMessage("IP has been unbanned... ✅").queue();
                        } else {
                            message.getChannel().sendMessage("IP is not banned...").queue();
                        }
                    }
                    break;
                default:
                    message.getChannel().sendMessage("Excessive arguments provided! :(").queue();
                    break;
            }
        }

    }

}
