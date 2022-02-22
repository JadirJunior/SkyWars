package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        for (Game game : SkyWars.getInstance().getGames()) {
            for (GamePlayer gamePlayer : game.getPlayers()) {
                if (gamePlayer.isTeamClass()) {
                    if (gamePlayer.getTeam().isPlayer(player)) {
                        player.damage(player.getMaxHealth());
                    }
                } else {
                    if (gamePlayer.getPlayer() == player) {
                        player.damage(player.getMaxHealth());
                    }
                }
            }
        }
    }

}
