package com.jadir.skywars.skywars.references;

import com.jadir.skywars.skywars.SkyWars;
import com.jadir.skywars.skywars.tasks.GameCountDownTask;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    //Diretamente da config.
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private List<Location> spawns;
    private String displayName;
    private Location lobby;
    private boolean isTeamGame;
    private List<ItemStack> normalItems;
    private List<ItemStack> rareItems;

    //Infos inGame
    private List<GamePlayer> players;
    private List<GamePlayer> spectatorsPlayers;
    private HashMap<GamePlayer, Location> spawnsPlayers;
    private GameState gameState = GameState.LOBBY;
    private List<Chest> openedChests;
    private boolean movementFreeze = false;

    public Game(String gameName) {
        this.maxPlayers = SkyWars.gameInfo.getConfig().getInt("games."+gameName+".maxPlayers");
        this.minPlayers = SkyWars.gameInfo.getConfig().getInt("games."+gameName+".minPlayers");
        this.world = Bukkit.createWorld(new WorldCreator(SkyWars.gameInfo.getString("games."+gameName+".worldName")));
        this.displayName = SkyWars.gameInfo.getConfig().getString("games."+gameName+".displayName");
        this.isTeamGame = SkyWars.gameInfo.getConfig().getBoolean("games."+gameName+".isTeamGame");

        try {
            String[] lobbyFormatted = SkyWars.gameInfo.getString("games."+gameName+".lobby").split("/");
            double xLobby = Double.parseDouble(lobbyFormatted[0]);
            double yLobby = Double.parseDouble(lobbyFormatted[1]);
            double zLobby = Double.parseDouble(lobbyFormatted[2]);
            this.lobby = new Location(world, xLobby,yLobby,zLobby);
        } catch (Exception err) {
            SkyWars.getInstance().getLogger().warning("Falha ao carregar o lobby do jogo " + gameName);
        }

        this.spawns = new ArrayList<>();
        if (SkyWars.gameInfo.getConfig().getStringList("games."+gameName+".spawns") != null) {
            SkyWars.gameInfo.getConfig().getStringList("games."+gameName+".spawns").forEach(spawn -> {
                //FORMAT -> x/y/z
                try {
                    String[] spawnFormatted = spawn.split("/");
                    double x = Double.parseDouble(spawnFormatted[0]);
                    double y = Double.parseDouble(spawnFormatted[1]);
                    double z = Double.parseDouble(spawnFormatted[2]);

                    spawns.add(new Location(world,x,y,z));
                } catch (Exception err) {
                    SkyWars.getInstance().getLogger().warning("Falha ao carregar o spawn " +spawn+" do jogo "+gameName);
                }

            });
        }

        this.normalItems = new ArrayList<>();
        this.rareItems = new ArrayList<>();


        for (String item : SkyWars.getInstance().getConfig().getStringList("games."+gameName+".normalItems")) {
            try {
                Material material = Material.valueOf(item);
                int count = 1;
                if (material == Material.ARROW) {
                    count = 32;
                }
                this.normalItems.add(new ItemStack(material, count));
            } catch(Exception err) {
                SkyWars.getInstance().getLogger().warning("Ocorreu um erro ao carregar o item " +item+" do mapa "+gameName);
            }
        }

        for (String item : SkyWars.getInstance().getConfig().getStringList("games."+gameName+".rareItems")) {
            try {
                Material material = Material.valueOf(item);
                int count = 1;
                if (material == Material.ARROW) {
                    count = 64;
                }
                this.rareItems.add(new ItemStack(material, count));
            } catch(Exception err) {
                SkyWars.getInstance().getLogger().warning("Ocorreu um erro ao carregar o item " +item+" do mapa "+gameName);
            }
        }



        this.players = new ArrayList<>();
        this.spectatorsPlayers = new ArrayList<>();
        this.spawnsPlayers = new HashMap<>();
        this.openedChests = new ArrayList<>();
    }

    public List<Chest> getOpenedChests() { return openedChests; }

    public void setOpenedChests(List<Chest> openedChests) { this.openedChests = openedChests; }

    public List<ItemStack> getRareItems() { return rareItems; }

    public List<ItemStack> getNormalItems() { return normalItems; }

    public boolean joinGame(GamePlayer gamePlayer) {
        if (gamePlayer.isTeamClass() && !isTeamGame) {
            return false;
        }

        if (isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            if (players.size() == maxPlayers) { gamePlayer.sendMessage("&4Jogo está cheio"); return false; }

            players.add(gamePlayer);
            gamePlayer.teleport(isState(GameState.LOBBY) ? lobby : null);
            sendMessage("&1 [+] &e" +gamePlayer.getName() + "&1 [+] &8("+getPlayers().size() + "/" + getMaxPlayers()+")");

            if (getPlayers().size() == getMinPlayers() && !isState(GameState.STARTING)) {
                setGameState(GameState.STARTING);
                sendMessage("&aO jogo iniciará em 20 segundos.");
                startCountDown();
            }

            SkyWars.getInstance().setGame(gamePlayer.getPlayer(), this);
            return true;
        }
        spectatorsPlayers.add(gamePlayer);
        return true;
    }

    public void startCountDown() {
        int id = 0;

        for (GamePlayer gamePlayer : getPlayers()) {
            try{
                SkyWars.getInstance().getLogger().warning(spawns.get(id).getX()+"/"+spawns.get(id).getY()+"/"+spawns.get(id).getZ());
                spawnsPlayers.put(gamePlayer, spawns.get(id));
                gamePlayer.teleport(spawns.get(id));
                id++;
            } catch (Exception err) {
                SkyWars.getInstance().getLogger().warning("Ocorreu um erro no game " +gamePlayer.getName());
            }
        }

        new GameCountDownTask(this).runTaskTimer(SkyWars.getInstance(), 0, 20);
    }

    public boolean isState(GameState state) { return gameState == state; }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() { return gameState; }

    public boolean isTeamGame() { return isTeamGame; }

    public int getMaxPlayers() { return maxPlayers; }

    public int getMinPlayers() { return minPlayers; }

    public List<GamePlayer> getPlayers() { return players; }

    public List<GamePlayer> getSpectatorsPlayers() { return spectatorsPlayers; }

    public List<Location> getSpawns() { return spawns; }

    public String getDisplayName() { return displayName; }

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.isTeamClass()) {
                //parada
            } else {
                if (gamePlayer.getPlayer() == player) {
                    return gamePlayer;
                }
            }
        }

        for (GamePlayer gamePlayer : getSpectatorsPlayers()) {
            if (gamePlayer.isTeamClass()) {
                //parada
            } else {
                if (gamePlayer.getPlayer() == player) {
                    return gamePlayer;
                }
            }
        }

        return null;
    }

    public void sendMessage(String message) {
        if (getPlayers().size() == 0) return;
        getPlayers().forEach(gamePlayer -> {
            gamePlayer.sendMessage(message);
        });

    }

    public void switchToSpectator(GamePlayer gamePlayer) {
        if (gamePlayer != null) {
            getPlayers().remove(gamePlayer);
            getSpectatorsPlayers().add(gamePlayer);
        }
    }

    public boolean isMovementFreeze() {
        return movementFreeze;
    }

    public World getWorld() { return world; }

    public void setMovementFreeze(boolean movementFreeze) {
        this.movementFreeze = movementFreeze;
    }

    public enum GameState {
        LOBBY, STARTING, IN_GAME, DEATH_MATCH, ENDING
    }

}
