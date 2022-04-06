package com.raiden;

import com.raiden.utils.PropertyReader;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

@Slf4j
public class Bot {
    public static void main(String[] args) throws LoginException {

        PropertyReader propertyReader = new PropertyReader();

        JDA jda = JDABuilder.createDefault(propertyReader.getPropertyValue("bot.token"))
                .addEventListeners(new Listener())
                .build();
    }
}
