package com.raiden.commands.utils.player;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TrackScheduler extends AudioEventListener {

    public final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    public IPlayer player;

    public TrackScheduler(IPlayer player){
        this.player = player;
    }

    public void queueTrack(AudioTrack track){
        if(player.getPlayingTrack() == null){
            player.playTrack(track);
            return;
        }
        queue.offer(track);
    }

    public void nextTrack(){
        AudioTrack track = this.queue.poll();
        this.player.playTrack(track);
    }

    @Override
    public void onTrackStart(IPlayer player, AudioTrack track) {
        log.info("Starting track: " + track.getInfo().title);
    }

    @Override
    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext){
            nextTrack();
        }
    }

    @Override
    public void onTrackStuck(IPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onTrackException(IPlayer player, AudioTrack track, Exception exception) {
        super.onTrackException(player, track, exception);
    }
}
