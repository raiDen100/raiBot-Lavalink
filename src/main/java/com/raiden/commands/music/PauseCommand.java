package com.raiden.commands.music;

import com.raiden.commands.utils.CommandContext;
import com.raiden.commands.utils.IButtonCommand;
import com.raiden.commands.utils.VoiceChecks;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.List;

public class PauseCommand implements IButtonCommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null)
            return;
        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        audioPlayer.setPaused(true);
        musicManager.messageManager.updateButtonsNowPlayingEmbed();

        ctx.getMessage().addReaction("⏸").queue();
    }

    @Override
    public void handle(ButtonClickEvent event) {

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null)
            return;
        if (!VoiceChecks.inChannelWith(musicManager, event.getMember()))
            return;

        audioPlayer.setPaused(true);
        musicManager.messageManager.updateButtonsNowPlayingEmbed(event);
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public List<String> getAliases() {
        return List.of("halt");
    }

    @Override
    public String getHelp() {
        return IButtonCommand.super.getHelp();
    }
}
