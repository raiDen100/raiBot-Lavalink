package com.raiden.utils.player;

import com.raiden.utils.messages.EmbedCreator;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class TrackScheduler extends AudioEventListener {

    public BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
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
            player.playTrack(track);
            return;
        }
        queue.offer(track);
    }

    public void nextTrack(){
        AudioTrack track = this.queue.poll();
        this.player.playTrack(track);
    }

    public void shuffleQueue() {
        List<AudioTrack> queueTracks = new ArrayList<>();
        queue.drainTo(queueTracks);
        Collections.shuffle(queueTracks);

        queue.addAll(queueTracks);
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
                queueTrack(track.makeClone());
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
}
