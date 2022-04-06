package com.raiden.commands.utils.player;

import com.raiden.LavalinkHandler;
import com.raiden.commands.utils.CommandContext;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlayerManager extends DefaultAudioPlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    private final DefaultAudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public PlayerManager() {
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }

    public void loadAndPlay(CommandContext ctx, String trackUrl, User author){
        GuildMusicManager musicManager = getMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {


            @Override
            public void trackLoaded(AudioTrack track) {
                log.info("Track loaded: " + track.getInfo().title);
                scheduler.queueTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                AudioTrack track = audioPlaylist.getTracks().stream()
                        .findFirst()
                        .orElseThrow();
                log.info(track.getInfo().title);
                scheduler.queueTrack(track);
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException e) {
                log.info(e.getMessage());
            }
        });
    }

    public GuildMusicManager getMusicManager(Guild guild){
        if (!musicManagers.containsKey(guild.getIdLong())){
            musicManagers.put(guild.getIdLong(), new GuildMusicManager(LavalinkHandler.getLavalink().getLink(guild)));
        }
        return musicManagers.get(guild.getIdLong());
    }

    public static PlayerManager getInstance(){
        if (INSTANCE == null){
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }

}
