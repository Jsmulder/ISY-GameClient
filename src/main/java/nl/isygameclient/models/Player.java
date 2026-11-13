package nl.isygameclient.models;

import nl.isygameclient.util.Vector2D;

public abstract class Player {

    protected Game game;
    protected String playerName;
    protected String playingAs;

    public Player(String playerName, String playingAs) {
        this.playerName = playerName;
        this.playingAs = playingAs;
    }

    public abstract Vector2D<Integer, Integer> onPlayerTurn();

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayingAs() {
        return playingAs;
    }

    public void setPlayingAs(String playingAs) {
        this.playingAs = playingAs;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", playingAs='" + playingAs + '\'' +
                '}';
    }
}
