package com.raiden.utils.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raiden.utils.messages.EmbedCreator;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.DecodedTrackHolder;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class TrackScheduler extends AudioEventListener {

    public List<AudioTrack> queue = new ArrayList<>();
    private BlockingQueue<AudioTrack> preloadQueue = new LinkedBlockingQueue<>();
    public IPlayer player;
    private final Guild guild;

    public boolean loop = false;
    public boolean loopQueue = false;


    public TrackScheduler(IPlayer player, Guild guild){
        this.player = player;
        this.guild = guild;
    }

    public void queueTrack(AudioTrack track){
        if(player.getPlayingTrack() == null){
            if (track instanceof SpotifyAudioTrack){
                track = convertSpotifyTrack(track);
            }
            player.playTrack(track);
            return;
        }
        queue.add(track);
    }

    public void queueTracks(List<AudioTrack> tracks) {
        for (AudioTrack t: tracks){
            queueTrack(t);
        }
    }

    public void nextTrack(){
        AudioTrack track = this.queue.get(0);
        queue.remove(0);
        if (track instanceof SpotifyAudioTrack){
            track = convertSpotifyTrack(track);
        }
        List<AudioTrack> tracksToPreload = queue.stream()
                .filter(t -> t instanceof SpotifyAudioTrack && !preloadQueue.contains(t))
                .limit(3)
                .collect(Collectors.toList());
        preloadQueue.addAll(tracksToPreload);

        new Thread(){
            public void run(){
                preloadSpotifyTracks();
            }
        }.start();

        this.player.playTrack(track);
    }

    public void shuffleQueue() {
        Collections.shuffle(queue);
    }

    @Override
    public void onTrackStart(IPlayer player, AudioTrack track) {

        MessageEmbed messageEmbed = EmbedCreator.nowPlayingEmbed(track, ((User)track.getUserData()).getIdLong());
        PlayerManager.getInstance().getMusicManager(guild).messageManager.sendNowPlayingMessage(messageEmbed);
    }

    @Override
    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        PlayerManager.getInstance().getMusicManager(guild).messageManager.removeLastPlayingMessage();
        if (endReason.mayStartNext){
            if (loop){
                player.playTrack(track.makeClone());
                return;
            }
            if (loopQueue){
                queue.add(track.makeClone());
            }
            if (queue.size() > 0)
                nextTrack();
        }
    }

    @Override
    public void onTrackStuck(IPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onTrackException(IPlayer player, AudioTrack track, Exception exception) {
        log.info(exception.getMessage());
    }

    synchronized private void preloadSpotifyTracks(){
        for (int i = 0; i < preloadQueue.size(); i++) {
            AudioTrack spotifyTrack = preloadQueue.poll();
            AudioTrack converted = convertSpotifyTrack(spotifyTrack);
            for (int y = 0; y < queue.size(); y++) {
                if (spotifyTrack == queue.get(y)){
                    queue.set(y, converted);
                    break;
                }
            }
        }
    }

    private AudioTrack convertSpotifyTrack(AudioTrack track){
        try {
            List<AudioTrack> searchPlaylist = PlayerManager.getInstance().getMusicManager(guild).link.getRestClient().getYoutubeSearchResult(track.getInfo().title).get();
            AudioTrack ytTrack = searchPlaylist.get(0);
            log.info("Converting: " + ytTrack.getInfo().title);
            ytTrack.setUserData(track.getUserData());
            return ytTrack;
        }catch(Exception e){

        }
        return null;
    }


}
