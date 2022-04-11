package com.raiden.utils.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.User;

public class PlayNowResultHandler implements AudioLoadResultHandler {

    private final User author;
    private final GuildMusicManager musicManager;
    private final TrackScheduler scheduler;

    public PlayNowResultHandler(User author, GuildMusicManager musicManager) {
        this.author = author;
        this.musicManager = musicManager;
        this.scheduler = musicManager.scheduler;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        track.setUserData(author);
        musicManager.scheduler.insertTrack(0, track, true);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.isSearchResult()){
            AudioTrack track = audioPlaylist.getTracks().get(0);
            track.setUserData(author);
            musicManager.scheduler.insertTrack(0, track, true);
        }
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
