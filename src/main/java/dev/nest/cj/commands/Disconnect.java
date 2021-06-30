package dev.nest.cj.commands;

import dev.nest.cj.StatementHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Disconnect extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        final Message message = event.getMessage();
        final String content = message.getContentRaw();
        if (content.startsWith(".dc")) {
            message.getChannel().sendMessage("Please wait...").queue();
            message.getChannel().sendMessage("Attempting to disconnect connection with LuckPerms database..").queue();
            if (StatementHandler.isIsLuckPermsEstablished()) {
                StatementHandler.disconnectLuckPermsPool();
                message.getChannel().sendMessage("LuckPerms database connection disconnected.").queue();
            } else {
                message.getChannel().sendMessage("LuckPerms database connection already disconnected.").queue();
            }
            message.getChannel().sendMessage("Attempting to disconnect connection with CentrixCore database..").queue();
            if (StatementHandler.isIsCCoreEstablished()) {
                StatementHandler.disconnectCCorePool();
                message.getChannel().sendMessage("CentrixCore database connection disconnected.").queue();
            } else {
                message.getChannel().sendMessage("CentrixCore database connection already disconnected.").queue();
            }
        }

    }

}
