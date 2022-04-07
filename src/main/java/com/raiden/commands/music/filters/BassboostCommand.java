package com.raiden.commands.music.filters;

import com.raiden.commands.utils.CommandContext;
import com.raiden.commands.utils.ICommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BassboostCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        if(ctx.getArgs().size() < 2)
            return;

        if (ctx.getArgs().get(1).equals("reset")){
            musicManager.resetBassboost();
            return;
        }

        float bassboostValue = Float.parseFloat(ctx.getArgs().get(1)) / 100;

        musicManager.setBassboost(bassboostValue);
    }

    @Override
    public String getName() {
        return "bassboost";
    }

    @Override
    public List<String> getAliases() {
        return List.of("bb");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
