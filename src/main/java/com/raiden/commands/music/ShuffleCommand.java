package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.IButtonCommand;
import com.raiden.utils.command.ICommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.raiden.utils.player.VoiceChecks;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.List;

public class ShuffleCommand implements IButtonCommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (!VoiceChecks.inChannelWith(musicManager, ctx.getMember()) || musicManager.scheduler.queue.size() < 2)
            return;

        musicManager.scheduler.shuffleQueue();

        ctx.getMessage().addReaction("🔀").queue();
    }

    @Override
    public void handle(ButtonClickEvent event) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (!VoiceChecks.inChannelWith(musicManager, event.getMember()) || musicManager.scheduler.queue.size() < 2)
            return;

        musicManager.scheduler.shuffleQueue();
        musicManager.messageManager.updateButtonsQueueButtons(event, QueueCommand.getPage(musicManager, musicManager.messageManager.queuePage));
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public List<String> getAliases() {
        return List.of("sf");
    }

    @Override

    public String getHelp() {
        return IButtonCommand.super.getHelp();
    }
}
