package com.raiden.utils.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SpotifyAudioTrack implements AudioTrack {

    private final AudioTrackInfo audioTrackInfo;
    private Object userData;

    public SpotifyAudioTrack(AudioTrackInfo audioTrackInfo) {
        this.audioTrackInfo = audioTrackInfo;
    }

    @Override
    public AudioTrackInfo getInfo() {
        return this.audioTrackInfo;
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public AudioTrackState getState() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isSeekable() {
        return false;
    }

    @Override
    public long getPosition() {
        return 0;
    }

    @Override
    public void setPosition(long l) {

    }

    @Override
    public void setMarker(TrackMarker trackMarker) {

    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public AudioTrack makeClone() {
        return null;
    }

    @Override
    public AudioSourceManager getSourceManager() {
        return new AudioSourceManager() {
            @Override
            public String getSourceName() {
                return "spotify";
            }

            @Override
            public AudioItem loadItem(AudioPlayerManager audioPlayerManager, AudioReference audioReference) {
                return null;
            }

            @Override
            public boolean isTrackEncodable(AudioTrack audioTrack) {
                return false;
            }

            @Override
            public void encodeTrack(AudioTrack audioTrack, DataOutput dataOutput) throws IOException {

            }

            @Override
            public AudioTrack decodeTrack(AudioTrackInfo audioTrackInfo, DataInput dataInput) throws IOException {
                return null;
            }

            @Override
            public void shutdown() {

            }
        };
    }

    @Override
    public void setUserData(Object o) {
        this.userData = o;
    }

    @Override
    public Object getUserData() {
        return userData;
    }

    @Override
    public <T> T getUserData(Class<T> aClass) {
        return (T) userData;
    }
}
