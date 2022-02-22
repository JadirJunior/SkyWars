package com.jadir.skywars.skywars.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    abstract void execute(CommandSender sender, String[] args);
}
