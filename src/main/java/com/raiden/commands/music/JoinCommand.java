package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.command.exceptions.VoiceChannelNullException;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.io.jda.JdaLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Optional;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        JdaLink link = musicManager.link;

        Member member = ctx.getMember();
        VoiceChannel memberVoiceChannel = Optional.ofNullable(member.getVoiceState().getChannel())
                .orElseThrow(() -> {
                    throw new VoiceChannelNullException(channel);
                });

        if(!link.getPlayer().isConnected() || !VoiceChecks.inChannelWith(musicManager, member)){
            link.connect(memberVoiceChannel);
        }
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public List<String> getAliases() {
        return List.of("summon", "connect");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
