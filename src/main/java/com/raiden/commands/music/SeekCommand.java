package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SeekCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        if(ctx.getArgs().size() < 2)
            return;

        long seekValue = Long.parseLong(ctx.getArgs().get(1)) * 1000;

        audioPlayer.seekTo(seekValue);
    }

    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public List<String> getAliases() {
        return List.of("ff");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
