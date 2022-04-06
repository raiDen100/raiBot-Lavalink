package com.raiden.utils;

import com.raiden.Bot;
import lavalink.client.io.jda.JdaLavalink;

public class LavalinkHandler {

    private static final JdaLavalink lavalink = new JdaLavalink(1, Bot::getJdaInstanceFromId);

    public static JdaLavalink getLavalink(){
        return lavalink;
    }
}
