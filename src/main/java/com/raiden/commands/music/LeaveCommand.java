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

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        JdaLink link = musicManager.link;

        Member member = ctx.getMember();

        if(!link.getPlayer().isConnected() || !VoiceChecks.inChannelWith(musicManager, member))
            return;

        link.disconnect();
        link.getPlayer().stopTrack();
        musicManager.scheduler.loopQueue = false;
        musicManager.scheduler.loop = false;
        musicManager.scheduler.queue.clear();
        musicManager.resetFilters();

        ctx.getMessage().addReaction("👋").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public List<String> getAliases() {
        return ICommand.super.getAliases();
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
