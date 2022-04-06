package com.raiden;

import com.raiden.commands.utils.CommandManager;
import com.raiden.utils.Config;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavalink.client.io.Link;
import lavalink.client.io.jda.JdaLavalink;
import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class Listener extends ListenerAdapter {

    private final DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();
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
    public void onReady(@NotNull ReadyEvent event) {
        JdaLavalink lavalink = LavalinkHandler.getLavalink();
        lavalink.setUserId(event.getJDA().getSelfUser().getId());
        URI nodeUri = null;
        try {
            nodeUri = new URI("ws://localhost:6969");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        lavalink.addNode(nodeUri, "pass");
    }
}
