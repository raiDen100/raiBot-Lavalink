package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.IButtonCommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.player.IPlayer;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.List;

@Slf4j
public class SkipCommand implements IButtonCommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null)
            return;
        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        if (audioPlayer.getPlayingTrack() != null && musicManager.scheduler.queue.size() == 0){
            log.info("Skipping: " + audioPlayer.getPlayingTrack().getInfo().title);
            audioPlayer.stopTrack();
            return;
        }

        log.info("Skipping: " + audioPlayer.getPlayingTrack().getInfo().title);
        musicManager.scheduler.nextTrack();
    }

    public void handle(ButtonClickEvent event){
        log.info("Skip button clicked!");

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        Member member = event.getInteraction().getMember();

        if (!VoiceChecks.inChannelWith(musicManager, member))
            return;

        if (audioPlayer.getPlayingTrack() != null && musicManager.scheduler.queue.size() == 0){
            audioPlayer.stopTrack();
            return;
        }

        log.info("Skipping: " + audioPlayer.getPlayingTrack().getInfo().title);
        musicManager.scheduler.nextTrack();

    }



    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public List<String> getAliases() {
        return List.of();
    }

    @Override
    public String getHelp() {
        return IButtonCommand.super.getHelp();
    }
}
