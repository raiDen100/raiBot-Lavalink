package com.raiden.commands.utils.player;

import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.IPlayer;

public class GuildMusicManager {
    public IPlayer audioPlayer;
    public TrackScheduler scheduler;
    public JdaLink link;


    public GuildMusicManager(JdaLink link) {
        this.link = link;
        this.audioPlayer = link.getPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(scheduler);
    }
}
