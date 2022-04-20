package com.raiden.utils.player;

import com.raiden.utils.messages.EmbedCreator;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class SpotifyClientWrapper {

    private static SpotifyClient spotifyClient = new SpotifyClient();

    public static List<AudioTrack> handleSpotifyUrl(String url, User author){
        List<String> splitted = Arrays.asList(url.split("/"));
        Collections.reverse(splitted);
        String id = splitted.get(0).split("\\?")[0];
        String type = splitted.get(1);

        switch(type){
            case "track":    return List.of(getTrack(id, author));
            case "playlist": return getPlaylist(id, author);
            case "album":    return getAlbum(id, author);
            case "artist":   return getArtistTracks(id, author);
        }
        return List.of();
    }

    public static AudioTrack getTrack(String id, User author){
        Track t = spotifyClient.getTrackDetailsById(id);
        SpotifyAudioTrack spotifyAudioTrack = new SpotifyAudioTrack(new AudioTrackInfo(t.getName() + " - " + getTrackArtists(t), getTrackArtists(t),t.getDurationMs(), t.getId(), false, t.getExternalUrls().get("spotify")));
        spotifyAudioTrack.setUserData(author);
        return spotifyAudioTrack;
    }


    public static List<AudioTrack> getPlaylist(String id, User author){
        List<Track> spotifyTracks = spotifyClient.getPlaylistTracks(id);

        List<AudioTrack> tracks = new ArrayList<>();
        for (Track t : spotifyTracks){
            SpotifyAudioTrack spotifyAudioTrack = new SpotifyAudioTrack(new AudioTrackInfo(t.getName() + " - " + getTrackArtists(t), getTrackArtists(t),t.getDurationMs(), t.getId(), false, ""));
            spotifyAudioTrack.setUserData(author);
            tracks.add(spotifyAudioTrack);
        }
        return tracks;
    }

    public static List<AudioTrack> getAlbum(String id, User author){
        List<TrackSimplified> spotifyTracks = spotifyClient.getAlbumTracks(id);

        List<AudioTrack> tracks = new ArrayList<>();
        for (TrackSimplified t : spotifyTracks) {
            SpotifyAudioTrack spotifyAudioTrack = new SpotifyAudioTrack(new AudioTrackInfo(t.getName() + " - " + getTrackArtists(t), getTrackArtists(t),t.getDurationMs(), t.getId(), false, ""));
            spotifyAudioTrack.setUserData(author);
            tracks.add(spotifyAudioTrack);
        }
        return tracks;
    }

    public static List<AudioTrack> getArtistTracks(String id, User author){
        List<Track> spotifyTracks = spotifyClient.getArtistTracks(id);

        List<AudioTrack> tracks = new ArrayList<>();
        for (Track t : spotifyTracks){
            SpotifyAudioTrack spotifyAudioTrack = new SpotifyAudioTrack(new AudioTrackInfo(t.getName() + " - " + getTrackArtists(t), getTrackArtists(t),t.getDurationMs(), t.getId(), false, ""));
            spotifyAudioTrack.setUserData(author);
            tracks.add(spotifyAudioTrack);
        }
        return tracks;
    }


    private static String getTrackArtists(Track track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
    }

    private static String getTrackArtists(TrackSimplified track){
        ArtistSimplified[] artists = track.getArtists();
        String result = "";
        for (int i = 0; i < artists.length && i < 3; i++) {
            result += artists[i].getName() + " ";
        }
        return result.trim();
    }
}
