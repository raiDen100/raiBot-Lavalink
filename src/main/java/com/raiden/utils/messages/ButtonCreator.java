package com.raiden.utils.messages;

import com.raiden.utils.player.TrackScheduler;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;

public class ButtonCreator {
    public static List<Button> createNowPlayingButtons(TrackScheduler scheduler){
        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.secondary("clear", "Clear"));
        buttonList.add(Button.secondary("skip", "Skip"));
        buttonList.add(Button.secondary("resume", "Play"));
        if(!scheduler.player.isPaused())
            buttonList.add(Button.secondary("pause", "Pause"));
        else
            buttonList.add(Button.danger("pause", "Pause"));

        if(!scheduler.loop)
            buttonList.add(Button.secondary("loop", "Loop"));
        else
            buttonList.add(Button.danger("loop", "Loop"));

        return buttonList;
    }

    public static List<Button> createQueueButtons(){
        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.secondary("queue first", "First"));
        buttonList.add(Button.secondary("queue previous", "Previous"));
        buttonList.add(Button.secondary("queue next", "Next"));
        buttonList.add(Button.secondary("queue last", "Last"));

        return buttonList;
    }
}
