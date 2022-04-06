package com.raiden.commands.music;

import com.raiden.LavalinkHandler;
import com.raiden.commands.utils.*;
import com.raiden.commands.utils.exceptions.VoiceChannelNullException;
import com.raiden.commands.utils.player.GuildMusicManager;
import com.raiden.commands.utils.player.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import lavalink.client.io.jda.JdaLavalink;
import lavalink.client.io.jda.JdaLink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class PlayCommand implements ICommand {
    JdaLavalink lavalink = LavalinkHandler.getLavalink();
    private final DefaultAudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    @Override
    public void handle(CommandContext ctx) {
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());


        JdaLink link = musicManager.link;

        Member member = ctx.getMember();
        VoiceChannel memberVoiceChannel = Optional.ofNullable(member.getVoiceState().getChannel())
                .orElseThrow(() -> {
                    throw new VoiceChannelNullException(channel);
                });

        if(!link.getPlayer().isConnected() || !Objects.equals(link.getChannel(), memberVoiceChannel.getId())){
            link.connect(memberVoiceChannel);
        }

        String track = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size()-1));

        PlayerManager.getInstance().loadAndPlay(ctx, track, ctx.getAuthor());
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
