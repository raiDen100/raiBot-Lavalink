package com.raiden.commands.music.filters;

import com.raiden.commands.utils.CommandContext;
import com.raiden.commands.utils.ICommand;
import com.raiden.utils.messages.EmbedCreator;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        Member member = ctx.getMember();
        List<String> args = ctx.getArgs();

        if (!VoiceChecks.inChannelWith(musicManager, member) || args.size() < 2) {
            return;
        }

        if (args.get(1).equals("reset")){
            musicManager.resetVolume();
            channel.sendMessageEmbeds(EmbedCreator.actionSuccessfulEmbed("Volume reset!")).queue();
            return;
        }

        float volume = Float.parseFloat(ctx.getArgs().get(1));
        if (volume > 500 || volume < 0){
            channel.sendMessageEmbeds(EmbedCreator.actionFailedEmbed("Value must be 0-500")).queue();
            return;
        }

        musicManager.setVolume(volume / 100);


        channel.sendMessageEmbeds(EmbedCreator.actionSuccessfulEmbed("Volume " + Math.round(volume) + " set!")).queue();
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public List<String> getAliases() {
        return List.of("er");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
