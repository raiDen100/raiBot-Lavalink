package com.raiden;

import com.raiden.utils.Config;
import com.raiden.utils.LavalinkHandler;
import lavalink.client.io.jda.JdaLavalink;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.net.URISyntaxException;

@Slf4j
public class Bot {
    private static ShardManager shardManager;

    public static void main(String[] args) throws LoginException, URISyntaxException {

        JdaLavalink lavalink = LavalinkHandler.getLavalink();

        shardManager = DefaultShardManagerBuilder.createDefault(Config.getToken())
                .addEventListeners(new Listener(), lavalink)
                .setVoiceDispatchInterceptor(lavalink.getVoiceInterceptor())
                .build();

    }

    public static JDA getJdaInstanceFromId(Integer shardId) {
        return shardManager.getShardById(shardId);
    }

}
