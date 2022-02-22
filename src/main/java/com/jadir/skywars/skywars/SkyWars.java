package com.jadir.skywars.skywars;

import com.jadir.skywars.skywars.commands.SkyWarsCommand;
import com.jadir.skywars.skywars.config.Config;
import com.jadir.skywars.skywars.listeners.*;
import com.jadir.skywars.skywars.references.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SkyWars extends JavaPlugin {

    public static Config gameInfo;
    private static SkyWars instance;
    private List<Game> games = new ArrayList<>();
    private int gamesLimit = 0;

    private boolean singleServerMode = false;

    private HashMap<Player, Game> playerGameMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        this.singleServerMode = getConfig().getBoolean("single-mode");

        if (singleServerMode) {
            gamesLimit = 1;
        } else {
            gamesLimit = getConfig().getInt("max-games");
        }

        gameInfo = new Config(this, "gameInfo.yml");
        gameInfo.saveConfig();
        if (gameInfo.getConfig().getConfigurationSection("games") != null) {
            gameInfo.getConfig().getConfigurationSection("games").getKeys(false).forEach(gameName -> {
                getLogger().warning(gameName);
                Game game = new Game(gameName);
                boolean stats  = this.registerGame(game);
                if (!stats) {
                    getLogger().warning("Falha ao carregar o jogo "+gameName);
                }
            });
        }
        else {
            getLogger().warning("No Have Games");
        }
        getCommand("skywars").setExecutor(new SkyWarsCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevel(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new BlockInteract(), this);
        Bukkit.getPluginManager().registerEvents(new EntitySpawn(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamage(), this);
        Bukkit.getPluginManager().registerEvents(new ChestInteract(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static SkyWars getInstance() {
        return instance;
    }

    public boolean registerGame(Game game) {
        if (games.size() == gamesLimit && gamesLimit != -1) {
            return false;
        }
        games.add(game);
        return true;
    }

    public Game getGame(String gameName) {
        for (Game game : games) {
            getLogger().warning(game.getDisplayName());
            if (game.getDisplayName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }
        return null;
    }

    public Game getGame(Player player) {return playerGameMap.get(player);}

    public List<Game> getGames() {
        return games;
    }

    public void setGame(Player player, Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        } else {
            this.playerGameMap.put(player, game);
        }
    }

    public HashMap<Player, Game> getPlayerGameMap() {
        return playerGameMap;
    }

    public boolean isSingleServerMode() {
        return singleServerMode;
    }
}
