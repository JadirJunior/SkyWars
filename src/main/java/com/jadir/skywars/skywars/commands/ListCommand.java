package com.jadir.skywars.skywars.commands;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.utils.ChatUtil;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    @Override
    void execute(CommandSender sender, String[] args) {
        for (Game game : SkyWars.getInstance().getGames()) {
            sender.sendMessage(ChatUtil.format("&9Skywars &7>> &f" + game.getDisplayName() + " - " + game.getPlayers().size() + " (alive) players"));
        }
    }

}
