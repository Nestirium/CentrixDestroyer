package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Uinfo extends ListenerAdapter {

    private boolean exists;

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".uinfo")) {
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
                    String user = args[1];
                    EmbedBuilder builder = new EmbedBuilder();
                    if (!StatementHandler.userExists(user)) {
                        message.getChannel().sendMessage("User does not exist!").queue();
                        return;
                    }
                    message.getChannel().sendMessage("Please wait...").queue();
                    MessageEmbed embed = StatementHandler.getCCoreUserData(user, builder);
                    if (embed == null) {
                        message.getChannel().sendMessage("An error occurred while displaying from CCore data, sorry.").queue();
                        return;
                    }
                    message.getChannel().sendMessage(embed).queue();
                    MessageEmbed embed2 = StatementHandler.getLPUserData(user, builder);
                    if (embed2 == null) {
                        message.getChannel().sendMessage("An error occurred while displaying from LP data, sorry.").queue();
                        return;
                    }
                    message.getChannel().sendMessage(embed2).queue();
                    break;
                default:
                    message.getChannel().sendMessage("Excess arguments provided!").queue();
                    break;
            }
        }

    }

}
