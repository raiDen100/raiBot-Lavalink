package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.IButtonCommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.io.jda.JdaLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.List;

public class ClearCommand implements IButtonCommand {
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
    public void handle(ButtonClickEvent event) {

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        Member member = event.getInteraction().getMember();

        if (!VoiceChecks.inChannelWith(musicManager, member))
            return;

        musicManager.audioPlayer.stopTrack();
        musicManager.scheduler.queue.clear();
        musicManager.messageManager.updateButtonsNowPlayingEmbed(event);
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
        return IButtonCommand.super.getHelp();
    }
}
