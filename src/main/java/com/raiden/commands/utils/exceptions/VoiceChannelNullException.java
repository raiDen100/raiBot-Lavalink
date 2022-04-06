package com.raiden.commands.utils.exceptions;

import net.dv8tion.jda.api.entities.TextChannel;

public class VoiceChannelNullException extends NullPointerException{
    public VoiceChannelNullException(TextChannel channel){
        channel.sendMessage("You have to be in a voice channel").queue();
    }
}
