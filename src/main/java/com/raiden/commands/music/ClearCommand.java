package com.raiden.commands.music;

import com.raiden.commands.utils.CommandContext;
import com.raiden.commands.utils.ICommand;
import com.raiden.commands.utils.exceptions.VoiceChannelNullException;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.io.jda.JdaLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Optional;

public class ClearCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        JdaLink link = musicManager.link;

        Member member = ctx.getMember();

        if(!link.getPlayer().isConnected() || !VoiceChecks.inChannelWith(musicManager, member))
            return;

        musicManager.audioPlayer.stopTrack();
        musicManager.scheduler.queue.clear();

        ctx.getMessage().addReaction("🛑").queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public List<String> getAliases() {
        return List.of("clear");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
