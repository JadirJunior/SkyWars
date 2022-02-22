package com.jadir.skywars.skywars.references;

import com.jadir.skywars.skywars.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GamePlayer {
    private Player player = null;
    private GameTeam team = null;
    private GamePlayerState gamePlayerState;
    private Location spawnPoint;

    public GamePlayer(Player player)  {
        this.player = player;
    }

    public GamePlayer(GameTeam team) { this.team = team; }

    public Player getPlayer() {
        return player;
    }

    public GameTeam getTeam() {
        return team;
    }

    public boolean isTeamClass() { return player == null && team != null; }

    public void sendMessage(String message) {
        if (isTeamClass()) {
            getTeam().sendMessage(message);
        } else {
            getPlayer().sendMessage(ChatUtil.format(message));
        }
    }


    public void teleport(Location loc) {
        if (loc == null) return;
        if (isTeamClass()) {
            getTeam().teleport(loc);
        } else {
            getPlayer().teleport(loc);
        }
    }

    public String getName() {
        if (isTeamClass()) {
           return getTeam().getName();
        } else {
            return getPlayer().getName();
        }
    }

    public enum GamePlayerState {

    }
}
