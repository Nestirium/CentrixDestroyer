package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

public class SetVotePoints extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".setvp")) {
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
                    message.getChannel().sendMessage("Please specify amount of vote points :(").queue();
                    break;
                case 3:
                    String user = args[1];
                    String amountString = args[2];
                    if (!NumberUtils.isNumber(amountString)) {
                        message.getChannel().sendMessage("Vote points must be a number.").queue();
                        return;
                    }
                    int amount = Integer.parseInt(amountString);
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User doesn't exist!").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.setVotePoints(user, amount);
                    message.getChannel().sendMessage("Vote points has been modified successfully. ✅").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided! :(").queue();
                    break;
            }
        }

    }

}
