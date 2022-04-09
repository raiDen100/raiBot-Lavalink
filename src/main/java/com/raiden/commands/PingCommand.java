package com.raiden.commands;

import com.raiden.utils.command.CommandContext;
import com.raiden.utils.command.ICommand;

import java.util.List;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("PONG").queue();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public List<String> getAliases() {
        return List.of("pong");
    }

    @Override
    public String getHelp() {
        return ICommand.super.getHelp();
    }
}
