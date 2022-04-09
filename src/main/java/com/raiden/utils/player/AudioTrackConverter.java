package com.raiden.utils.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavalink.client.io.jda.JdaLink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AudioTrackConverter {

    private final Guild guild;
    private Thread converterThread;

    public AudioTrackConverter(Guild guild) {
        this.guild = guild;
        this.converterThread = new Thread();
    }

   public void convertQueue(List<AudioTrack> queue){
        if (!converterThread.isAlive()){
            converterThread = new Thread(() -> convertTracksInQueue(queue));
            converterThread.setName("SpotifyConverter-" + guild.getId());
            converterThread.start();
        }
    }

    public void convertTracksInQueue(List<AudioTrack> queue){
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i) instanceof SpotifyAudioTrack){

                AudioTrack converted = convertSpotifyTrack(queue.get(i));
                queue.set(i, converted);
            }
        }
    }

    public AudioTrack convertSpotifyTrack(AudioTrack track){
        try {
            List<AudioTrack> searchPlaylist = PlayerManager.getInstance().getMusicManager(guild).link.
                    getRestClient().getYoutubeSearchResult(track.getInfo().title)
                    .get();
            AudioTrack ytTrack = searchPlaylist.get(0);
            log.info("Converting: " + ytTrack.getInfo().title);
            ytTrack.setUserData(track.getUserData());
            return ytTrack;
        }catch(Exception e){
            log.info("Track conversion failed: " + e.getMessage());
        }
        return null;
    }
}
