package com.raiden.commands.music;

import com.raiden.commands.utils.*;
import com.raiden.commands.utils.exceptions.VoiceChannelNullException;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.io.jda.JdaLink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.net.URL;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PlayCommand implements ICommand {
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

        String track = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size())).trim();

        if (!isValid(track))
            track = "ytsearch:" + track;


        musicManager.setChannel(channel);
        PlayerManager.getInstance().loadAndPlay(ctx, track, ctx.getAuthor());
    }

    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
