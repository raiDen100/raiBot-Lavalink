package com.raiden.commands.utils;

import java.util.List;

public interface ICommand {
    public void handle(CommandContext ctx);
    public String getName();
    public List<String> getAliases();
    default public String getHelp(){
        return "Help not provided";
    }

}
