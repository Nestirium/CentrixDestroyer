package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Nuke extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".nuke")) {
            if (!StatementHandler.isIsCCoreEstablished() || !StatementHandler.isIsLuckPermsEstablished()) {
                message.getChannel().sendMessage("Connection to database is not established, please use .connect to establish a connection!").queue();
                return;
            }
            String[] args = content.split(" ");
            switch (args.length) {
                case 1:
                    message.getChannel().sendMessage("Please enter nuke launch code :)").queue();
                    break;
                case 2:
                    String code = args[1];
                    if (!StatementHandler.validateCode(code)) {
                        message.getChannel().sendMessage("Invalid launch key! :(").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    StatementHandler.nuke();
                    message.getChannel().sendMessage("Centrix has been nuked. Databases are now wiped, centrix has been shutdown. I will shutdown too... BYE ✅").queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided!").queue();
                    break;
            }
        }

    }

}
