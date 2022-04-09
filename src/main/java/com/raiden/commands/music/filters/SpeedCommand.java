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

public class SpeedCommand implements ICommand {
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
            musicManager.resetSpeed();
            channel.sendMessageEmbeds(EmbedCreator.actionSuccessfulEmbed("Speed reset!")).queue();
            return;
        }

        float speed = Float.parseFloat(ctx.getArgs().get(1));
        if (speed > 200 || speed < 20){
            channel.sendMessageEmbeds(EmbedCreator.actionFailedEmbed("Value must be 20-200")).queue();
            return;
        }

        musicManager.setSpeed(speed / 100);
        channel.sendMessageEmbeds(EmbedCreator.actionSuccessfulEmbed("Speed " + Math.round(speed) + " set!")).queue();
    }

    @Override
    public String getName() {
        return "speed";
    }

    @Override
    public List<String> getAliases() {
        return List.of("sp");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
