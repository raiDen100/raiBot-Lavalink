package com.raiden.commands.utils;

import com.raiden.commands.PingCommand;
import com.raiden.commands.music.*;
import com.raiden.commands.music.filters.BassboostCommand;
import com.raiden.commands.utils.exceptions.CommandNotFoundException;
import com.raiden.utils.Config;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    private final List<ICommand> commands = List.of(
            new PingCommand(),
            new PlayCommand(),
            new SkipCommand(),
            new LoopCommand(),
            new PauseCommand(),
            new ResumeCommand(),
            new BassboostCommand(),
            new LoopQueueCommand(),
            new ShuffleCommand(),
            new QueueCommand(),
            new LeaveCommand(),
            new ClearCommand()
    );

    public void handle(GuildMessageReceivedEvent event){

        String messageNoPrefix = event.getMessage().getContentRaw().replace(Config.getPrefix(), "");
        String[] split = messageNoPrefix.split(" ");
        if (split.length < 1)
            return;

        List<String> args = Arrays.asList(split);

        ICommand command = commands.stream()
                .filter(iCommand -> iCommand.getName().equals(args.get(0)) || iCommand.getAliases().contains(args.get(0)))
                .findFirst()
                .orElseThrow(CommandNotFoundException::new);

        command.handle(new CommandContext(event, args));
    }


    public void handle(ButtonClickEvent event) {

        String[] split = event.getButton().getId().split(" ");
        if (split.length == 0)
            return;

        List<String> args = Arrays.asList(split);

        ICommand command = commands.stream()
                .filter(iCommand -> iCommand.getName().equals(args.get(0)) || iCommand.getAliases().contains(args.get(0)))
                .findFirst()
                .orElseThrow(CommandNotFoundException::new);

        if (command instanceof IButtonCommand)
            ((IButtonCommand) command).handle(event);
    }
}
