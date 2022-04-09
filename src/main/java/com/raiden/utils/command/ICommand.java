package com.raiden.utils.command;

import java.util.List;

public interface ICommand {
    public void handle(CommandContext ctx);
    public String getName();
    default public List<String> getAliases(){
        return  List.of();
    }
    default public String getHelp(){
        return "Help not provided";
    }

}
