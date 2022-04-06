package com.raiden;

import com.raiden.commands.utils.CommandManager;
import com.raiden.utils.Config;
import com.raiden.utils.LavalinkHandler;
import lavalink.client.io.jda.JdaLavalink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class Listener extends ListenerAdapter {

    private static final CommandManager commandManager = new CommandManager();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        String message = event.getMessage().getContentRaw();
        String prefix = Config.getPrefix();

        if (user.isBot() || !message.startsWith(prefix))
            return;

        log.info(event.getMessage().getContentRaw());
        commandManager.handle(event);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        commandManager.handle(event);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        JdaLavalink lavalink = LavalinkHandler.getLavalink();
        lavalink.setUserId(event.getJDA().getSelfUser().getId());
        URI nodeUri = null;
        try {
            nodeUri = new URI(Config.getNodeAddress());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        lavalink.addNode(nodeUri, "pass");
    }
}
