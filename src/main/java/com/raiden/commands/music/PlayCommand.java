package com.raiden.commands.music;

import com.raiden.commands.utils.*;
import com.raiden.commands.utils.exceptions.VoiceChannelNullException;
import com.raiden.utils.player.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavalink.client.io.jda.JdaLink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayCommand implements ICommand {

    private final SpotifyService spotifyService = new SpotifyService();

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

        if (!isSpotifyUrl(track))
            track = "ytsearch:" + track;

        if (isSpotifyUrl(track)){
            List<String> splitted = Arrays.asList(track.split("/"));
            Collections.reverse(splitted);
            String id = splitted.get(0).split("\\?")[0];
            String type = splitted.get(1);
            if (type.equals("track")){
                Track spotifyTrack = spotifyService.getTrackDetailsById(id);
                track = spotifyTrack.getName() + " " + getTrackArtists(spotifyTrack);
            }
            else if (type.equals("playlist")){
                List<Track> tracks = spotifyService.getPlaylistTracks(id);

                musicManager.setChannel(channel);
                for (Track t : tracks){
                    SpotifyAudioTrack spotifyAudioTrack = new SpotifyAudioTrack(new AudioTrackInfo(t.getName() + " " + getTrackArtists(t), getTrackArtists(t),t.getDurationMs(), t.getId(), false, ""));
                    spotifyAudioTrack.setUserData(ctx.getAuthor());
                    musicManager.scheduler.queueTrack(spotifyAudioTrack);
                }
                return;
            }
        }


        musicManager.setChannel(channel);
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

    public String getTrackArtists(Track track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
    }

    public String getTrackArtists(TrackSimplified track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
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
