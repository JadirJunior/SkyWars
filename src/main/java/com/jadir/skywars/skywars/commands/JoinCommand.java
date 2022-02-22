package com.jadir.skywars.skywars.commands;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import com.jadir.skywars.skywars.utils.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends SubCommand {
    @Override
    void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ChatUtil.format("&9Skywars &7>> &c/skywars join <game name>"));
            } else {
                for (Game game : SkyWars.getInstance().getGames()) {
                    for (GamePlayer gamePlayer : game.getPlayers()) {
                        if (gamePlayer.isTeamClass()) {
                            if (gamePlayer.getTeam().isPlayer(player)) {
                                player.sendMessage(ChatUtil.format("&9Skywars &7>> &cVocê já está em um jogo"));
                                return;
                            }
                        } else {
                            if (gamePlayer.getPlayer() == player) {
                                player.sendMessage(ChatUtil.format("&9Skywars &7>> &cVocê já está em um jogo!"));
                                return;
                            }
                        }
                    }
                }

                Game game = SkyWars.getInstance().getGame(args[0]);
                if (game == null) {
                    player.sendMessage(ChatUtil.format("&9Skywars &7>> &cJogo não existe!."));
                    return;
                }
                player.sendMessage("Entrando no game " + game.getDisplayName());
                game.joinGame(new GamePlayer(player));
            }
        } else {
            sender.sendMessage(ChatUtil.format("&9Skywars &7>> &cVocê não é um player!"));
        }
    }
}
