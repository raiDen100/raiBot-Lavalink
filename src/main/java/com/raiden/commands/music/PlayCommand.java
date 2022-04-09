package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.command.exceptions.VoiceChannelNullException;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavalink.client.io.jda.JdaLink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayCommand implements ICommand {

    private final SpotifyClientWrapper spotifyClientWrapper = new SpotifyClientWrapper();

    @Override
    public void handle(CommandContext ctx) {

        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        JdaLink link = musicManager.link;

        Member member = ctx.getMember();
        VoiceChannel memberVoiceChannel = Optional.ofNullable(member.getVoiceState().getChannel())
                .orElseThrow(() -> {
                    throw new VoiceChannelNullException(channel);
                });

        if(!link.getPlayer().isConnected() || !VoiceChecks.inChannelWith(musicManager, member)){
            link.connect(memberVoiceChannel);
        }

        String track = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size())).trim();

        musicManager.setChannel(channel);

        if (isSpotifyUrl(track)){
            List<AudioTrack> tracks = spotifyClientWrapper.handleSpotifyUrl(track, ctx.getAuthor());
            if (tracks.size() > 1){
                MessageEmbed messageEmbed = EmbedCreator.queuedPlaylistEmbed(tracks.size());
                channel.sendMessageEmbeds(messageEmbed).queue();
            }
            else{
                if (musicManager.audioPlayer.getPlayingTrack() != null){
                    MessageEmbed messageEmbed = EmbedCreator.queuedTrackEmbed(tracks.get(0), ctx.getAuthor().getIdLong());
                    channel.sendMessageEmbeds(messageEmbed).queue();
                }
            }
            musicManager.scheduler.queueTracks(tracks);
            return;
        }

        if (!isValid(track))
            track = "ytsearch:" + track;

        PlayerManager.getInstance().loadAndPlay(ctx, track, ctx.getAuthor());
    }

    public static boolean isSpotifyUrl(String url){
        if(isValid(url) && url.contains("open.spotify")){
            return true;
        }
        return false;
    }

    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
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
