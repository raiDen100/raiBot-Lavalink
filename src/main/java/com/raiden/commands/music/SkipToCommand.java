package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

@Slf4j
public class SkipToCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null)
            return;
        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        List<String> args = ctx.getArgs();
        if (musicManager.scheduler.queue.size() == 0 || args.size() < 2)
            return;


        int skipToIndex = Integer.parseInt(args.get(1))-1;

        if (skipToIndex == 0)
            return;

        if (skipToIndex > musicManager.scheduler.queue.size()){
            MessageEmbed messageEmbed = EmbedCreator.actionFailedEmbed("Cannot skip farther than the queue length");
            channel.sendMessageEmbeds(messageEmbed).queue();
            return;
        }

        SkipCommand.sendCounterEmbed(musicManager, audioPlayer, channel);

        List<AudioTrack> newQueue = musicManager.scheduler.queue.subList(skipToIndex-1, musicManager.scheduler.queue.size());
        musicManager.scheduler.queue = newQueue;
        musicManager.scheduler.nextTrack();
    }

    @Override
    public String getName() {
        return "skipto";
    }

    @Override
    public List<String> getAliases() {
        return List.of("st");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
