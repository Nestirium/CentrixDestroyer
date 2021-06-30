package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class Connect extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".connect")) {
            message.getChannel().sendMessage("Please wait...").queue();
            message.getChannel().sendMessage("Attempting to establish connection with LuckPerms database..").queue();
            if (!StatementHandler.isIsLuckPermsEstablished()) {
                StatementHandler.establishLuckPermsPool();
                message.getChannel().sendMessage("LuckPerms database connection established successfully.").queue();
            } else {
                message.getChannel().sendMessage("LuckPerms database connection already established.").queue();
            }
            message.getChannel().sendMessage("Attempting to establish connection with CentrixCore database..").queue();
            if (!StatementHandler.isIsCCoreEstablished()) {
                StatementHandler.establishCCorePool();
                message.getChannel().sendMessage("CentrixCore database connection established successfully.").queue();
            } else {
                message.getChannel().sendMessage("CentrixCore database connection already established.").queue();
            }
        }

    }

}
