package com.raiden.utils.player;

import com.raiden.commands.utils.MessageManager;
import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.TextChannel;

public class GuildMusicManager {
    public IPlayer audioPlayer;
    public TrackScheduler scheduler;
    public JdaLink link;

    public MessageManager messageManager = new MessageManager();

    public GuildMusicManager(JdaLink link) {
        this.link = link;
        this.audioPlayer = link.getPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer, link.getGuild());
        this.audioPlayer.addListener(scheduler);
        messageManager.setTrackScheduler(scheduler);
    }



    public void setChannel(TextChannel channel) {
        messageManager.setChannel(channel);
    }
}
