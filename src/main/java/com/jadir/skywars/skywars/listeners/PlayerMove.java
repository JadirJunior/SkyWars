package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        Game game = SkyWars.getInstance().getGame(player);

        if (game != null) {
            if (game.isMovementFreeze()) {
                if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                    player.teleport(e.getFrom());
                }
            }
        }
    }
}
