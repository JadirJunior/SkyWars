package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();

            Game game = SkyWars.getInstance().getGame(player);
            if (game != null) {
                if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING)) {
                    e.setCancelled(true);
                }
            } else {
                if (SkyWars.getInstance().isSingleServerMode()) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
