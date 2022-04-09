package com.raiden.utils.player;

import com.raiden.utils.Config;
import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.net.URI;
import java.util.*;

@Slf4j
public class SpotifyClient {

    private SpotifyApi spotifyApi;

    public SpotifyClient(){
        getAccessToken();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getAccessToken();
            }
        }, 3590 * 1000, 3590 * 1000);
    }

    private void getAccessToken(){
        try{
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId(Config.getSpotifyId())
                    .setClientSecret(Config.getSpotifySecret())
                    .setRedirectUri(new URI(Config.getSpotifyRedirect()))
                    .build();
            ClientCredentials clientCredentials = spotifyApi.clientCredentials().build().execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            log.info("Received access token");
        }
        catch (Exception e){
            log.info("Getting access token failed");
            log.info(e.getMessage());
        }
    }

    public Track getTrackDetailsById(String trackId){
        try {
            Track track = spotifyApi.getTrack(trackId).build().execute();

            return track;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Track> getPlaylistTracks(String playlistId) {
        try {
            Paging<PlaylistTrack> playlistsItems = spotifyApi.getPlaylistsItems(playlistId).build().execute();

            List<PlaylistTrack> playlistItems = List.of(playlistsItems.getItems());
            List<Track> playlistTracks = new ArrayList<>();

            playlistItems.forEach((PlaylistTrack p) -> {
                Track track = (Track) p.getTrack();

                playlistTracks.add(track);
            });
            return playlistTracks;
        } catch (Exception e) {

        }
        return null;
    }

    public List<TrackSimplified> getAlbumTracks(String albumId){
        try {
            Paging<TrackSimplified> albumTracks = spotifyApi.getAlbumsTracks(albumId).build().execute();

            List<TrackSimplified> tracks = new ArrayList<>();
            tracks.addAll(Arrays.asList(albumTracks.getItems()));
            return tracks;
        }
        catch (Exception e){

        }
        return null;
    }
}
