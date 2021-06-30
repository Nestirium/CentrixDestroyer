package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ban extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".ban")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = ldt.format(formatter);
            String user, reason;
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please specify user to fuck :(").queue();
                    break;
                case 2:
                    message.getChannel().sendMessage("Please specify a reason, centrix staff is pro we do not ban without reason :(").queue();
                    break;
                case 3:
                    user = args[1];
                    reason = args[2];
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User doesn't exist!").queue();
                        return;
                    }
                    if (StatementHandler.isUserBanned(user)) {
                        message.getChannel().sendMessage("User is already banned.").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.ban(user, date, reason);
                    message.getChannel().sendMessage("User has been banned successfully! ✅").queue();
                    break;
                default:
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]).append(" ");
                    }
                    user = args[1];
                    reason = builder.toString();
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.ban(user, date, reason);
                    message.getChannel().sendMessage("User has been banned successfully! ✅").queue();
                    break;
            }
        }

    }


}
