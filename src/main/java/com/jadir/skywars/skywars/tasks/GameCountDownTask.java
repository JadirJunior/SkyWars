package com.jadir.skywars.skywars.tasks;

import com.jadir.skywars.skywars.references.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountDownTask extends BukkitRunnable {
    private int timeToStart = 20;
    private Game game;

    public GameCountDownTask(Game game) {
        this.game = game;
        game.setMovementFreeze(true);
    }

    @Override
    public void run() {
        timeToStart--;
        if (timeToStart==10) {
            game.setGameState(Game.GameState.STARTING);
            game.sendMessage("&aO jogo iniciará em 10 segundos.");
        } else if (timeToStart<=5) {
            if (timeToStart<=0) {
                cancel();
                game.sendMessage("&aIniciando...");
                game.setMovementFreeze(false);
                game.setGameState(Game.GameState.IN_GAME);
                timeToStart = 20;
                return;
            }
            game.sendMessage("&aO jogo iniciará em "+timeToStart+" segundos.");
        }
    }
}
