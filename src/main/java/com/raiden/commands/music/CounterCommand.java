package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class CounterCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        IPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null)
            return;

        MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Played this song " + musicManager.scheduler.getLoopCounter() + " times before");
        channel.sendMessageEmbeds(messageEmbed).queue();
    }

    @Override
    public String getName() {
        return "counter";
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
