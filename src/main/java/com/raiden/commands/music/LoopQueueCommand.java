package com.raiden.commands.music;

import com.raiden.commands.utils.CommandContext;
import com.raiden.commands.utils.ICommand;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class LoopQueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()))
            return;

        musicManager.scheduler.loopQueue = !musicManager.scheduler.loopQueue;
        musicManager.scheduler.loop = false;

        musicManager.messageManager.updateButtonsNowPlayingEmbed();

        if (musicManager.scheduler.loopQueue){
            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Looping queue!");
            channel.sendMessageEmbeds(messageEmbed).queue();
        }
        else{
            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Queue loop disabled");
            channel.sendMessageEmbeds(messageEmbed).queue();
        }
    }

    @Override
    public String getName() {
        return "loopqueue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("loopq", "lq");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
