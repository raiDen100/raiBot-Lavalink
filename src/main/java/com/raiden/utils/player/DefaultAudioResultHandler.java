package com.raiden.utils.player;

import com.raiden.utils.messages.EmbedCreator;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

@Slf4j
public class DefaultAudioResultHandler implements AudioLoadResultHandler {


    private final User author;
    private final GuildMusicManager musicManager;
    private final TrackScheduler scheduler;

    public DefaultAudioResultHandler(User author, GuildMusicManager musicManager) {
        this.author = author;
        this.musicManager = musicManager;
        this.scheduler = musicManager.scheduler;
    }

    @Override
    public void trackLoaded(AudioTrack track) {

        track.setUserData(author);
        if (musicManager.audioPlayer.getPlayingTrack() != null){
            MessageEmbed messageEmbed = EmbedCreator.queuedTrackEmbed(track, author.getIdLong());
            musicManager.messageManager.sendQueueMessage(messageEmbed);
        }

        scheduler.queueTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {

        if (audioPlaylist.isSearchResult()){

            AudioTrack track = audioPlaylist.getTracks().stream()
                    .findFirst()
                    .orElseThrow();

            if (musicManager.audioPlayer.getPlayingTrack() != null ){

                MessageEmbed messageEmbed = EmbedCreator.queuedTrackEmbed(track, author.getIdLong());
                musicManager.messageManager.sendQueueMessage(messageEmbed);
            }

            track.setUserData(author);
            scheduler.queueTrack(track);
            return;
        }

        List<AudioTrack> tracks = audioPlaylist.getTracks();

        MessageEmbed messageEmbed = EmbedCreator.queuedPlaylistEmbed(tracks.size());
        musicManager.messageManager.sendQueueMessage(messageEmbed);

        for(AudioTrack track : tracks){
            track.setUserData(author);
            scheduler.queueTrack(track);
        }
    }

    @Override
    public void noMatches() {
    }

    @Override
    public void loadFailed(FriendlyException e) {
        log.info(e.getMessage());
    }
}
