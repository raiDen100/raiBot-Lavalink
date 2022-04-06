package com.raiden.utils;

public class Config {
    private static final PropertyReader propertyReader = new PropertyReader();

    public static String getToken(){
        return propertyReader.getPropertyValue("bot.token");
    }

    public static String getPrefix() {
        return propertyReader.getPropertyValue("bot.prefix");
    }
}
