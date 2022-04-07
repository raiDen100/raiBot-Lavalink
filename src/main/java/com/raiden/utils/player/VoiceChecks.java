package com.raiden.utils.player;

import com.raiden.utils.player.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;

public class VoiceChecks {

    public static boolean inChannelWith(GuildMusicManager musicManager, Member member){
        return member.getVoiceState().getChannel().getId().equals(musicManager.link.getChannel());
    }
}
