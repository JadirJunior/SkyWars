package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();

        for (Game game : SkyWars.getInstance().getGames()) {
            for (GamePlayer gamePlayer : game.getPlayers()) {
                if (gamePlayer.isTeamClass()) {
                    if (gamePlayer.getTeam().isPlayer(player)) {
                        handle(e, game);
                    }
                } else {
                    if (gamePlayer.getPlayer() == player) {
                        handle(e, game);
                    }
                }
            }
        }
    }

    public void handle(PlayerDeathEvent e, Game game) {
        Player player = e.getEntity();
        GamePlayer gamePlayer = game.getGamePlayer(player);
        e.setDeathMessage(null);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setGameMode(GameMode.SPECTATOR);

        game.switchToSpectator(gamePlayer);
    }

}
