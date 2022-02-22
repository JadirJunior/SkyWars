package com.jadir.skywars.skywars.listeners;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.references.Game;
import com.jadir.skywars.skywars.references.GamePlayer;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ChestInteract implements Listener {

    @EventHandler
    public void onChestInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

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

    private void handle(PlayerInteractEvent e, Game game) {
        if (e.hasBlock() && e.getClickedBlock() != null && e.getClickedBlock().getState() instanceof Chest) {
            Chest chest = (Chest) e.getClickedBlock().getState();

            if (game.getOpenedChests().contains(chest)) { return; }

            Random random = new Random();

            if (random.nextFloat() < 0.2) {
                int toFill = random.nextInt(8);

                for (int i = 0; i<toFill;i++) {
                    chest.getBlockInventory().addItem(game.getRareItems().get(random.nextInt(game.getRareItems().size())));
                }

            } else {
                int toFill = random.nextInt(5);

                for (int i = 0; i<toFill;i++) {
                    chest.getBlockInventory().addItem(game.getNormalItems().get(random.nextInt(game.getNormalItems().size())));
                }
            }
            game.getOpenedChests().add(chest);
        }
    }
}
