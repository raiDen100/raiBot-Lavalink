package com.raiden.utils.player;

import com.raiden.utils.messages.MessageManager;
import lavalink.client.io.filters.Filters;
import lavalink.client.io.filters.Karaoke;
import lavalink.client.io.filters.Timescale;
import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.IPlayer;
import lavalink.client.player.LavalinkPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.TextChannel;

@Slf4j
public class GuildMusicManager {
    public IPlayer audioPlayer;
    public TrackScheduler scheduler;
    public JdaLink link;

    public MessageManager messageManager = new MessageManager();
    private final Filters filters;

    private final float[] BASS_BOOST = {-0.05F, 0.07F, 0.16F, 0.03F};

    public GuildMusicManager(JdaLink link) {
        this.link = link;
        this.audioPlayer = link.getPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, link.getGuild());
        this.audioPlayer.addListener(scheduler);
        messageManager.setTrackScheduler(scheduler);
        filters = ((LavalinkPlayer)audioPlayer).getFilters();
    }
    public void setBassboost(float value){

        for (int i = 0; i < BASS_BOOST.length; i++) {
           filters.setBand(i, BASS_BOOST[i] * value);
        }
        filters.commit();
    }

    public void setVolume(float value){
        filters.setVolume(value);
        filters.commit();
    }

    public void resetVolume() {
        filters.setVolume(1);
        filters.commit();
    }
    public void resetBassboost(){
        for (int i = 0; i < BASS_BOOST.length; i++) {
            filters.setBand(i, 0);
        }
        filters.commit();
    }

    public void resetFilters() {
        filters.clear();
        filters.commit();
    }

    public void setChannel(TextChannel channel) {
        messageManager.setChannel(channel);
    }


    public void setSpeed(float speed) {
        Timescale timescale = new Timescale();
        timescale.setSpeed(speed);
        filters.setTimescale(timescale);
        filters.commit();
    }

    public void resetSpeed() {
        filters.setTimescale(null);
        filters.commit();
    }
}
