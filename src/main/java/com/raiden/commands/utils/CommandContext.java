package com.raiden.commands.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.annotation.Nullable;
import java.util.List;

public class CommandContext implements ICommandContext{

    private GuildMessageReceivedEvent event;
    private final List<String> args;

    public List<String> getArgs() {
        return args;
    }

    public CommandContext(@Nullable GuildMessageReceivedEvent event, @Nullable List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return ICommandContext.super.getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    @Override
    public TextChannel getChannel() {
        return ICommandContext.super.getChannel();
    }

    @Override
    public Message getMessage() {
        return ICommandContext.super.getMessage();
    }

    @Override
    public User getAuthor() {
        return ICommandContext.super.getAuthor();
    }

    @Override
    public Member getMember() {
        return ICommandContext.super.getMember();
    }

    @Override
    public JDA getJDA() {
        return ICommandContext.super.getJDA();
    }

    @Override
    public ShardManager getShardManager() {
        return ICommandContext.super.getShardManager();
    }

    @Override
    public User getSelfUser() {
        return ICommandContext.super.getSelfUser();
    }

    @Override
    public Member getSelfMember() {
        return ICommandContext.super.getSelfMember();
    }
}
