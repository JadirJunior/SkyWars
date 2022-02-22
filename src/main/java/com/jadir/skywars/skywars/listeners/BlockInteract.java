package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockInteract implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        handle(e,player);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        handle(e, player);
    }


    private void handle(Cancellable e, Player player) {
        Game game = SkyWars.getInstance().getGame(player);
        if (game != null) {
            if (game.isState(Game.GameState.STARTING) || game.isState(Game.GameState.LOBBY)) {
                e.setCancelled(true);
                return;
            }

            GamePlayer gamePlayer = game.getGamePlayer(player);
            if (gamePlayer != null) {
                if (gamePlayer.isTeamClass()) {
                    if (gamePlayer.getTeam().isPlayer(player)) {
                        if (!game.getPlayers().contains(gamePlayer)) {
                            e.setCancelled(true);
                        }
                    }
                }
                else {
                    if (gamePlayer.getPlayer() == player) {
                        if (!game.getPlayers().contains(gamePlayer)){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        } else {
            if (SkyWars.getInstance().isSingleServerMode()) {
                e.setCancelled(true);
            }
        }
    }
}
