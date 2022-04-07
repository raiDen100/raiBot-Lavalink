package com.raiden.utils.player;

import com.raiden.utils.Config;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.net.URI;
import java.util.*;

public class SpotifyService {

    private SpotifyApi spotifyApi;

    public SpotifyService(){
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

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        }
        catch (Exception e){
            System.out.println(e);
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
    }//https://open.spotify.com/track/13s6dCsNpSZflwVu2uzzPi?si=cec3953e31a54ec9

    public List<Track> getPlaylistTracks(String playlistId){
        try {
            Paging<PlaylistTrack> playlistsItems = spotifyApi.getPlaylistsItems(playlistId).build().execute();

            List<PlaylistTrack> playlistItems = List.of(playlistsItems.getItems());
            List<Track> playlistTracks = new ArrayList<>();

            playlistItems.forEach((PlaylistTrack p) -> {
                Track track = (Track) p.getTrack();

                playlistTracks.add(track);
            });
            return playlistTracks;
        }
        catch (Exception e){

        }
        return null;
    }//https://open.spotify.com/playlist/1HR1Cq6ZpWplFtRWTPVBRm?si=a033384b2fc64495

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
    }//https://open.spotify.com/album/7fJJK56U9fHixgO0HQkhtI?si=Gz11-NqrRtiGm79bfjmSkg
}
