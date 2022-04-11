package com.raiden.utils.player;

import com.raiden.utils.LavalinkHandler;
import com.raiden.utils.command.CommandContext;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
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
        audioPlayerManager.loadItem(trackUrl, new DefaultAudioResultHandler(author, musicManager));
    }

    public void loadAndPlayNow(CommandContext ctx, String track, User author) {
        GuildMusicManager musicManager = getMusicManager(ctx.getGuild());
        audioPlayerManager.loadItem(track, new PlayNowResultHandler(author, musicManager));
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
