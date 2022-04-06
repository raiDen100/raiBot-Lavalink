package com.raiden;

import lavalink.client.io.jda.JdaLavalink;

public class LavalinkHandler {

    private static final JdaLavalink lavalink = new JdaLavalink(1, Bot::getJdaInstanceFromId);

    public static JdaLavalink getLavalink(){
        return lavalink;
    }
}
