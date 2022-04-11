package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.command.exceptions.VoiceChannelNullException;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.SpotifyClientWrapper;
import com.raiden.utils.player.VoiceChecks;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavalink.client.io.jda.JdaLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.List;
import java.util.Optional;

public class PlayNowCommand implements ICommand {

    private static final SpotifyClientWrapper spotifyClientWrapper = PlayCommand.getSpotifyClientWrapper();

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

        if (!link.getPlayer().isConnected() || !VoiceChecks.inChannelWith(musicManager, member)) {
            link.connect(memberVoiceChannel);
        }

        String track = String.join(" ", ctx.getArgs().subList(1, ctx.getArgs().size())).trim();

        if (PlayCommand.isSpotifyUrl(track)) {
            List<AudioTrack> tracks = spotifyClientWrapper.handleSpotifyUrl(track, ctx.getAuthor());
            musicManager.scheduler.insertTrack(0, tracks.get(0), true);
            return;
        }

        if (!PlayCommand.isValid(track))
            track = "ytsearch:" + track;


        PlayerManager.getInstance().loadAndPlayNow(ctx, track, ctx.getAuthor());
    }

    @Override
    public String getName() {
        return "playnow";
    }

    @Override
    public List<String> getAliases() {
        return List.of("pn");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
