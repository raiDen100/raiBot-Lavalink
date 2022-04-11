package com.raiden.commands.music;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.IButtonCommand;
import com.raiden.utils.player.GuildMusicManager;
import com.raiden.utils.player.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueueCommand implements IButtonCommand {

    private static final int CHARS_PER_LINE = 60;
    private static final int SONG_TITLE_CHARS = 50;
    private static final int ITEMS_PER_PAGE = 10;
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        if (getFullQueue(musicManager).size() == 0)
            return; // Queue is empty

        musicManager.messageManager.sendQueueMessage(getPage(musicManager, 0));
    }

    @Override
    public void handle(ButtonClickEvent event) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

        if (getFullQueue(musicManager).size() == 0)
            return; // Queue is empty
        List<String> args = Arrays.asList(event.getButton().getId().split(" "));
        if (args.size() < 2){
            musicManager.messageManager.updateButtonsNowPlayingEmbed(event);
            musicManager.messageManager.sendQueueMessage(getPage(musicManager, 0));
            return;
        }

        switch (args.get(1)){
            case "next" -> {
               nextPage(musicManager, event);
            }
            case "previous" -> {
               previousPage(musicManager, event);
            }
            case "first" -> {
                firstPage(musicManager, event);
            }
            case  "last" -> {
                lastPage(musicManager, event);
            }
        }

    }

    private void nextPage(GuildMusicManager musicManager, ButtonClickEvent event){
        double maxPageNumber = Math.ceil((float)getFullQueue(musicManager).size() / ITEMS_PER_PAGE)-1;
        musicManager.messageManager.queuePage++;
        if (musicManager.messageManager.queuePage > maxPageNumber)
            musicManager.messageManager.queuePage = (int)maxPageNumber;
        musicManager.messageManager.updateButtonsQueueButtons(event, getPage(musicManager, musicManager.messageManager.queuePage));
    }

    private void previousPage(GuildMusicManager musicManager, ButtonClickEvent event){
        musicManager.messageManager.queuePage--;
        if (musicManager.messageManager.queuePage < 0)
            musicManager.messageManager.queuePage = 0;

        musicManager.messageManager.updateButtonsQueueButtons(event, getPage(musicManager, musicManager.messageManager.queuePage));
    }

    private void firstPage(GuildMusicManager musicManager, ButtonClickEvent event){
        musicManager.messageManager.queuePage = 0;

        musicManager.messageManager.updateButtonsQueueButtons(event, getPage(musicManager, musicManager.messageManager.queuePage));
    }

    private void lastPage(GuildMusicManager musicManager, ButtonClickEvent event){
        musicManager.messageManager.queuePage = (int)Math.ceil((float)getFullQueue(musicManager).size() / ITEMS_PER_PAGE)-1;

        musicManager.messageManager.updateButtonsQueueButtons(event, getPage(musicManager, musicManager.messageManager.queuePage));
    }

    public static String getPage(GuildMusicManager musicManager, int pageNumber){
        List<AudioTrack> fullQueue = getFullQueue(musicManager);

        int startIndex = pageNumber * ITEMS_PER_PAGE;
        int endIndex = (ITEMS_PER_PAGE * (pageNumber+1));
        if (endIndex > fullQueue.size())
            endIndex = fullQueue.size();

        List<AudioTrack> tracksOnPage = fullQueue.subList(startIndex, endIndex);
        String page = "```ml\n";
        int trackNumber = pageNumber * ITEMS_PER_PAGE + 1;
        for(AudioTrack track : tracksOnPage){
            page += getPreparedLine(track, trackNumber);
            trackNumber += 1;
        }
        page += "```";

        return page;
    }

    private static String getPreparedLine(AudioTrack track, int trackNumber){

        long durationInSeconds = track.getInfo().length / 1000;

        String trackNumberString = trackNumber + ". ";

        String durationString = getDurationString(durationInSeconds);
        StringBuilder title = new StringBuilder(track.getInfo().title);
        if (title.length() > SONG_TITLE_CHARS)
            title =  new StringBuilder(title.substring(0, SONG_TITLE_CHARS));

        int remainingCharacters = CHARS_PER_LINE - title.length() - durationString.length() - trackNumberString.length();

        StringBuilder line = title;
        for (int i = 0; i < remainingCharacters; i++) {
            line.append(" ");
        }

        line.append(durationString).append("\n");
        return trackNumberString + line.toString();
    }

    private static String getDurationString(long duration){
        long minutes = duration / 60;
        long seconds = duration % 60;

        String minutesStr = String.valueOf(minutes);
        String secondsStr = String.valueOf(seconds);


        if (seconds < 10)
            secondsStr = "0" + seconds;
        if (minutes < 10)
            minutesStr = "0" + minutes;

        return minutesStr + ":" + secondsStr;
    }



    private static List<AudioTrack> getFullQueue(GuildMusicManager musicManager){
        List<AudioTrack> queueTracks = new ArrayList<>();
        queueTracks.addAll(musicManager.scheduler.queue);
        queueTracks.add(0, musicManager.audioPlayer.getPlayingTrack());

        return queueTracks;
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("q");
    }

    @Override
    public String getHelp() {
        return IButtonCommand.super.getHelp();
    }
}
