package com.raiden.commands.utils;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public interface IButtonCommand extends ICommand{

    public void handle(ButtonClickEvent event);

}
