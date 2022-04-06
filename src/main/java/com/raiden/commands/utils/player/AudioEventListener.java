package com.raiden.commands.utils.player;

import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lavalink.client.player.IPlayer;
import lavalink.client.player.event.*;

public class AudioEventListener implements IPlayerEventListener {


    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason){

    }
    public void onTrackStart(IPlayer player, AudioTrack track){

    }
    public void onTrackStuck(IPlayer player, AudioTrack track, long thresholdMs){

    }
    public void onTrackException(IPlayer player, AudioTrack track, Exception exception){

    }
    public void onPlayerPause(IPlayer player){

    }
    public void onPlayerResume(IPlayer player){

    }

    @Override
    public void onEvent(PlayerEvent playerEvent) {
        if (playerEvent instanceof TrackStartEvent){
            onTrackStart(playerEvent.getPlayer(), ((TrackStartEvent) playerEvent).getTrack());
        } else if (playerEvent instanceof TrackEndEvent){
            onTrackEnd(playerEvent.getPlayer(), ((TrackEndEvent) playerEvent).getTrack(), ((TrackEndEvent) playerEvent).getReason());
        } else if (playerEvent instanceof TrackStuckEvent){
            onTrackStuck(playerEvent.getPlayer(), ((TrackStuckEvent) playerEvent).getTrack(), ((TrackStuckEvent) playerEvent).getThresholdMs());
        } else if (playerEvent instanceof TrackExceptionEvent){
            onTrackException(playerEvent.getPlayer(), ((TrackExceptionEvent) playerEvent).getTrack(), ((TrackExceptionEvent) playerEvent).getException());
        } else if(playerEvent instanceof PlayerPauseEvent){
            onPlayerPause(playerEvent.getPlayer());
        }else if (playerEvent instanceof PlayerResumeEvent){
            onPlayerResume(playerEvent.getPlayer());
        }
    }
}
