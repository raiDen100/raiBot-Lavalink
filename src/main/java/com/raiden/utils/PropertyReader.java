package com.raiden.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertyReader {

    public String getPropertyValue(String propertyName){
        try{

            Properties properties = new Properties();

            InputStream inputStream = getClass().getResourceAsStream("/config.properties");
            properties.load(inputStream);

            return properties.getProperty(propertyName);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return null;
    }
}
