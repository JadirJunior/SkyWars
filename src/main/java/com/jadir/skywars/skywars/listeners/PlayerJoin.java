package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import com.jadir.skywars.skywars.utils.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!SkyWars.getInstance().getConfig().getBoolean("single-mode")) {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(25);
        }

        /*Game testGame = SkyWars.getInstance().getGame("teste");

        if (testGame == null) {
            player.sendMessage(ChatUtil.format("Algo deu errado..."));
            return;
        }

        testGame.joinGame(new GamePlayer(player));*/
    }
}
