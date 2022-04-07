package com.raiden.utils.messages;

import com.raiden.utils.player.TrackScheduler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.List;

public class MessageManager {
    private TextChannel channel;

    private Message lastNowPlaying;
    private TrackScheduler scheduler;

    private Message lastQueueMessage;

    public int queuePage = 0;


    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }
    public void setTrackScheduler(TrackScheduler scheduler){
        this.scheduler = scheduler;
    }

    public void sendNowPlayingMessage(MessageEmbed messageEmbed){
        List<Button> buttons = ButtonCreator.createNowPlayingButtons(scheduler);

        channel.sendMessageEmbeds(messageEmbed).setActionRow(buttons).queue(message -> lastNowPlaying = message);
    }

    public void sendQueueMessage(String messageContent){
        List<Button> buttons = ButtonCreator.createQueueButtons();

        queuePage = 0;
        channel.sendMessage(messageContent).setActionRow(buttons).queue(message -> lastQueueMessage = message);
    }

    public void updateButtonsQueueButtons(ButtonClickEvent buttonEvent, String messageContent){
        buttonEvent.editMessage(messageContent).queue();
    }

    public void updateButtonsNowPlayingEmbed(ButtonClickEvent buttonEvent){
        List<Button> buttons = ButtonCreator.createNowPlayingButtons(scheduler);

        buttonEvent.editMessage(buttonEvent.getMessage()).setActionRow(buttons).queue();
    }

    public void updateButtonsNowPlayingEmbed(){
        List<Button> buttons = ButtonCreator.createNowPlayingButtons(scheduler);

        lastNowPlaying.editMessage(lastNowPlaying).setActionRow(buttons).queue();
    }

    public void sendQueueMessage(MessageEmbed messageEmbed){
        channel.sendMessageEmbeds(messageEmbed).queue();
    }

    public void removeLastPlayingMessage(){
        lastNowPlaying.delete().queue();
    }
}
