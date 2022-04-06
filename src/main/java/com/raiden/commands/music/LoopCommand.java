package com.raiden.commands.music;

import com.raiden.commands.utils.*;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.w3c.dom.Text;

import java.util.List;

public class LoopCommand implements IButtonCommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        Member member = ctx.getMember();

        if (!VoiceChecks.inChannelWith(musicManager, member))
            return;

        musicManager.scheduler.loop = !musicManager.scheduler.loop;

        musicManager.messageManager.updateButtonsNowPlayingEmbed();

        if (musicManager.scheduler.loop){
            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Looping current song!");
            channel.sendMessageEmbeds(messageEmbed).queue();
        }
        else{
            MessageEmbed messageEmbed = EmbedCreator.actionSuccessfulEmbed("Loop disabled");
            channel.sendMessageEmbeds(messageEmbed).queue();
        }
    }

    @Override
    public void handle(ButtonClickEvent event) {

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        Member member = event.getMember();

        if (!VoiceChecks.inChannelWith(musicManager, member))
            return;

        musicManager.scheduler.loop = !musicManager.scheduler.loop;
        musicManager.messageManager.updateButtonsNowPlayingEmbed(event);

    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public List<String> getAliases() {
        return List.of("repeat");
    }

    @Override
    public String getHelp() {
        return IButtonCommand.super.getHelp();
    }
}
