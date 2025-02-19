package com.raiden.utils;

public class Config {
    private static final PropertyReader propertyReader = new PropertyReader();

    public static String getToken(){
        return propertyReader.getPropertyValue("bot.token");
    }

    public static String getPrefix() {
        return propertyReader.getPropertyValue("bot.prefix");
    }

    public static String getNodeAddress() {
        return propertyReader.getPropertyValue("node.address");
    }

    public static String getNodePassword(){
        return propertyReader.getPropertyValue("node.password");
    }

    public static String getSpotifySecret(){
        return propertyReader.getPropertyValue("spotify.client.secret");
    }
    public static String getSpotifyId(){
        return propertyReader.getPropertyValue("spotify.client.id");
    }
    public static String getSpotifyRedirect(){
        return propertyReader.getPropertyValue("spotify.redirect");
    }
}
